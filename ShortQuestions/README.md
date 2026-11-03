
## Question 1

Compare the data models of logs, metrics and traces: what a single record contains, how the storage volume of each signal grows (with traffic, with the number of label combinations, or with the number of services a request touches), and which kind of question each signal answers best. Explain why a telemetry pipeline is itself a high-throughput write-heavy, low-frequency-read distributed system.
Answer:

| Signal | Single record | Storage growth | Best questions |
|---|---|---|---|
| Logs | Timestamp, severity, message/body, service/resource attributes, and often trace/span IDs | Roughly proportional to the number of emitted events and bytes per event; often grows with traffic and verbosity | What exactly happened? Which exception, input, or error message occurred? |
| Metrics | Metric name, numeric value (or histogram buckets), timestamp, and a set of labels identifying a time series | Roughly proportional to unique label combinations × samples per second × retention; traffic may change values without creating more series | How much/how often? What is the error rate, CPU usage, or p95 latency over time? |
| Traces| A span containing trace ID, span ID, parent span ID, operation name, start/end times, attributes, status, and optional events/links | Roughly proportional to sampled requests × spans per request × average span size; spans tend to increase as requests cross more instrumented services/operations | Where did time go? Which dependency or service caused a request to slow down or fail? |

Examples:

- A Java application emits a log: `2026-11-03T10:00:00Z ERROR CheckoutService Payment timed out traceId=abc123`.
- A metric is `http_server_requests_total{service="checkout",status="500"} 42`. Each distinct combination of `service`, `status`, and other labels forms a separate time series.
- A trace may contain `API Gateway → CheckoutService → PaymentService → Database`, with each instrumented operation represented by a span.

Growth and cardinality: Logs generally increase with event volume. Metrics can explode even with modest traffic when labels such as `user_id` or `request_id` create millions of distinct series. Traces grow with the number of sampled requests and the spans produced by each request; fan-out and nested operations increase span counts. Sampling reduces trace storage but can omit rare events unless sampling policies account for them.

Why the telemetry pipeline is a distributed system: Thousands of applications may continuously emit data, producing a high-throughput, write-heavy ingestion workload. Most raw telemetry is never queried; reads are comparatively sporadic and typically focus on recent incidents or aggregated trends (although dashboards can generate frequent metric reads). A production pipeline therefore needs:

1. Horizontally scalable ingestion:agents/collectors, partitioned queues, and multiple ingestion workers.
2. Buffering and backpressure: absorb bursts and protect downstream storage; define dropping or sampling policies under overload.
3. Durability and delivery semantics: retries, batching, acknowledgments, and duplicate handling where needed.
4. Efficient storage: compression, indexing tailored to the signal, retention tiers, and object storage for older data.
5. Multi-tenancy and reliability: quotas, authentication, isolation, monitoring, and graceful degradation.

An example flow is `Java services → OpenTelemetry Collector → queue/buffer → signal-specific processors → logs/metrics/traces storage → dashboards and queries`.

---

## Question 2

Compare the two families of log storage: a full-text inverted index and a label index with compressed chunks in object storage. For each family describe what is paid at write time and at read time, which query patterns are fast or slow, and how each one is hurt by high cardinality. State which family you would choose for a platform with very high volume and mostly "this service, last 15 minutes" searches, and why.

Answer:

Two common designs are full-text inverted indexing (as in Elasticsearch/OpenSearch-style deployments) and label indexing with compressed log chunks (as in Loki-style deployments).

| Dimension | Full-text inverted index | Label index + compressed chunks |
|---|---|---|
| Write-time cost | Parse/tokenize log text, build and update posting lists, maintain index segments, and perform merges; relatively CPU-, I/O-, and storage-intensive | Extract/index selected low-cardinality labels, buffer logs into chunks, compress and upload chunks; generally lower indexing overhead |
| Read-time cost | Use term indexes to find matching documents quickly, then fetch matching records | Use labels and time range to select chunks, then fetch/decompress and scan log contents for text matches |
| Fast queries | Arbitrary indexed keyword/field searches and complex text filters | Narrow label selectors plus bounded time windows, such as `{service="checkout"}` over 15 minutes |
| Slow queries | Expensive broad aggregations, wildcard/regex patterns, and large result sets; performance depends on mappings and indexes | Global text searches without selective labels, broad regex searches, or scans over many chunks |
| Storage trade-off | More index storage and write amplification in exchange for fast content search | Smaller indexes and economical compressed storage in exchange for more work during content searches |
| High-cardinality impact | Many unique field values/terms increase index size, memory requirements, and indexing/merge overhead; mappings can also explode | High-cardinality *indexed labels* create too many streams and small chunks, reducing compression efficiency and increasing index/metadata overhead |

Important nuance: A unique value in log text is not automatically an indexed label in a label-index architecture. The major problem is promoting identifiers such as `request_id`, `user_id`, or `trace_id` into labels. Keep those identifiers in the log body or structured metadata instead, and index stable attributes such as `service`, `environment`, and `region`.

Recommendation: For a very high-volume platform whose dominant query is “this service, last 15 minutes,” choose a label index with compressed chunks in object storage.

Reasons:

1. `service` and time are selective, predictable filters, so the system can locate a limited set of chunks.
2. Indexing only a small number of stable labels reduces ingest CPU and index storage.
3. Compression and object storage lower costs at large log volumes.
4. Horizontal ingestion and query workers can scale independently.

Trade-off: Arbitrary searches across all services and long time periods may require scanning many chunks and will often be slower than searches backed by a full-text index. If global free-text investigation is the primary workload, a full-text index may be a better choice. For very recent logs, the architecture may also read chunks from ingesters before they are flushed to object storage.

---

## Question 3

Compare pull-based and push-based metrics collection. Cover service discovery, detecting that a target is down, short-lived batch jobs, firewalls and network topology, who controls the collection rate, and what happens to the collector when an application misbehaves.

Answer:

Pull collection means the monitoring system periodically scrapes a target's metrics endpoint (for example, Prometheus requesting `/metrics`). Push collection means the application, agent, or intermediary sends metrics to a receiver (for example, via OTLP or a remote-write-style pipeline).

| Concern | Pull-based | Push-based |
|---|---|---|
| Service discovery | Collector needs a list of targets, often from Kubernetes, DNS, or a service registry; discovery is central to scraping | Producers need a configured receiver endpoint; the receiver may not need to discover individual producers, though producer identity still needs management |
| Detecting a down target | Failed scrapes and `up == 0` provide a direct signal for previously discovered targets | Silence is ambiguous: the app may be down, disconnected, idle, or simply not emitting; use heartbeats or expected-reporting alerts |
| Short-lived batch jobs | A job may finish between scrapes and be missed; a suitable intermediary or alternate design may be required | A job can emit metrics on completion, making short-lived workloads easier to capture, provided delivery is reliable |
| Firewalls/network topology | Collector must reach target endpoints; inbound access to targets can be difficult across NAT, networks, or security boundaries | Producers initiate outbound connections, often easier through NAT/firewalls, but they must reach the receiver |
| Who controls collection rate? | Scraper controls scrape interval, concurrency, and timeout; target controls what it exposes | Producer/exporter controls emission frequency and batching; receiver can enforce quotas, throttling, or backpressure |
| Misbehaving application | Slow or broken endpoints can time out; bounded scrape concurrency/timeouts protect the collector, though expensive scrapes still consume resources | A noisy producer can overwhelm ingestion with excessive volume or cardinality; receiver needs rate limits, queues, quotas, and isolation |

### Design considerations

Pull example: In Kubernetes, Prometheus discovers Java services exposing Micrometer/Prometheus metrics at `/actuator/prometheus`, scrapes every 15 seconds, and records scrape health. A failed scrape is distinguishable from a successful scrape returning zero requests. If a target disappears from discovery, separate service-discovery or inventory checks may be needed; `up == 0` is not guaranteed to persist for removed targets.

Push example: A Java service exports metrics through OpenTelemetry SDK/Collector using OTLP. An agent can batch and retry outbound exports, and a central collector can apply per-tenant rate limits. However, if the service crashes before buffered metrics are sent, some measurements may be lost. Push also requires monitoring producer silence independently.

Short-lived jobs: Push often fits batch jobs better, but a push gateway must be managed carefully: stale metrics can remain after jobs finish, and job lifecycle cleanup matters. For some batch workloads, writing durable completion events or using a job-oriented metric pipeline is preferable.

Collector protection: Both approaches require bounded memory, concurrency limits, timeouts, quotas, and cardinality controls. Pull generally gives the collector stronger control over when data is requested; push offers better support for outbound-only networking and ephemeral workloads but needs stronger ingestion-side protection against floods.

Recommendation: Use pull for long-running, discoverable services in a network where the collector can reach metrics endpoints. Prefer push or a local collector for ephemeral jobs, cross-network deployments, or outbound-only environments. A hybrid design is common: collectors scrape local targets and then push aggregated or forwarded telemetry to central storage.

---

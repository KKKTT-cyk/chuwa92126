# HW18


## 1. Logs, Metrics, Traces, and Telemetry Pipeline

Logs, metrics, and traces have different data models and serve different purposes.

| Signal  |                Data Model                                 |                How Volume Grows                   | Best For |
| --- | --- | --- | --- |
| Logs    | Individual events with timestamps, severity, and messages | Number of events | Understanding what happened    |
| Metrics | Time-series samples with metric names, labels, and values | Number of unique label combinations (cardinality) | Monitoring trends and alerting |
| Traces  | Spans linked by trace and parent IDs                      | Number of requests and spans per request          | Finding latency and errors across services |

A telemetry pipeline is a high-throughput, write-heavy, low-read-frequency distributed system because applications continuously generate data, while queries mainly occur during monitoring and incident investigation. It uses agents, collectors, buffers, and specialized storage to handle large data volumes reliably. It must also remain available during incidents without slowing down applications.



## 2. Log Storage Design

There are two common approaches to log storage.

**Full-text inverted index:** This approach indexes individual terms in log messages and maps them to the corresponding log records. It has a high write cost because the system must tokenize log data and build indexes. It also requires additional storage for the indexes. However, it provides fast and flexible full-text searches and field-based queries. High-cardinality fields can increase index size and mapping complexity.

**Label index with compressed chunks:** This approach indexes only a small set of labels, such as service, environment, and log level. Log entries are grouped into compressed chunks and stored in object storage. It has lower write and storage costs because it avoids building a full-text index for every log entry. However, queries involving arbitrary keywords may be slower because the system needs to scan the contents of selected chunks. High-cardinality labels can also cause index growth and create many small chunks.

For a system with very high log volume and mostly service-specific searches over the last 15 minutes, I would choose the label-index and compressed-chunk approach. These queries can first use labels and time ranges to identify relevant chunks, avoiding the need for a full-text index. This reduces ingestion and storage costs while still supporting the system's primary query patterns.


## 3. Pull-based vs. Push-based Metrics Collection

| Aspect            |                  Pull                            |               Push                     |
| --- | --- | --- |
| Service Discovery | Collector discovers targets                      | Producers know the collector address   |
| Failure Detection | Failed scrapes indicate unavailable targets      | Missing data is ambiguous              |
| Short-lived Jobs  | May miss jobs between scrapes                    | Jobs can send metrics before exiting   |
| Network           | Collector must access targets                    | Producers can use outbound connections |
| Collection Rate   | Controlled by collector                          | Controlled by producers                |
| Overload          | Collector controls scrape frequency and timeouts | Requires rate limiting and buffering   |

Pull works well for long-running services because it provides centralized collection control and clear failure detection. Push is better for short-lived jobs and restricted network environments. A hybrid approach can use Pull for long-running services and Push for batch jobs.

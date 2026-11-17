# Kubernetes AI Inference Homework - Questions and Answers

## 1. What is API Priority and Fairness (APF), and how does it prevent a misbehaving controller from starving critical system traffic?

API Priority and Fairness is a Kubernetes API server mechanism that classifies incoming requests into priority levels and flows. Each priority level has a configured amount of concurrency and queueing behavior. The API server identifies a request using `FlowSchema` rules (for example, user, group, namespace, or verb) and sends it to the matching `PriorityLevelConfiguration`.

A noisy or broken controller can therefore be placed in a lower-priority level with a bounded concurrency share and queues. Its request burst is queued or throttled instead of consuming all API server request capacity. Critical traffic - such as node heartbeats, system controllers, and cluster-admin requests - is assigned a higher priority level and retains capacity. APF uses fair queueing and shuffle sharding to reduce the chance that one flow monopolizes a queue.

## 2. What is the difference between mutating admission and validating admission? What risks do external admission webhooks introduce?

Admission runs after authentication and authorization but before an object is persisted.

- Mutating admission may change an incoming object. Typical examples are defaulting fields, adding sidecars, and injecting labels or annotations. Kubernetes runs mutating admission first and may reinvoke it when later mutations make earlier logic relevant again.
- Validating admission decides whether the final object is acceptable. It cannot modify the object; it returns allow or deny. Typical uses include policy checks, security controls, and organization-specific guardrails.

External admission webhooks add a synchronous dependency to API writes. Their main risks are added latency, reduced API-server availability when the webhook is down or slow, network/TLS/certificate failures, and inconsistent policy behavior during rollout. A `failurePolicy: Fail` improves enforcement but can block creates and updates when the webhook fails; `Ignore` preserves availability but can let policy checks be bypassed temporarily. Webhooks should have narrow matching rules, short timeouts, high availability, and careful versioning.

## 3. How do informers, local caches, work queues, and reconciliation functions work together in a Kubernetes controller?

1. An informer lists objects from the API server, watches for changes, and maintains a local cache.
2. Event handlers receive add, update, and delete notifications and usually enqueue only a lightweight key such as `namespace/name`.
3. A rate-limiting work queue deduplicates keys, buffers bursts, and retries failed work with backoff.
4. Worker threads dequeue a key and call the controller's reconciliation function.
5. Reconciliation reads the desired object and related resources from the informer cache, compares actual state with desired state, then creates, updates, or deletes resources as needed. It updates status when appropriate.

The cache keeps ordinary reads off the API server; the queue turns many event notifications into controlled, retryable work. A controller should treat events as hints rather than as the source of truth.

## 4. Why should Kubernetes reconciliation be level-triggered and idempotent? How can a controller recover from missed events and stale caches?

Level-triggered reconciliation asks, "given the desired state now, what actions are required to make reality match it?" It does not depend on receiving every transition event. This is essential because watches can disconnect, duplicate events can occur, events may be coalesced, and a controller may restart.

Idempotency means running reconcile repeatedly for the same desired state has the same safe result. Controllers should use declarative create-or-update/patch operations, stable resource names, owner references, observed generation, and status conditions rather than non-repeatable side effects.

To recover, an informer performs a fresh list and re-establishes its watch after disconnects. Controllers can requeue after errors, use periodic resyncs or explicit requeues, and rely on the cache eventually converging. For actions that require a read-after-write guarantee, a controller may use an API-server read or wait until the informer observes the new resource version. Correctness must come from reconciling the current state, not from a perfect event history.

## 5. What are Custom Resource Definitions (CRDs) and Operators, and how do they extend Kubernetes functionality?

A CustomResourceDefinition (CRD) extends the Kubernetes API with a new resource type, such as `InferenceService`, `ModelDeployment`, or `DatabaseCluster`. A CRD can define names, scope, versions, OpenAPI schema validation, defaulting, status and scale subresources, and printer columns. Users then manage custom resources with normal Kubernetes API tools such as `kubectl`.

An Operator is a controller (often packaged with its CRD) that watches those custom resources and encodes operational knowledge in a reconciliation loop. For example, an inference Operator could create Deployments, Services, autoscalers, model-storage configuration, and monitoring resources from one `InferenceService` custom resource. CRDs define the desired-state API; the Operator continuously implements it.

## 6. What are the differences between resource requests, limits, capacity, and allocatable resources? How do QoS classes affect pod eviction?

- Requests are the CPU and memory amounts used by the scheduler for placement. They also influence CPU shares and memory protection behavior at runtime.
- Limits are the maximum resource amounts a container may use. CPU overage is throttled; memory usage beyond the effective limit normally leads to an OOM kill.
- Capacity is the total resource quantity reported by a node.
- Allocatable is the portion of capacity Kubernetes makes available to Pods after reservations for the OS, kubelet, and eviction thresholds. The scheduler bases placement on allocatable resources and already-requested resources.

QoS is derived from container CPU and memory requests and limits:

- Guaranteed: every container has CPU and memory requests equal to its corresponding limits.
- Burstable: at least one request or limit is set, but the Pod is not Guaranteed.
- BestEffort: no CPU or memory requests or limits are set.

Under node resource pressure, kubelet generally evicts BestEffort Pods before Burstable Pods, and Burstable before Guaranteed Pods, while considering priority and actual usage relative to requests. Guaranteed Pods receive the strongest protection, but no QoS class makes a Pod absolutely unevictable.

## 7. How does the Kubernetes scheduler select a node for a pod? Explain filtering, scoring, reservation, and binding.

For an unscheduled Pod, the scheduler follows this broad flow:

1. Filtering: remove nodes that cannot run the Pod. Common checks include available requested resources, node selectors/required affinity, taints and tolerations, volume constraints, topology rules, host ports, and inter-Pod affinity rules.
2. Scoring:rank the feasible nodes using plugins such as resource balancing, preferred affinity, topology spread, image locality, and custom scheduler policies. The highest aggregate score wins.
3. Reservation (assumption): optimistically reserve the Pod's requested resources on the selected node in the scheduler cache. This prevents simultaneous scheduling decisions from overcommitting it.
4. Binding: write a binding (or update `spec.nodeName`) through the API server. The kubelet for that node then observes and starts the Pod. If binding fails, the scheduler rolls back the reservation and retries.

## 8. What are the differences between node affinity, pod affinity, taints and tolerations, and priority-based preemption?

- Node affinity constrains or prefers nodes according to node labels. `requiredDuringSchedulingIgnoredDuringExecution` is a hard placement rule; `preferred...` is a scoring preference.
- Pod affinity places a Pod near Pods with particular labels, using a topology domain such as a hostname or zone. Pod anti-affinity keeps matching Pods apart. These rules help availability and locality but can make large clusters more expensive to schedule.
- Taints are attributes on nodes that repel Pods. A Pod needs a matching toleration to schedule onto or remain on a tainted node. Effects are `NoSchedule`, `PreferNoSchedule`, and `NoExecute`; the last can evict already-running Pods that do not tolerate it.
- Priority-based preemption is a last-resort scheduler behavior. If a high-priority Pod cannot be scheduled, the scheduler may choose a node and evict lower-priority Pods so the pending Pod can fit. It does not override every constraint; for example, required affinity, taints, and resource feasibility must still be satisfied.

## 9. What are the respective responsibilities of the kubelet, Container Runtime Interface (CRI), and Container Network Interface (CNI) when starting a pod?

The kubelet watches the API server for Pods assigned to its node and drives the Pod lifecycle. It validates the Pod, mounts volumes and secrets, obtains images through the runtime, configures the sandbox, starts containers, runs probes, and reports status.

The CRI is the gRPC contract between kubelet and a compatible container runtime such as containerd or CRI-O. Through CRI, kubelet asks the runtime to pull images, create the Pod sandbox, create/start/stop containers, fetch logs/status, and clean up. The runtime performs the low-level container work.

The CNI is the plugin contract used to set up Pod networking. When the runtime creates a sandbox, it invokes the configured CNI plugin to add network interfaces, assign IP addresses, configure routes, and apply any network-specific policy plumbing; it calls CNI again on teardown. In short: kubelet orchestrates, CRI runs containers, and CNI connects the Pod to the network.

## 10. How do Services, EndpointSlices, kube-proxy, and CoreDNS work together to enable service discovery and traffic routing? How does a headless Service differ from a regular ClusterIP Service?

A Service provides a stable logical name and, for most Service types, a stable virtual IP (ClusterIP). The EndpointSlice controller selects ready Pods matching the Service selector and publishes their IP addresses and ports in one or more EndpointSlices. EndpointSlices scale better than the older monolithic Endpoints object.

CoreDNS watches Services and EndpointSlices and answers DNS queries such as `my-service.my-namespace.svc.cluster.local`. For a normal ClusterIP Service, DNS usually returns the Service's ClusterIP. kube-proxy on each node watches Service and EndpointSlice changes and programs packet-routing rules (commonly iptables or IPVS) so traffic sent to the ClusterIP is load-balanced to a ready backend Pod endpoint.

A headless Service sets `clusterIP: None`. It has no virtual ClusterIP and kube-proxy does not provide Service-level virtual-IP load balancing. DNS returns the individual endpoint IPs instead, allowing clients to discover and choose Pods directly. This is commonly used by StatefulSets and systems that need direct peer discovery. A regular ClusterIP Service hides individual endpoints behind one stable virtual IP and load-balances connections to them.

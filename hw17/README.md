# HW17


## 1. Why Thread-per-Request Wastes Resources for I/O-Bound Work

A thread-per-request model assigns one thread to each incoming request. For I/O-bound work, the thread spends most of its time waiting for database queries or external API responses rather than performing CPU work. A request that waits 20 ms on a database and 150 ms on an external API occupies its thread for about 175 ms while using the CPU for only a few milliseconds, and while it is blocked it cannot serve other requests.

As a result:

- Concurrency is limited by the thread pool size rather than by CPU or network capacity.
- Slow dependencies hold threads longer and can exhaust the thread pool, which also affects unrelated endpoints.
- Increasing the pool size consumes more memory and increases context-switching overhead.


## 2. The Reactor Pattern

- **Demultiplexer:** Monitors multiple connections and reports which ones are ready for I/O.
- **Event loop:** Waits for events and dispatches them to the appropriate handlers.
- **Acceptor:** Accepts new connections and registers them with the demultiplexer.
- **Handlers:** Process read and write events for individual connections.

Blocking an event loop thread prevents it from processing any other connection assigned to that loop, so one slow call delays every client on it. Event loops are sized to the number of cores, so a few blocked threads can stall the whole server. Blocking operations such as JDBC calls must be moved to a separate scheduler.


## 3. Reactive Streams

The specification defines four interfaces:

- `Publisher<T>`: `subscribe(Subscriber)`
- `Subscriber<T>`: `onSubscribe`, `onNext`, `onError`, `onComplete`
- `Subscription`: `request(long n)`, `cancel()`
- `Processor<T, R>`: Both a `Subscriber<T>` and a `Publisher<R>`

The sequence of signals is `subscribe()`, then `onSubscribe(subscription)`, then `subscription.request(n)`, then at most `n` `onNext` signals, and finally `onComplete` or `onError`. The protocol is `onSubscribe onNext* (onComplete | onError)?`: `onSubscribe` occurs first and exactly once, the two terminal signals are mutually exclusive, and signals to one subscriber are serialized.

`request(n)` is the backpressure channel. The publisher may emit no more items than the subscriber has requested, so the consumer sets the pace. Demand is cumulative, and `request(Long.MAX_VALUE)` means unbounded demand.


## 4. `Mono`, `Flux`, and Lazy Assembly

`Mono<T>` emits zero or one item and then completes. `Flux<T>` emits zero to N items and may be infinite.

"Nothing happens until you subscribe" means that operator calls only assemble a description of the work. No request is sent and no scheduler is used until `subscribe()` runs, so a pipeline that is never subscribed to never executes.

```java
Mono<Report> a = Mono.just(buildReport());               // eager
Mono<Report> b = Mono.fromCallable(() -> buildReport()); // lazy
```

- `Mono.just(buildReport())` is an ordinary method call. `buildReport()` runs immediately at assembly time, on the calling thread, even if nobody subscribes. Every subscriber then receives the same already-computed value, and a thrown exception escapes as a normal exception rather than an `onError` signal.
- `Mono.fromCallable(() -> buildReport())` captures the lambda. `buildReport()` runs once per subscription, at subscribe time, exceptions become `onError` signals, and the call can be moved to another thread with `subscribeOn`.


## 5. `map`, `flatMap`, `concatMap`, and `flatMapSequential`

| Operator | Function returns | Inner calls concurrent | Output order |
|---|---|---|---|
| `map` | a plain value `R` | no, synchronous transform | source order |
| `flatMap` | `Publisher<R>` | yes, up to 256 by default | interleaved, completion order |
| `concatMap` | `Publisher<R>` | no, one inner at a time | source order |
| `flatMapSequential` | `Publisher<R>` | yes | source order, results buffered |

Use `flatMap` when only throughput matters, `concatMap` when calls must not overlap, and `flatMapSequential` when calls should run in parallel but the output must stay ordered.


## 6. Error Handling in a Pipeline

A `try/catch` around the code that builds a pipeline only wraps the assembly phase. That code creates the operator chain and returns immediately, while the actual work happens later, at subscribe time and often on a different thread. A failure then travels down the chain as an `onError` signal rather than as a thrown exception, so it must be handled by operators inside the chain.

- `onErrorReturn(value)`: Replaces an error with a fixed fallback value.
- `onErrorResume(...)`: Switches to another `Publisher` when an error occurs.
- `onErrorMap(...)`: Transforms the original error into another exception.
- `retryWhen(...)`: Resubscribes to the source according to a retry strategy, so it is only safe for idempotent operations.
- `timeout(...)`: Emits a `TimeoutException` and cancels the source if the operation takes too long.


## 7. `publishOn`, `subscribeOn`, and Blocking Calls

- `subscribeOn(scheduler)` controls where the subscription happens and therefore where the source starts emitting. It affects the chain upstream of itself regardless of its position, and only the first `subscribeOn` in a chain takes effect.
- `publishOn(scheduler)` switches the thread for every operator downstream of the point where it appears, and it can be used several times in one chain.

A blocking JDBC call should be wrapped with `Schedulers.boundedElastic()`, which is designed to host blocking work and is bounded, so a slow database cannot spawn unlimited threads:

```java
Mono.fromCallable(() -> jdbcTemplate.queryForObject(sql, mapper))
    .subscribeOn(Schedulers.boundedElastic());
```

`Schedulers.parallel()` is sized to the number of cores and is meant for short non-blocking CPU work, so it must not be blocked.


## 8. Backpressure

Backpressure is the mechanism that prevents a fast producer from overwhelming a slow consumer. The publisher may emit only what the subscriber has requested, so the slowest stage controls the rate and memory stays bounded. A subscriber expresses demand by calling `request(n)` on the `Subscription` it receives in `onSubscribe`, usually through operators such as `limitRate(n)` rather than by hand; a plain `subscribe(...)` requests unbounded demand.

When the source cannot slow down, as with `Flux.interval`, an overflow strategy must be chosen:

- `onBackpressureBuffer()`: Queues the extra items, which risks an `OutOfMemoryError` if unbounded.
- `onBackpressureDrop()`: Discards items that arrive with no demand.
- `onBackpressureLatest()`: Keeps only the most recent item.
- `onBackpressureError()`: Fails fast with an `OverflowException`.

Operators such as `sample` and `buffer` can also reduce the volume instead of dropping arbitrary items.


## 9. Cold and Hot Publishers

A cold publisher creates its data source per subscription, so each subscriber triggers an independent execution and receives the full sequence. A hot publisher emits independently of its subscribers, so late subscribers see only what is emitted after they join.

A `WebClient` `Mono` is cold: it describes a request, and the request is sent by the act of subscribing. Subscribing twice therefore sends two HTTP requests. The same duplication happens implicitly through `retry` or through two branches over the same `Mono`.

- `cache()`: The first subscription triggers the single call, the result is retained, and all later subscribers replay it without a new request.
- `share()`: Multicasts to the subscribers currently present. There is no replay for late subscribers, and the source is cancelled when the subscriber count drops to zero.


## 10. Spring MVC and Spring WebFlux

- **Concurrency model:** MVC uses thread-per-request with blocking I/O; WebFlux uses event loops with non-blocking I/O.
- **Return types:** MVC returns values such as `User`; WebFlux returns `Mono` and `Flux`.
- **Data access:** MVC uses JDBC and JPA; WebFlux needs non-blocking drivers such as R2DBC.
- **Scaling limit:** MVC is limited by the thread pool size; WebFlux by memory and in-flight requests.
- **Backpressure:** MVC has none beyond TCP; WebFlux propagates it end to end.

WebFlux is the better choice for high concurrency with slow I/O, for streaming, and when backpressure must propagate end to end, but only if the whole path is non-blocking.

Spring MVC with virtual threads is the better choice when the stack must stay blocking, as with JDBC, JPA, or legacy SDKs. Virtual threads make blocking cheap without a rewrite, since a virtual thread costs a few hundred bytes of heap instead of a 1 MB operating system thread stack, whereas WebFlux would only park those calls on `boundedElastic` and lose most of the benefit. Straight-line code also keeps meaningful stack traces, `try/catch`, and `ThreadLocal` and MDC working.

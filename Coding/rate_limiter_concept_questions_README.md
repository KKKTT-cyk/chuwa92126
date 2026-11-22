# Rate Limiter - Conceptual Questions and Answers

## 1. Fixed Window Counter

### (a) Boundary bursts

A fixed-window counter resets at fixed boundaries, such as at the start of each minute. A client can send the full limit just before a boundary and another full limit just after it. For a limit of 100 per minute, nearly 200 requests could arrive in a few milliseconds while neither individual minute exceeds 100.

This is inherent in the fixed-window definition: requests on opposite sides of the boundary belong to different windows. Changing how the counter is stored inside the class cannot change that grouping. A sliding-window or token-bucket algorithm is needed if the policy must constrain traffic over every rolling interval. Fixed windows remain a good fit for calendar-aligned quotas, such as 1,000 calls per calendar day that reset at midnight, where the boundary is part of the requirement.

### (b) Plain HashMap and concurrent requests

Suppose two threads read a count of 9 for a limit of 10. Both decide that the next request is allowed, and both write 10. The stored count increases only once even though two requests were accepted. A similar race can occur while reading or replacing the window timestamp and count. A plain `HashMap` does not make the compound read-check-update operation atomic, so Version 1 is not thread-safe.

Version 1 is suitable for a single-threaded caller, a teaching example, or a context that guarantees external synchronization. It is not suitable for concurrent request handling without a lock around the complete operation.

### (c) `ConcurrentHashMap.compute`

`compute(key, function)` performs the remapping for that key atomically: concurrent computations for the same key do not interleave their read-and-update logic, so each request sees the state left by the prior computation. This lets Version 2 decide and update a key's window and count as one operation.

Other updates may be delayed briefly if they contend on the same internal map bin or lock region; the operation is not a promise that unrelated keys can never contend. The function should be short and should not make slow external calls. The map makes the per-key state update safe inside one process; it does not coordinate separate service instances.

### (d) Injected clock

Taking time from a `LongSupplier` makes time a dependency that can be controlled. Production code normally supplies `System::currentTimeMillis` (or an equivalent clock adapter). A test supplies a deterministic fake clock, such as a supplier backed by a mutable value, so it can advance across window boundaries without sleeping. This makes boundary and expiration tests fast and repeatable.

## 2. Sliding Window Rate Limiters

### (a) Sliding-window log

The log stores the timestamp of each accepted request. On a new request at time `now`, it removes timestamps at or before `now - windowLength`. If fewer than the configured limit remain, it records `now` and accepts; otherwise it rejects.

The decision considers the exact preceding interval, regardless of fixed calendar boundaries. Requests just before a boundary remain in the log until they actually age out, so crossing a boundary cannot immediately grant a second full quota.

### (b) Log memory cost

The log needs one timestamp per request that is still inside the window. At a limit of `L`, a busy key can therefore retain up to about `L` timestamps (plus collection overhead). A fixed-window counter keeps only a window marker and a count for each key, so its per-key state is constant-sized regardless of the limit.

The log is appropriate when an exact rolling-window limit matters and the per-key limit/traffic is modest, for example protecting login attempts or other abuse-sensitive actions. Its memory and cleanup costs may be too high for large limits and very high key counts.

### (c) Sliding-window counter estimate

The counter keeps the count in the current fixed window and the count in the immediately preceding window. It estimates the rolling-window count by weighting the previous window according to the fraction of that window that overlaps the rolling interval:

`estimate = currentCount + previousCount * (1 - elapsedInCurrentWindow / windowLength)`

It then compares this estimate with the configured limit. The estimate assumes requests were spread roughly uniformly through the previous window. The two counts alone do not reveal the exact timestamps of those requests, so the result is approximate.

### (d) Loose and strict estimates

If the previous-window requests all occurred near its beginning, many or all may already be outside the current rolling interval. The weighted estimate still counts part of them, so it can overestimate usage and be too strict, rejecting a request that an exact log would allow.

If the previous-window requests all occurred near its end, they may all still be inside the current rolling interval. The estimate discounts some of them based on the elapsed fraction, so it can underestimate usage and be too loose, allowing more requests than an exact rolling limit would. The sliding-window counter is a useful compromise when low memory and high throughput matter more than exact per-request accounting and a small approximation error is acceptable.

## 3. Token Bucket and Leaky Bucket

### (a) Idle bursts and parameters

A token bucket accumulates tokens while the client is idle, up to its capacity. The client can spend those saved tokens quickly when it becomes active. Fixed-window and sliding-window limiters instead judge requests against counts in recent windows and may reject some of the same burst.

The bucket capacity sets the largest immediate burst that can pass (assuming enough tokens have accumulated). The refill rate sets the long-term average allowance. A typical configuration might have capacity 20 and refill rate 5 tokens per second.

### (b) Lazy token refill

The token bucket does not need a background timer. It stores the last-refill time and the available token count. When a request arrives, it computes elapsed time, adds `elapsed * refillRate` tokens up to the capacity, updates the stored time, and then spends a token if one is available.

A short-interval count can exceed what the refill rate alone suggests because the bucket may begin that interval with saved tokens. Over longer periods, the refill rate governs the sustained average, while capacity bounds how much stored allowance can be released at once.

### (c) Leaky bucket behavior

A token bucket permits bursts using accumulated tokens. A leaky bucket models a queue or reservoir that drains at a configured rate, so arrivals can be smoothed: requests are accepted only while the reservoir has room, and queued work (or the represented level) drains over time.

With a finite reservoir and immediate accept/reject behavior, the long-run accepted rate can depend on arrival pattern: a burst can fill the reservoir and cause later arrivals to be rejected while it drains, whereas evenly spaced arrivals may fit. For a lazy level-based implementation, the per-key state is the current level and the timestamp of its last update (plus fixed configuration such as capacity and leak rate). On each request, elapsed time implies how much has leaked; the class calculates that reduction then, so a timer does not need to update idle keys.

### (d) Choosing a version

1. 1,000 calls per calendar day, reset at midnight: Version 2, fixed window. The calendar boundary is explicitly required, and a counter is compact. Use the thread-safe variant when requests can run concurrently.
2. 5 login attempts in any minute: Version 3, sliding-window log. It enforces the exact rolling interval and is a sensible fit for a small, security-sensitive limit.
3. 600 requests per minute per API key, many keys, small error acceptable: Version 4, sliding-window counter. It keeps compact state per key and offers a useful approximation at scale.
4. 20 calls immediately when a page loads, then 5 calls per second on average: Version 5, token bucket. Set capacity to 20 and refill at 5 tokens per second, allowing the initial burst while controlling the sustained rate.
5. A third-party API rejects a second call within 200 ms: Version 6, leaky bucket / minimum-spacing limiter. Enforce at least 200 ms between accepted calls (equivalently, a paced drain rate of 5 calls per second with no burst capacity). If the tutorial's Version 6 is a finite-capacity leaky bucket rather than a strict spacing limiter, configure its capacity to one request and its drain interval to 200 ms.

## 4. Memory Management and Distributed Rate Limiting

### (a) Unbounded per-key map growth

If entries are never removed, memory usage depends on the number of distinct keys ever seen and the amount of state stored for each key. It does not depend only on current traffic or on how many requests are active now. Keys such as IP addresses or session IDs can have very high or attacker-controlled cardinality, causing stale entries to accumulate and potentially exhausting memory.

### (b) Safe cleanup and atomic removal

An entry can be removed when its stored state has fully expired and recreating it would produce exactly the same decision for every future request. For a fixed-window counter, that generally means the old window has ended. For a token bucket, it means the bucket has refilled to its full capacity, so discarding it and creating a full bucket has the same future behavior. For a leaky bucket, the state that must have expired is the current water/queue level: it must have drained completely. A last-access timestamp alone is not enough unless the cleanup threshold proves the state has reached that equivalent reset state.

A racy sweep might read an entry as expired; a request then updates that same entry and is accepted; afterward the sweep removes it. The next request creates a fresh entry with full capacity and is accepted again, effectively resetting the key while it still had active state.

Version 7 rules this out by making the expiration check and removal atomic with respect to updates for the same key (for example, using `computeIfPresent` to inspect and conditionally remove, while request updates use the same per-key atomic mechanism). The cleanup operation must confirm that the current value is still expired at the moment it removes it.

### (c) HTTP rejection response

Version 8 can return a retry delay computed from the key's state: for example, the time until the current window expires or until a token becomes available. An HTTP rate-limit rejection normally uses 429 Too Many Requests. The `Retry-After` header carries the delay, commonly as a non-negative number of whole seconds (rounded up so the client does not retry too early); HTTP also permits an HTTP-date form. The exact delay depends on the limiter and its implementation.

### (d) Per-instance limits

If each service instance runs its own independent limiter, each instance grants its own quota. A user routed across `N` instances may therefore receive roughly `N` times the intended service-wide allowance, with the exact result depending on load balancing and each instance's state.

Version 8 is still a good choice when there is only one instance, or when the intended policy is explicitly per-instance (or an approximate limit is acceptable and the multiplier is understood). A local limiter can also be useful as a protective layer in front of a shared global limit, but that is an additional use beyond a single global quota.

### (e) Shared-store rate limiting

The shared store must provide an atomic operation that combines the relevant read, decision, and update for a key. A separate read followed by a write is unsafe: two instances can both read the same count, both decide the request is allowed, and then overwrite one another's increments. An atomic increment with an expiry, a transaction/script, or an equivalent compare-and-update operation can provide the needed behavior, depending on the algorithm.

The class uses wall-clock milliseconds because separate service instances need a common time scale to calculate windows and expiration. Each counter needs a time to live so inactive keys are eventually removed from the shared store rather than accumulating forever. Each request now incurs a network round trip to the shared store (and its latency/availability cost).

Store-failure behavior is a policy choice: fail closed rejects requests when the store cannot make a trustworthy decision; fail open allows them to preserve application availability but temporarily bypasses the limit. The question PDF does not include Version 9's implementation, so it does not establish which behavior the tutorial's run uses. That choice must be made in advance based on whether protecting the downstream service or preserving request availability is more important.

The in-process race from Version 2 can reappear if the shared-store operation is implemented as multiple non-atomic steps, such as `GET`, local comparison, then `SET`. Sharing the store alone is not enough; the decision and update must be atomic at the store.

# HW4 – Java Multithreading and Concurrency

## Question 1.

`count++` looks like one operation, but it actually compiles to **three separate steps**:

1. **READ** `count` from memory (e.g. `count = 5`)
2. **ADD** 1 to the read value (`5 + 1 = 6`)
3. **WRITE** the new value back (`count = 6`)

Another thread can run between any of these steps. If two threads interleave, both can read the same old
value, and one update is lost:

```
Thread A                    Thread B
1. READ count (5)
                            1. READ count (5)    <- both read 5!
2. ADD -> 6
                            2. ADD -> 6
3. WRITE count = 6
                            3. WRITE count = 6   <- lost update!
```

Two increments happened, but `count` is 6 instead of 7. This is a **race condition**: with many threads, the
final count is **smaller than expected** and **non-deterministic**. For example, 1000 threads each incrementing
once may print 987, 993, or sometimes 1000 by luck.

---

## Question 2.

| | `Thread.sleep(ms)` | `obj.wait()` |
|---|---|---|
| Releases the lock? | **No**, it keeps holding any locks | **Yes**, it releases the lock while waiting |
| Wakes up when | The time elapses | `notify()`/`notifyAll()` is called on the same object, or a timeout |
| Static/Instance | Static method of `Thread` | Instance method (on any object) |

**Why `wait()` must be called inside a `synchronized` block:** `wait()` releases the object's lock and waits,
so the thread must **hold that object's lock** first. Otherwise there is no lock to release, and
`IllegalMonitorStateException` is thrown. The lock also protects the shared condition (e.g. "the queue is
empty"): the waiting thread checks the condition under the lock, and the thread that changes the condition
calls `notify()`/`notifyAll()` under the same lock.

---

## Question 3.

- An **instance-level `synchronized` method** locks on **`this`**, the current instance. Threads only block
  each other if they call it **on the same instance**.
- A **`static synchronized` method** locks on **`ClassName.class`**, the class-level lock. All threads calling
  it compete for the same lock, **regardless of which instance** they are on.

```java
public class Resource {
    public synchronized void instanceMethod() { }         // locks on "this"
    public static synchronized void staticMethod() { }    // locks on Resource.class
}
```

`this` and `Resource.class` are **different objects with different locks**. A thread holding the lock on
`this` does not hold the lock on `Resource.class`, so a thread calling the instance method and a thread calling
the static method **do not block each other**.

---

## Question 4.

**volatile guarantees:**
1. **Visibility**: every read sees the latest write from any thread (reads/writes go directly to main memory,
   not the CPU cache)
2. **Ordering**: prevents certain compiler/CPU instruction reordering around the variable

**volatile does NOT guarantee:**
- **Atomicity**: a read-modify-write operation is still multiple steps

**Scenario where volatile alone fails:**

```java
private volatile int count = 0;

public void increment() {
    count++; // STILL NOT ATOMIC, even with volatile!
}
```

Each thread always reads the latest `count`, but two threads can still both read 5 and both write 6
(Question 1), so increments are still lost. volatile only fixes visibility, not atomicity. The fix is
`synchronized`, a `Lock`, or `AtomicInteger`.

---

## Question 5.

**The four conditions for deadlock (Coffman conditions):**

| Condition | Meaning |
|---|---|
| **Mutual Exclusion** | Only one thread can hold a lock at a time |
| **Hold and Wait** | A thread holds a lock while waiting for another |
| **No Preemption** | A lock can't be forcibly taken away from a thread |
| **Circular Wait** | A cycle of threads exists, each waiting for the next's lock |

Breaking any one of these prevents deadlock.

**(a) Enforcing a consistent lock-acquisition order** breaks **Circular Wait**. If every thread acquires locks
in the same global order (e.g. by a fixed ID), no thread can hold a "later" lock while waiting for an
"earlier" one, so a cycle cannot form.

**(b) Using `tryLock` with a timeout** breaks **No Preemption**. If a thread cannot acquire the next lock in
time, it gives up and **releases the locks it already holds**, then backs off and retries instead of waiting
forever. The locks are effectively taken back from the thread.

---

## Question 6.

| Capability | `synchronized` | `ReentrantLock` |
|---|---|---|
| Try without blocking | No | Yes - `tryLock()` |
| Timeout support | No | Yes - `tryLock(timeout, unit)` |
| Interruptible wait | No | Yes - `lockInterruptibly()` |
| Fairness policy | No (JVM decides) | Yes - `new ReentrantLock(true)` |
| Multiple conditions | Only one wait-set per object | Multiple `Condition` objects via `newCondition()` |

**Use case 1: `tryLock(timeout, unit)` for a bank transfer.** A transfer needs the locks of two accounts. With
`tryLock` and a timeout, if a lock can't be acquired in time, the thread releases what it holds and retries
instead of waiting forever, which prevents deadlock.

**Use case 2: multiple `Condition` objects for a bounded producer-consumer queue.** With one lock and two
conditions, `notFull` and `notEmpty`, producers wait on `notFull` and consumers wait on `notEmpty`. A producer
then only wakes up consumers (`notEmpty.signal()`), instead of waking up every waiting thread like
`notifyAll()` does.

---

## Question 7.

CAS (Compare-And-Swap) is a **lock-free, hardware-supported atomic instruction**: "update the value only if it
still equals the expected value."

1. Thread A reads value = 5
2. Thread A computes newValue = 6
3. Thread A calls `compareAndSwap(expected=5, new=6)`
   - Is the current value **still 5**? **Yes** → success, value = 6
   - **No** (another thread changed it first) → fail, retry

**The retry loop** inside `incrementAndGet()`:

```java
public int incrementAndGet() {
    int current;
    int next;
    do {
        current = get();          // 1. read the current value
        next = current + 1;       // 2. compute the new value
    } while (!compareAndSet(current, next)); // 3. retry if another thread changed it
    return next;
}
```

If `compareAndSet` fails, the thread does not block. It goes back to the beginning of the loop, **reads the
latest value again**, recomputes, and tries again until it succeeds. This is "optimistic locking".

---

## Question 8.

`Collections.synchronizedMap()` wraps **every** method call in `synchronized` on **one single lock** for the
whole map (coarse-grained). Only one thread can access the map at a time, even if threads are working on
completely different keys.

`ConcurrentHashMap` uses **lock striping**: instead of one lock for the whole map, it divides the internal
storage into segments/buckets, **each independently lockable**. Different threads can operate on **different
buckets simultaneously**, and only threads accessing the same bucket compete for a lock. This gives much higher
concurrency.

---

## Question 9.

When a new task is submitted:

1. **Are fewer than `corePoolSize` threads running?**
   YES → create a new **core thread** to run it immediately.
2. Otherwise, **is the work queue full?**
   NO → **enqueue the task**; a core thread will pick it up later.
3. Otherwise (queue is full), **are fewer than `maximumPoolSize` threads running?**
   YES → create a new **extra (non-core) thread** to run it now.
4. Otherwise (queue and pool are both full) → apply the **rejection policy** (e.g. `AbortPolicy` throws
   `RejectedExecutionException`).

Note that the queue is checked **before** creating extra threads: threads beyond `corePoolSize` are only
created when the queue is full.

---

## Question 10.

| | CountDownLatch | CyclicBarrier |
|---|---|---|
| Reusable | **No** (one-time, cannot be reset) | **Yes** (resets automatically) |
| Who waits | **One or more threads wait for others** to finish | **All participating threads wait for each other** |
| Trigger action | None built-in | Optional `Runnable` when the barrier is tripped |

**CountDownLatch use case:** waiting for multiple parallel initialization tasks (loading config, connecting to
services) to finish before starting the application. The main thread calls `await()`, and each task calls
`countDown()` when it finishes.

**CyclicBarrier use case:** multi-phase parallel computations, e.g. parallel simulations, where all threads must
finish phase 1 before any can start phase 2. Each thread calls `await()` at the end of a phase, and the barrier
is reused for the next phase.

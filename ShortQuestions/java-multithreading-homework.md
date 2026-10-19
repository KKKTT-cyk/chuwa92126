# Java Multithreading Homework

## 1.  Explain why count++ is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute count++ concurrently.

`count++` looks like one operation in Java, but it is actually a read-modify-write sequence:

1. Read the current value of `count` from memory.
2. Add `1` to that value.
3. Write the new value back to `count`.

For example, suppose `count` is initially `5` and two threads execute `count++` at the same time. Thread A may read `5`, and Thread B may also read `5` before Thread A writes its result. Both threads calculate `6` and both write `6`. The expected result is `7`, but the actual result can be `6`. This is called a **lost update** and is a race condition.

```java
class Counter {
    private int count = 0;

    public void increment() {
        count++; // Not atomic
    }
}
```

Using `synchronized`, a `Lock`, or an atomic class can make the update thread-safe.

---

## 2. Explain the core difference between sleep() and wait() (whether the lock is released, and what wakes the thread up), and explain why wait() must be called inside a synchronized block.

`Thread.sleep()` pauses the currently executing thread for a specified amount of time. If the thread currently owns a monitor lock, `sleep()` does not release that lock. The thread becomes runnable again after the sleep time expires, although the scheduler determines exactly when it runs.

`wait()`, on the other hand, is an `Object` method used for communication between threads. When a thread calls `wait()`, it **releases the monitor lock** for that object and enters the waiting state. It can be awakened by another thread calling `notify()` or `notifyAll()` on the same object. A timed `wait()` may also wake after its timeout, and waiting can also end through interruption or a spurious wakeup.

`wait()` must be called while the thread owns the object's monitor, normally inside a `synchronized` block or synchronized method. Otherwise, Java throws `IllegalMonitorStateException`.

```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
}
```

The synchronized block is important because checking the condition and beginning to wait must be coordinated using the same monitor.

---

## 3. Explain the difference between locking on this (an instance-level synchronized method) and locking on ClassName.class (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.

An instance-level synchronized method locks the **specific object instance**, equivalent to synchronizing on `this`.

```java
public synchronized void instanceMethod() {
    // Locks this object
}
```

A static synchronized method locks the class's **Class object**, such as `MyClass.class`.

```java
public static synchronized void staticMethod() {
    // Locks MyClass.class
}
```

These are different monitor objects. Therefore, a thread executing a synchronized instance method and another thread executing a static synchronized method do not block each other just because the methods belong to the same class. One thread owns the instance monitor, while the other owns the class monitor.

Two threads calling synchronized instance methods on the same instance do block each other. Two threads calling static synchronized methods of the same class also block each other.

---

## 4. What does volatile guarantee, and what does it NOT guarantee? Give a concrete code scenario where adding volatile alone fails to fix a concurrency bug.

`volatile` primarily provides **visibility** and ordering guarantees. When one thread writes to a volatile variable, another thread reading that variable will see the latest volatile write according to Java's happens-before rules. It also prevents certain instruction reorderings around volatile accesses.

However, `volatile` does **not** make compound operations such as `count++` atomic.

```java
class Counter {
    private volatile int count = 0;

    public void increment() {
        count++;
    }
}
```

This code is still not thread-safe because `count++` performs a read, increment, and write. Two threads can read the same value and overwrite each other's updates. `volatile` makes the value visible but does not make the entire read-modify-write sequence indivisible.

A good use of `volatile` is a simple shared status flag:

```java
private volatile boolean running = true;
```

---

## 5. List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using tryLock with a timeout.

The four **Coffman conditions** are:

1. Mutual exclusion – At least one resource can be held by only one thread at a time.
2. Hold and wait – A thread holds one resource while waiting to acquire another.
3. No preemption – A resource cannot simply be forcibly taken away from a thread; it must be released voluntarily.
4. Circular wait – A cycle exists where each thread waits for a resource held by the next thread in the cycle.

### Strategy A: Consistent lock-acquisition order

If every thread acquires locks in the same global order, a circular dependency cannot form. Therefore, this strategy breaks the **circular wait** condition.

```java
synchronized (lockA) {
    synchronized (lockB) {
        // Code
    }
}
```

Every thread that needs both locks should acquire `lockA` before `lockB`.

### Strategy B: `tryLock()` with a timeout

With `ReentrantLock.tryLock(timeout, unit)`, a thread does not have to wait forever for another lock. If acquisition fails, it can release locks it already holds and retry later. This is commonly used to prevent an indefinite hold-and-wait cycle by backing out rather than waiting forever.

---

## 6. Explain what additional capabilities ReentrantLock provides over synchronized, and give a realistic use case for two of them.

`ReentrantLock` provides more explicit and flexible locking features than the `synchronized` keyword, including:

- `tryLock()` to attempt lock acquisition without waiting forever.
- Timed `tryLock()` to wait only for a limited amount of time.
- `lockInterruptibly()` so a thread waiting for a lock can respond to interruption.
- Optional fairness policies.
- Multiple `Condition` objects associated with one lock.
- Explicit lock/unlock operations.

A realistic use case for timed `tryLock()` is transferring money between two accounts. If the second account lock cannot be obtained within a certain period, the operation can release the first lock and retry, reducing deadlock risk.

A realistic use case for multiple `Condition` objects is a bounded producer-consumer buffer. One condition can represent `notFull` and another can represent `notEmpty`, allowing more targeted signaling than a single monitor wait set.

Always release a `ReentrantLock` in a `finally` block:

```java
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();
}
```

---

## 7. Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like incrementAndGet() uses internally when the compare-and-set step fails.

CAS is an atomic operation that compares a variable's current value with an expected value. If they are equal, CAS replaces the current value with a new value atomically. If the value has changed, the update fails.

Conceptually:

```text
compareAndSet(expectedValue, newValue)
```

A method such as `incrementAndGet()` can conceptually use a retry loop like this:

```java
while (true) {
    int current = get();
    int next = current + 1;

    if (compareAndSet(current, next)) {
        return next;
    }
}
```

Suppose Thread A and Thread B both read `5`. Both calculate `6`. Thread A successfully changes `5` to `6`. Thread B then tries to change `5` to `6`, but the current value is already `6`, so its CAS fails. Thread B retries, reads `6`, calculates `7`, and attempts the CAS again.

This allows atomic classes to perform many updates without using a traditional monitor lock.

---

## 8.Explain how ConcurrentHashMap achieves higher concurrency than a HashMap wrapped with Collections.synchronizedMap(), referencing the idea of lock striping.

A map wrapped using `Collections.synchronizedMap()` effectively protects map operations with a single shared mutex. As a result, concurrent operations that require that mutex are serialized.

```java
Map<String, Integer> map =
        Collections.synchronizedMap(new HashMap<>());
```

`ConcurrentHashMap` is designed for finer-grained concurrency. The classic implementation is often explained using **lock striping**: instead of locking the entire map, different portions of the map can be updated independently. Modern Java implementations use finer-grained techniques, including CAS and locking individual bins when necessary, rather than the old fixed `Segment` design.

As a result, multiple threads can often access or update different parts of a `ConcurrentHashMap` concurrently, while reads generally avoid locking. This provides much better scalability under concurrent access than synchronizing an entire `HashMap` with one mutex.

---

## 9. Describe the order of checks a ThreadPoolExecutor performs when a new task is submitted, with respect to corePoolSize, the task queue, and maximumPoolSize.

When a new task is submitted through `execute()`, `ThreadPoolExecutor` conceptually follows this order:

1. If the number of running worker threads is less than `corePoolSize`, create a new worker thread to execute the task.
2. Otherwise, try to place the task into the work queue.
3. If the queue cannot accept the task, try to create another worker, as long as the number of workers is less than `maximumPoolSize`.
4. If the queue cannot accept the task and the pool is already at `maximumPoolSize`, reject the task using the configured `RejectedExecutionHandler`.

For example:

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
        2,                      // corePoolSize
        4,                      // maximumPoolSize
        60,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10)
);
```

The executor first grows to 2 core workers. Additional tasks are then queued. If the queue fills, the executor can grow toward 4 workers. Once both the queue and maximum worker capacity are exhausted, additional tasks are rejected.

---

## 10. Explain the difference between CountDownLatch and CyclicBarrier (reusability, and who is waiting for whom), and give one appropriate use case for each.

`CountDownLatch` is generally used when one or more threads need to wait until a set of operations finishes. Its counter decreases when `countDown()` is called. Once the counter reaches zero, waiting threads are released. A `CountDownLatch` can't be reset or reused.

Example use case: the main thread starts three initialization tasks and waits until all three complete before starting the application.

```java
CountDownLatch latch = new CountDownLatch(3);

// Worker threads call:
latch.countDown();

// Main thread waits:
latch.await();
```

`CyclicBarrier` is used when a fixed group of peer threads must all reach the same synchronization point before any of them continue. Each participating thread calls `await()`. When all parties arrive, they are released. A `CyclicBarrier` is **reusable**, so the same threads can synchronize again in another phase.

Example use case: several worker threads perform a multi-stage calculation. After each stage, every worker waits at the barrier until all other workers finish that stage.
---
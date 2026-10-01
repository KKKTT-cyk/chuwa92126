# Java Multithreading Homework — Conceptual Questions

## 1. Why is `count++` not an atomic operation?

`count++` is not atomic because it consists of multiple steps:

1. Read the current value of `count`.
2. Add `1` to the value.
3. Write the new value back to `count`.

If two threads execute `count++` at the same time, both threads may read the same original value.

For example, if `count = 5`:

- Thread A reads `5`.
- Thread B reads `5`.
- Thread A calculates `6` and writes `6`.
- Thread B calculates `6` and writes `6`.

The expected result is `7`, but the actual result can be `6`. This is called a **race condition** or **lost update**.

---

## 2. What is the difference between `sleep()` and `wait()`?

The main difference is whether the thread releases the lock.

- `Thread.sleep()` pauses the current thread for a specified amount of time, but it **does not release any lock** the thread currently holds.
- `wait()` causes the thread to wait and **releases the object's monitor lock** while waiting.

A sleeping thread normally wakes up when the specified sleep time expires or when it is interrupted.

A waiting thread can wake up because another thread calls `notify()` or `notifyAll()` on the same object, because it is interrupted, or because a timeout expires if timed `wait()` is used.

`wait()` must be called while the thread owns the object's monitor, normally inside a `synchronized` block or method. Otherwise, Java throws an `IllegalMonitorStateException`.

Example:

```java
synchronized (lock) {
    lock.wait();
}
```

---

## 3. What is the difference between locking on `this` and locking on `ClassName.class`?

An instance-level synchronized method locks the current object instance:

```java
public synchronized void methodA() {
}
```

This is effectively locking on:

```java
synchronized (this) {
}
```

A static synchronized method locks the class object:

```java
public static synchronized void methodB() {
}
```

This is effectively locking on:

```java
synchronized (ClassName.class) {
}
```

Therefore, `this` and `ClassName.class` are two different lock objects.

A thread executing an instance synchronized method and another thread executing a static synchronized method on the same class do not block each other because they are acquiring different locks.

---

## 4. What does `volatile` guarantee, and what does it NOT guarantee?

`volatile` mainly guarantees **visibility**. When one thread changes a volatile variable, other threads can see the updated value.

For example:

```java
private volatile boolean running = true;
```

If one thread changes:

```java
running = false;
```

another thread reading `running` will see the updated value.

However, `volatile` does **not** make compound operations atomic.

For example:

```java
private volatile int count = 0;

public void increment() {
    count++;
}
```

This code is still not thread-safe because `count++` contains multiple operations:

1. Read `count`.
2. Add `1`.
3. Write the result.

Two threads can still read the same value and overwrite each other's updates.

Therefore, `volatile` alone cannot fix this race condition. `synchronized`, `Lock`, or an atomic operation would be required.

---

## 5. What are the four necessary conditions for deadlock?

The four Coffman conditions for deadlock are:

1. **Mutual Exclusion** — A resource can be held by only one thread at a time.
2. **Hold and Wait** — A thread holds one resource while waiting for another resource.
3. **No Preemption** — A resource cannot simply be forcibly taken away from a thread.
4. **Circular Wait** — A circular chain exists where each thread waits for a resource held by another thread.

### Strategy A: Consistent Lock-Acquisition Order

If every thread acquires locks in the same order, circular dependencies cannot form.

For example:

```text
Lock A → Lock B
```

All threads must acquire Lock A before Lock B.

This breaks the **circular wait** condition.

### Strategy B: `tryLock()` with a Timeout

With `tryLock()`, a thread can stop waiting if it cannot obtain a lock within a certain amount of time.

It can then release locks it already holds and retry later instead of waiting forever.

This prevents the thread from remaining indefinitely in a deadlocked lock-acquisition sequence.

---

## 6. What additional capabilities does `ReentrantLock` provide over `synchronized`?

`ReentrantLock` provides several capabilities beyond basic `synchronized`, including:

- `tryLock()`
- Timed lock attempts
- Interruptible lock acquisition with `lockInterruptibly()`
- Optional fairness
- Multiple `Condition` objects

### Use Case 1: `tryLock()`

A program may need multiple locks but should not wait forever for them.

```java
if (lock.tryLock()) {
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
}
```

If the lock is unavailable, the thread can perform another operation or retry later.

### Use Case 2: Fair Locks

A fair lock can be created with:

```java
Lock lock = new ReentrantLock(true);
```

Fairness generally favors threads that have been waiting longer. This can be useful when reducing thread starvation is important.

---

## 7. How does CAS (Compare-And-Swap) work?

CAS stands for **Compare-And-Swap**.

It conceptually performs the following operation:

```text
Compare current value with expected value.

If they are equal:
    replace current value with new value
    return success

Otherwise:
    return failure
```

For example, suppose:

```text
count = 5
```

A thread attempts:

```text
CAS(count, 5, 6)
```

If `count` is still `5`, it is changed to `6`.

If another thread has already changed `count`, the CAS operation fails.

A method such as `incrementAndGet()` can conceptually use a retry loop:

```java
while (true) {
    int oldValue = get();
    int newValue = oldValue + 1;

    if (compareAndSet(oldValue, newValue)) {
        return newValue;
    }
}
```

If `compareAndSet()` fails because another thread changed the value first, the thread reads the latest value and tries again.

---

## 8. How does `ConcurrentHashMap` achieve higher concurrency than `Collections.synchronizedMap()`?

A map created with:

```java
Collections.synchronizedMap(new HashMap<>());
```

uses synchronization around map operations, which can cause many threads to contend for the same map-level lock.

`ConcurrentHashMap` is designed to allow more concurrent access.

The basic idea can be understood through **lock striping**: instead of protecting the entire map using one global lock, different parts of the map can be updated independently.

Conceptually:

```text
Map
├── Part 1 → Thread A
├── Part 2 → Thread B
└── Part 3 → Thread C
```

Therefore, operations on unrelated parts of the map do not necessarily block each other.

Modern `ConcurrentHashMap` implementations use techniques such as CAS and fine-grained synchronization rather than simply placing one lock around the entire map.

As a result, `ConcurrentHashMap` generally provides better performance when many threads access the map concurrently.

---

## 9. What order of checks does `ThreadPoolExecutor` perform when a new task is submitted?

When a new task is submitted, `ThreadPoolExecutor` generally performs the checks in this order:

1. If the number of worker threads is less than `corePoolSize`, create a new worker thread.
2. Otherwise, try to place the task into the task queue.
3. If the queue cannot accept the task, try to create another worker thread, up to `maximumPoolSize`.
4. If the pool has reached `maximumPoolSize` and the queue cannot accept the task, reject the task using the configured rejection policy.

The simplified order is:

```text
corePoolSize
      ↓
task queue
      ↓
maximumPoolSize
      ↓
rejection
```

For example:

```java
new ThreadPoolExecutor(
    2,      // corePoolSize
    5,      // maximumPoolSize
    60,
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(10)
);
```

The executor first creates workers up to the core size. After that, tasks are normally placed into the queue. If the queue becomes full, the executor can create additional workers up to the maximum pool size.

---

## 10. What is the difference between `CountDownLatch` and `CyclicBarrier`?

`CountDownLatch` and `CyclicBarrier` both coordinate multiple threads, but they are used differently.

### CountDownLatch

`CountDownLatch` allows one or more threads to wait until a certain number of operations have completed.

For example:

```java
CountDownLatch latch = new CountDownLatch(3);
```

Worker threads call:

```java
latch.countDown();
```

The waiting thread calls:

```java
latch.await();
```

When the count reaches `0`, the waiting thread can continue.

A `CountDownLatch` **cannot be reset and reused**.

A common use case is having the main thread wait for several worker threads to finish initialization.

### CyclicBarrier

`CyclicBarrier` allows a group of threads to wait for **each other** to reach the same synchronization point.

For example:

```java
CyclicBarrier barrier = new CyclicBarrier(3);
```

Each worker calls:

```java
barrier.await();
```

When all three threads reach the barrier, they can all continue.

Unlike `CountDownLatch`, a `CyclicBarrier` is **reusable**.

A common use case is a multi-phase parallel computation where all workers must finish one phase before any worker starts the next phase.

### Summary

| Feature | `CountDownLatch` | `CyclicBarrier` |
|---|---|---|
| Reusable | No | Yes |
| Main idea | Wait for tasks/events to complete | Threads wait for each other |
| Main methods | `countDown()`, `await()` | `await()` |
| Typical use | Main thread waits for workers | Workers synchronize at phases |
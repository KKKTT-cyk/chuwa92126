# Java Multithreading Homework 4

## 1. Why is `count++` not atomic?

`count++` looks like one operation in Java source code, but it is actually a read-modify-write sequence:

1. Read the current value of `count` from memory.
2. Add `1` to that value.
3. Write the new value back to `count`.

If two threads execute `count++` at the same time, both threads can read the same old value before either one writes the result back.

For example, suppose `count = 10`:

- Thread A reads `10`.
- Thread B reads `10`.
- Thread A calculates `11` and writes `11`.
- Thread B also calculates `11` and writes `11`.

The final value becomes `11`, even though two increments occurred. The correct result should be `12`. This is called a lost update.

---

## 2. Difference between `sleep()` and `wait()`

`Thread.sleep()` pauses the current thread for a specified amount of time, but it does **not** release any monitor lock that the thread currently owns. The thread normally wakes up after the sleep time expires, or earlier if it is interrupted.

`wait()` is used for thread coordination. When a thread calls `wait()` on an object, it releases that object's monitor lock and enters the waiting state. It can wake up when another thread calls `notify()` or `notifyAll()` on the same object, or when it is interrupted. A timed version of `wait()` can also wake up after the timeout expires.

`wait()` must be called inside a `synchronized` block or synchronized method because the calling thread must own the monitor of the object before it can release that monitor and enter the object's wait set. Otherwise, Java throws `IllegalMonitorStateException`.

Example:

```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
}
```

---

## 3. Locking on `this` vs. locking on `ClassName.class`

An instance-level synchronized method locks the current object, which is the same as locking on `this`.

```java
public synchronized void instanceMethod() {
    // locks this
}
```

A static synchronized method locks the `Class` object for that class, which is the same as locking on `ClassName.class`.

```java
public static synchronized void staticMethod() {
    // locks ClassName.class
}
```

These are two different lock objects. The instance method uses the monitor of a particular instance, while the static method uses the monitor of the class object. Therefore, a thread holding the instance lock does not automatically hold the class lock, and the two threads do not block each other unless the code explicitly uses the same lock object.

Also, two different instances have different instance locks, so synchronized instance methods on different objects can run at the same time.

---

## 4. What does `volatile` guarantee, and what does it NOT guarantee?

`volatile` mainly guarantees **visibility** and certain **ordering** properties.

If one thread writes a new value to a volatile variable, another thread that later reads that variable will see the most recent write. It also prevents some instruction reordering around volatile reads and writes.

However, `volatile` does **not** make compound operations atomic.

For example:

```java
public class Counter {
    private volatile int count = 0;

    public void increment() {
        count++;
    }
}
```

This is still not thread-safe because `count++` contains a read, increment, and write. Two threads can read the same value and overwrite each other's update. `volatile` makes the latest value visible, but it does not prevent the lost-update race condition.

To fix this, we need synchronization, a lock, or an atomic class such as `AtomicInteger`.

---

## 5. Four Coffman conditions for deadlock

The four necessary conditions for deadlock are:

1. **Mutual exclusion** - at least one resource can be held by only one thread at a time.
2. **Hold and wait** - a thread holds one resource while waiting for another resource.
3. **No preemption** - a resource cannot simply be forcibly taken away from the thread that owns it.
4. **Circular wait** - there is a cycle of threads where each thread is waiting for a resource held by the next thread.

### (a) Consistent lock-acquisition order

If every thread always acquires locks in the same global order, a circular dependency cannot form. Therefore, this strategy breaks the **circular wait** condition.

For example, if every thread must always acquire `lockA` before `lockB`, one thread cannot hold `lockB` while waiting for `lockA` and create the opposite dependency.

### (b) `tryLock()` with a timeout

A common pattern is to use `tryLock(timeout)` instead of waiting forever. If the second lock cannot be acquired within the timeout, the thread releases any lock it already holds and retries later.

That pattern breaks the **hold-and-wait** condition because a thread does not continue holding one lock indefinitely while waiting for another lock.

Important: `tryLock()` by itself does not automatically prevent deadlock. The code must handle failure by releasing already-acquired locks and backing off.

---

## 6. Additional capabilities of `ReentrantLock` over `synchronized`

`ReentrantLock` provides several features that normal `synchronized` blocks do not directly provide:

- `tryLock()` - attempt to acquire a lock without waiting forever.
- Timed `tryLock()` - wait for a limited amount of time.
- `lockInterruptibly()` - allow a thread waiting for a lock to respond to interruption.
- Optional fairness policy - construct the lock with `new ReentrantLock(true)` to favor threads that have waited longer.
- Multiple `Condition` objects - create multiple wait sets from one lock instead of using only one monitor wait set.
- Lock state inspection methods such as `isLocked()` and `getHoldCount()`.

Two realistic use cases are:

1. **Timed `tryLock()` in a banking transfer system:** if a thread cannot obtain the second account lock within a short timeout, it can release the first lock and retry. This reduces the risk of deadlock.
2. **Multiple `Condition` objects in a bounded buffer:** one condition can represent `notFull` and another can represent `notEmpty`, allowing producers and consumers to signal only the group of threads that should wake up.

---

## 7. How CAS works and how `incrementAndGet()` retries

CAS stands for Compare-And-Swap or Compare-And-Set. It is an atomic operation that compares the current value in memory with an expected value.

Conceptually, CAS does this:

```text
if (currentValue == expectedValue) {
    currentValue = newValue;
    return true;
} else {
    return false;
}
```

The comparison and update happen atomically, so another thread cannot change the value between those two steps.

A method such as `incrementAndGet()` can use a retry loop like this:

```java
while (true) {
    int oldValue = getCurrentValue();
    int newValue = oldValue + 1;

    if (compareAndSet(oldValue, newValue)) {
        return newValue;
    }
}
```

If another thread changes the value after `oldValue` is read, `compareAndSet()` fails because the current value is no longer equal to the expected value. The thread then reads the latest value, recalculates the new value, and tries again until the CAS succeeds.

---

## 8. `ConcurrentHashMap` vs. `Collections.synchronizedMap()`

A `HashMap` wrapped by `Collections.synchronizedMap()` uses one synchronized wrapper lock for map operations. This means many operations are effectively serialized: while one thread is inside a synchronized map operation, other threads trying to use the same map lock must wait.

`ConcurrentHashMap` uses much finer-grained synchronization. The general idea is similar to **lock striping**: instead of protecting the entire map with one global lock, different parts of the map can be updated independently. Therefore, threads working on different buckets or regions can often proceed concurrently.

In modern Java implementations, `ConcurrentHashMap` uses techniques such as CAS and fine-grained locking on individual bins rather than the older fixed `Segment` design, but the important idea is still that it avoids one global lock for the whole map.

Reads are also designed to be highly concurrent and normally do not lock the entire map.

As a result, `ConcurrentHashMap` usually scales much better than `Collections.synchronizedMap()` when many threads access the map at the same time.

---

## 9. Order of checks in `ThreadPoolExecutor`

When a new task is submitted to a `ThreadPoolExecutor`, the simplified order is:

1. **Check `corePoolSize`.**  
   If the current number of worker threads is less than `corePoolSize`, create a new worker thread to run the task, even if some existing workers are idle.

2. **Try to put the task into the work queue.**  
   If the core pool is already full, the executor tries to add the task to the queue.

3. **Check `maximumPoolSize`.**  
   If the queue cannot accept the task, usually because the queue is full, the executor tries to create another worker thread, as long as the current worker count is below `maximumPoolSize`.

4. **Reject the task.**  
   If the queue is full and the number of workers has already reached `maximumPoolSize`, the executor uses its configured `RejectedExecutionHandler`.

So the main decision order is:

```text
corePoolSize -> queue -> maximumPoolSize -> rejection
```

After a task is queued, the real implementation also rechecks the executor state to handle shutdown and other race conditions safely.

---

## 10. `CountDownLatch` vs. `CyclicBarrier`

### `CountDownLatch`

A `CountDownLatch` starts with a fixed count. Worker threads call `countDown()` when they finish some work, while one or more waiting threads call `await()` until the count reaches zero.

It is **one-time use**. After the count becomes zero, it cannot be reset.

The typical relationship is: **one thread waits for other threads or events to finish**.

Example use case: a main thread starts five worker threads and waits until all five workers complete initialization before continuing.

### `CyclicBarrier`

A `CyclicBarrier` is used when a fixed number of participating threads must all reach the same synchronization point. Each thread calls `await()`. When the required number of threads has arrived, the barrier opens and all of them continue.

It is **reusable**, so after one group passes the barrier, the same barrier can be used again for another phase.

The typical relationship is: **peer threads wait for each other**.

Example use case: a multi-step parallel algorithm where all worker threads must finish phase 1 before any of them begins phase 2.

---

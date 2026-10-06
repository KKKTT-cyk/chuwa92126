# HW4

## 1. Explain why `count++` is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute `count++` concurrently.

`count++` is not atomic because it consists of three steps: reading the current value, adding 1, and writing the result back. When multiple threads execute it concurrently, they may read the same value and overwrite each other's updates. For example, two threads incrementing `count` from 0 may produce 1 instead of 2. This is called a **lost update**.

## 2. Explain the core difference between `sleep()` and `wait()` (whether the lock is released, and what wakes the thread up), and explain why `wait()` must be called inside a synchronized block.

`sleep()` pauses the current thread without releasing any locks. It wakes up when the specified time expires or it is interrupted.

`wait()` releases the object's lock and waits until it is notified, times out, or is interrupted. Spurious wakeups are also possible. The thread must reacquire the lock before continuing.

`wait()` must be called while holding the object's monitor, inside a `synchronized` block or method; otherwise, an `IllegalMonitorStateException` is thrown.

## 3. Explain the difference between locking on `this` (an instance-level synchronized method) and locking on `ClassName.class` (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.

An instance-level synchronized method locks `this`, the current instance. A static synchronized method locks `ClassName.class`, the class object. Threads calling synchronized instance methods on the same instance share one lock, while threads calling static synchronized methods of the same class share the class lock. An instance synchronized method and a static synchronized method do not block each other because they use different locks.

## 4. What does `volatile` guarantee, and what does it NOT guarantee? Give a concrete code scenario where adding `volatile` alone fails to fix a concurrency bug.

`volatile` guarantees visibility of updates across threads and ordering, but it does not guarantee atomicity of compound operations.

```java
volatile int count = 0;

count++;
```

Both threads may read 0 and write back 1, so the result may be 1 instead of 2. Using `synchronized` or `AtomicInteger` fixes this problem.

## 5. List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using `tryLock` with a timeout.

The four necessary conditions for deadlock are **mutual exclusion**, **hold and wait**, **no preemption**, and **circular wait**.

**(a)** Enforcing a consistent lock-acquisition order breaks **circular wait**, because all threads acquire locks in the same order.

**(b)** Using `tryLock()` with a timeout, and releasing already-held locks if acquisition fails, breaks **hold and wait** by preventing a thread from continuing to hold locks while waiting for another. A timeout alone is not sufficient.

## 6. Explain what additional capabilities `ReentrantLock` provides over `synchronized`, and give a realistic use case for two of them.

`ReentrantLock` provides capabilities beyond `synchronized`, including non-blocking and timed lock acquisition with `tryLock()`, interruptible lock acquisition with `lockInterruptibly()`, optional fairness, and multiple `Condition` objects.

For example, an order-processing request can use timed `tryLock()` to avoid waiting indefinitely for an inventory lock. A cancellable background task can use `lockInterruptibly()` so it can stop waiting for a lock when the user cancels it.

## 7. Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like `incrementAndGet()` uses internally when the compare-and-set step fails.

CAS (Compare-And-Swap) atomically compares the current value with an expected value. If they match, it updates the value; otherwise, it fails without changing anything.

A CAS-based `incrementAndGet()` reads the current value, adds 1, and attempts the update. If CAS fails, it reads the latest value, recalculates, and retries until successful. It then returns the updated value.

## 8. Explain how `ConcurrentHashMap` achieves higher concurrency than a `HashMap` wrapped with `Collections.synchronizedMap()`, referencing the idea of lock striping.

`Collections.synchronizedMap()` uses one lock for the entire map, so threads must take turns. `ConcurrentHashMap` uses finer-grained locking, allowing threads to update different parts concurrently, while reads generally do not require locking. This follows the idea of **lock striping**: using separate locks for different parts to reduce contention.

## 9. Describe the order of checks a `ThreadPoolExecutor` performs when a new task is submitted, with respect to `corePoolSize`, the task queue, and `maximumPoolSize`.

When a new task is submitted, `ThreadPoolExecutor` checks in this order:

1. If there are fewer than `corePoolSize` threads, create a new thread to run the task.
2. Otherwise, try to add the task to the queue.
3. If the queue is full and there are fewer than `maximumPoolSize` threads, create another thread.
4. If the queue is full and the pool has reached `maximumPoolSize`, use the rejection policy.

## 10. Describe the order of checks a `ThreadPoolExecutor` performs when a new task is submitted, with respect to `corePoolSize`, the task queue, and `maximumPoolSize`.

**`CountDownLatch`** is not reusable. One or more threads wait for other threads to complete tasks and call `countDown()`. For example, the main thread waits for several services to finish initialization.

**`CyclicBarrier`** is reusable. A group of threads wait for one another to reach the same point before continuing. For example, parallel workers wait at the end of each simulation round before starting the next round.

## 11. Implement a thread-safe counter class `SafeCounter` in Java with `increment()` and `getCount()` methods, such that concurrent calls from multiple threads always produce a correct result. You may not use any class from `java.util.concurrent.atomic` — use only `synchronized` or `Lock`. Write complete, compilable code.

```java
public class SafeCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

## 12. Without using `java.util.concurrent.BlockingQueue`, implement a bounded blocking queue `BoundedQueue<T>` using only `wait()`/`notifyAll()`. It should provide `put(T item)` and `take()` methods: `put()` blocks when the queue is full, and `take()` blocks when the queue is empty. Write complete, compilable code.

```java
import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedQueue<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    public BoundedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException("Item cannot be null");
        }
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.remove();
        notifyAll();
        return item;
    }
}
```

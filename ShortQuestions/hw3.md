# Homework 3 - Multithreading

## Question 1. Why is count++ not atomic?

`count++` has three steps:

1. Read the current value of count.
2. Add 1 to that value.
3. Write the new value back to count.

Two threads can both read 0, both calculate 1, and both write 1. The final value is 1 instead of 2. This is a lost update. We need to protect the whole operation, for example with `synchronized`.

## Question 2. How are sleep() and wait() different?

`Thread.sleep()` pauses the current thread for a duration, unless it is interrupted. It does not release any monitor locks the thread holds. After the time passes, the thread becomes eligible to run; it may not run immediately.

`wait()` releases the monitor of the object it is called on and waits. The thread can wake because of `notify()`, `notifyAll()`, interruption, a timeout for timed wait, or a spurious wakeup. Before leaving `wait()`, it must acquire that monitor again. It does not release locks on other objects.

The thread must own the same object's monitor when calling `wait()`. Usually this means calling it inside `synchronized (object)` or a synchronized instance method on that object. Otherwise Java throws `IllegalMonitorStateException`. This lets checking the condition and starting to wait happen under the same lock, so a notification is not missed between those steps. Always check the condition in a `while` loop because waking up does not guarantee it is now true.

## Question 3. What is the difference between instance and static synchronized methods?

An instance synchronized method locks `this`, so calls on the same instance share a lock. Calls on different instances use different locks.

A static synchronized method locks `ClassName.class`. All calls to static synchronized methods of that class share this class-object lock.

An ordinary instance and its class object are different objects. Therefore, one thread in an instance synchronized method does not block another thread from entering a static synchronized method just because both methods belong to the same class. If both methods access the same shared data, they need to use the same lock to protect it.

## Question 4. What does volatile guarantee, and when is it not enough?

A write to a volatile field happens-before a subsequent read of that field. This provides visibility and ordering: a thread reading the published value also sees the writes that happened before it. Reads and writes of the volatile field itself are atomic, including volatile long and double fields.

It does not provide mutual exclusion or make several operations into one atomic operation. It also does not automatically make an object referenced by a volatile field thread-safe.

```java
class BrokenCounter {
    private volatile int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

If two threads call `increment()` at the same time, they can still read the same value and overwrite each other's increment. Volatile makes the individual reads and writes visible, but the read-add-write sequence is not atomic. Using synchronized methods for both incrementing and reading fixes this problem.

## Question 5. What are the four deadlock conditions, and how do the strategies help?

The four Coffman conditions are:

- Mutual exclusion: a resource can be held by only one thread at a time.
- Hold and wait: a thread holds one resource while waiting for another.
- No preemption: a resource cannot be forcibly taken away; its owner releases it.
- Circular wait: a cycle exists where each thread waits for a resource held by the next thread.

**Consistent lock order** breaks circular wait. If every thread takes lock A before lock B, one thread cannot hold B while waiting for A.

**tryLock with a timeout** lets a thread give up instead of waiting forever. In a deadlock-avoidance strategy, the thread releases any locks it already acquired when the next lock attempt times out, then retries later. This abandons hold and wait and allows other threads to progress. A timeout alone is not enough if the thread keeps its first lock and repeatedly waits for the second. This is voluntary release, not Java forcibly taking a lock from its owner. Backoff can also help avoid repeated collisions or livelock.

## Question 6. What extra features does ReentrantLock provide?

`ReentrantLock` supports immediate `tryLock()`, timed `tryLock()`, interruptible acquisition with `lockInterruptibly()`, optional fair acquisition, and multiple `Condition` objects for one lock. Both it and `synchronized` are reentrant: a thread can acquire a lock it already owns.

Two use cases:

- A request has a short deadline. A timed `tryLock()` lets it stop waiting and return a busy result if the lock cannot be acquired in time.
- A background worker is waiting for a shared resource when the user cancels the job. `lockInterruptibly()` lets interruption cancel its wait for the lock.

A bounded queue can also use separate conditions for not-empty and not-full. With a ReentrantLock, unlocking must be done explicitly, normally in `finally` after successful acquisition. Synchronized releases its monitor automatically when leaving the block, even if an exception occurs.

## Question 7. How does CAS work, and why retry?

CAS takes an expected value and a new value. As one atomic action, it compares the current value with the expected value. If they match, it writes the new value and reports success. If they do not match, it leaves the value unchanged and reports failure.

The basic increment retry pattern looks like this:

```java
int incrementAndGet(AtomicInteger value) {
    while (true) {
        int oldValue = value.get();
        int newValue = oldValue + 1;
        if (value.compareAndSet(oldValue, newValue)) {
            return newValue;
        }
    }
}
```

This example needs `import java.util.concurrent.atomic.AtomicInteger;`. It illustrates CAS for this short answer; question 11 uses synchronized instead.

If another thread changes the value before the CAS, the comparison can fail. The loop reads the latest value and tries again, so it does not overwrite that other update using a stale calculation. This is the conceptual CAS loop; an actual JDK may implement `incrementAndGet()` using an intrinsic atomic get-and-add instruction instead of this exact Java loop. CAS does not guarantee that each competing thread succeeds within a fixed time.

## Question 8. Why does ConcurrentHashMap allow more concurrency?

`Collections.synchronizedMap(new HashMap<>())` uses one wrapper mutex for its synchronized operations. Calls such as `get()` and `put()` contend for that same lock even when the keys are unrelated. Traversing its collection views requires the caller to synchronize on the wrapper too.

Lock striping means dividing the data into parts protected by different locks. Java 7 ConcurrentHashMap used separately locked segments. Java 8 and later use a different implementation: reads generally do not lock, and updates use CAS or synchronization at the bin level, with extra coordination for resizing. Updates to different bins can often proceed at the same time. Updates to the same bin can still contend.

This avoids one global lock for every ordinary operation. However, several separate method calls do not automatically become one atomic action. For example, use `putIfAbsent()` instead of a separate `containsKey()` followed by `put()` when inserting only if absent.

## Question 9. How does ThreadPoolExecutor handle a submitted task?

For a pool that is running, the main order is:

1. If there are fewer workers than `corePoolSize`, try to create a worker with the task.
2. Otherwise, try to put the task into the work queue using a non-blocking offer.
3. If the queue cannot accept it, try to create another worker, up to `maximumPoolSize`.
4. If the task cannot be queued or assigned to a new worker, invoke the rejection handler. Shutdown can also cause rejection.

After successfully queuing a task, the executor rechecks its state. If it has shut down, it tries to remove and reject the task. Otherwise, if there are no workers, it tries to start one to drain the queue.

The queue is tried before growing beyond the core size. Therefore, with an unbounded queue, tasks normally keep queuing after the core workers exist, and a larger maximumPoolSize usually does not create extra workers.

## Question 10. How do CountDownLatch and CyclicBarrier differ?

A `CountDownLatch` starts with a count. Threads call `countDown()` to reduce it, and threads calling `await()` wait for zero. The threads doing the work and the threads waiting can be different. Once the count reaches zero, it stays there; the latch cannot be reset.

Example: the main thread waits for three services to finish initialization before starting the application. Each service counts down once.

A `CyclicBarrier` has a fixed number of parties. Each participating thread calls `await()` when it reaches the barrier. They wait for each other, and once all parties arrive, they can continue. The barrier can be reused for the next round and can run an optional barrier action before releasing the parties. If interruption or a timeout breaks a round, the barrier needs to be reset or replaced before reuse.

Example: several threads calculate different parts of a simulation. They meet at the barrier after each time step before moving to the next step.

## Questions 11 and 12. Coding

The complete Java files are in the IntelliJ project:

- `org.tiff.homework3.question11`: `SafeCounter` and `Main`.
- `org.tiff.homework3.question12`: `BoundedQueue<T>` and `Main`.

Run each package's `Main` separately. SafeCounter uses synchronized methods. BoundedQueue uses synchronized methods with wait and notifyAll, without BlockingQueue.

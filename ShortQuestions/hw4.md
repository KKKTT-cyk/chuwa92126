## Java Concurrency Homework

### Dengtai Wang

#### Conceptual Questions

1. **Explain why count++ is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute count++ concurrently.**

   Answer:

   count++ is actually doing:

   1. Read the current value of count
   2. Add 1 to the value
   3. Write the new value back to count
   
   If two threads may execute count++ at the same time:

   ```text
   Thread A reads count = 0
   Thread B reads count = 0
   
   Thread A calculates 1
   Thread B calculates 1
   
   Thread A writes 1
   Thread B writes 1
   ```
   
   The correct result should be 2, but the final result is only 1.
   
2. **Explain the core difference between sleep() and wait() (whether the lock is released, and what wakes the thread up), and explain why wait() must be called inside a synchronized block.**

   Answer:

   ​	sleep() pauses the current thread for a certain amount of time. If the thread currently owns a lock, sleep() does not release that lock.

   ​	wait() is different. When a thread calls obj.wait(), it releases the monitor lock on obj and waits until another thread calls by using obj.notify() or obj.notifyAll().
   
   ​	wait() must be called inside a synchronized block because the thread must already own the object's monitor before it can release that monitor Calling wait() without owning the monitor causes IllegalMonitorStateException.

3. **Explain the difference between locking on this (an instance-level synchronized method) and locking on ClassName.class (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.**

   Answer:

   An instance synchronized method locks the current object,

   ```java
   public synchronized void methodA() {
   }
   // OR
   public void methodA() {
       synchronized (this) {
       }
   }
   ```
   
   A static synchronized method locks the Class
   
   ```java
   public static synchronized void methodB() {
   }
   // OR
   public static void methodB() {
       synchronized (ClassName.class) {
       }
   }
   ```
   
   Two different locks is that Instance method locks this, and static method ocks ClassName.class
   
   Therefore, a thread running an instance synchronized method and another thread running a static synchronized method do not block each other because they are using different monitor objects.
   
4. **What does volatile guarantee, and what does it NOT guarantee? Give a concrete code scenario where adding volatile alone fails to fix a concurrency bug.**

   Answer:

   ​	volatile mainly guarantees visibility. When one thread changes a volatile variable, other threads can see the latest value. volatile also prevents certain instruction reordering around volatile reads and writes.

   ​	However, volatile does not make compound operations atomic.

   For example:
   
   ```java
private volatile int count = 0;
   
public void increment() {
       count++;
   }
   ```

   This is still not thread-safe because count++ contains read, modify, and write steps. Two threads can still read the same old value and overwrite each other's updates. To fix this, we need synchorinized block

   ```java
public synchronized void increment() {
       count++;
}
   ```

5. **List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using tryLock with a timeout.**

   Answer:

   The four Coffman conditions are:

   1. Mutual Exclusion: a resource can only be held by one thread at a time.
   2. Hold and Wait: a thread holds one resource while waiting for another.
   3. No Preemption: a resource cannot be forcibly taken away from a thread.
   4. Circular Wait: threads form a cycle where each thread waits for a resource held by another.

   For strategy (a), enforcing a consistent lock order breaks the Circular Wait condition.

   For strategy (b), using tryLock() with a timeout allows a thread to stop waiting, release its current locks, and retry later.

6. **Explain what additional capabilities ReentrantLock provides over synchronized, and give a realistic use case for two of them.**

   Answer:

   ​	ReentrantLock provides more control than the synchronized keyword. For example, tryLock() to attempt to get a lock without waiting forever. tryLock(timeout, unit) to wait only for a limited amount of time.

   ​	Realistic case is that in a banking system, a money transfer may need to lock two accounts. Instead of waiting forever and risking deadlock, the program can use tryLock() and retry if it cannot get both account locks.

7. **Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like incrementAndGet() uses internally when the compare-and-set step fails.**

   Answer:

   ​	CAS works by comparing the current value with an expected value. If they are equal, it replaces the value with a new value. If they are not equal, the update fails.

   ​	A method like incrementAndGet() can use a retry loop like this:

   ```java
   while (true) {
       int current = get();
       int next = current + 1;
   
       if (compareAndSet(current, next)) {
           return next;
       }
   }
   ```
   
   If another thread changes the value before compareAndSet() succeeds, the CAS operation fails.
   
   The loop then reads the new current value, calculates the next value again, and retries until it succeeds.
   
8. **Explain how ConcurrentHashMap achieves higher concurrency than a HashMap wrapped with Collections.synchronizedMap(), referencing the idea of lock striping.**

   Answer:

   ​	A synchronized map can be created like this

   ```java
   Map<K, V> map = Collections.synchronizedMap(new HashMap<>());
   ```

   ​	This generally uses one lock around map operations. Because of this, only one thread can perform a synchronized operation at a time.

   ConcurrentHashMap is designed to allow more operations to happen at the same time.

9. **Describe the order of checks a ThreadPoolExecutor performs when a new task is submitted, with respect to corePoolSize, the task queue, and maximumPoolSize.**

   Answer:

   When a new task is submitted to a `ThreadPoolExecutor`, the executor generally checks in this order:

   1. If current worker count < corePoolSize, then create a new worker thread.
   2. Otherwise, try to put the task into the work queue.
   3. If the queue is full and worker count < maximumPoolSize then create another worker thread.
   4. If the queue is full and worker count has reached maximumPoolSize then reject the task using the configured rejection policy.
   
10. **Explain the difference between CountDownLatch and CyclicBarrier (reusability, and who is waiting for whom), and give one appropriate use case for each.**

    Answer:

    ​	CountDownLatch is usually used when one or more threads need to wait until a number of other operations finish. It is not reusable after the count reaches zero.

    ​	A good use case is waiting for several services or worker threads to finish initialization before starting the main application.

    ​	CyclicBarrier is used when a group of threads must all reach the same synchronization point before any of them continue. It is reusable, so the same barrier can be used again.

    ​	A good use case is a multi-phase parallel computation where all worker threads must finish phase 1 before any thread begins phase 2.

# java-multithreading-homework.md

## 1. Explain why count++ is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute count++ concurrently.
count++ consists of steps read, add and write, so it is not an atomic operation. When multiple threads execute count++ at the same time, they may read the same value before either thread writes the updated value back. For example, if count is 5, two threads may both read 5, add 1, and write 6. The final value will be 6 instead of 7, causing a lost update.


## 2. Explain the core difference between sleep() and wait() (whether the lock is released, and what wakes the thread up), and explain why wait() must be called inside a synchronized block.
sleep() pauses the current thread for a certain amount of time and does not release the lock. Thread wakes up when the sleep time is over. 
wait() releases the lock and waits until another thread calls notify() or notifyAll(). wait() must be called inside a synchronized block because the thread must own the lock before it can release and wait.


## 3. Explain the difference between locking on this (an instance-level synchronized method) and locking on ClassName.class (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.
An instance-level synchronized method locks on `this`, meaning it locks the current object. 
A static synchronized method locks on `ClassName.class`, which is the lock for the class. A thread calling an instance synchronized method and a thread calling a static synchronized method do not block each other because they are using two different locks.


## 4. What does volatile guarantee, and what does it NOT guarantee? Give a concrete code scenario where adding volatile alone fails to fix a concurrency bug.
volatile guarantees visibility, meaning when one thread changes a variable, other threads can see the updated value. It does not guarantee atomicity. For example, `volatile int count = 0` does not make count++ thread-safe because count++ still consists of read, add, and write. Multiple threads can read the same value and overwrite each other's updates.


## 5. List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using tryLock with a timeout.
The four conditions for deadlock:
- mutual exclusion
- hold and wait
- no preemption
- circular wait
(a) Enforcing a consistent lock-acquisition order breaks the circular wait condition because all threads acquire locks in the same order. 
(b) Using `tryLock()` with a timeout breaks the hold and wait condition because a thread can stop waiting and release the lock it already holds if it cannot get the next lock within timeout.


## 6. Explain what additional capabilities ReentrantLock provides over synchronized, and give a realistic use case for two of them.
`ReentrantLock` provides additional capabilities, such as `tryLock()`, timeout support, interruptible wait, fairness, and multiple conditions. For example, `tryLock()` with a timeout can be used when a thread should stop waiting if it cannot get a shared resource within a certain time. A fair lock can be used when many threads share the same resource and to reduce the chance that a thread waits for too long.


## 7. Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like incrementAndGet() uses internally when the compare-and-set step fails.
CAS compares the current value with expected value. If they are the same, it updates the value to a new value. If they are different, the update fails since the value may have be changed by another thread. `incrementAndGet()` uses a retry loop that it reads the current value, calculates the new value, and tries CAS. If CAS fails, it reads the latest value and tries again until the update succeeds.


## 8. Explain how `ConcurrentHashMap` achieves higher concurrency than a HashMap wrapped with `Collections.synchronizedMap()`, referencing the idea of lock striping.
`Collections.synchronizedMap()` uses synchronization that can make threads wait when accessing the map and limits concurrency. `ConcurrentHashMap` provides higher concurrency by using lock striping, where different parts of the map can be accessed independently instead of locking the entire map. Therefore, multiple threads can work on different parts of the map at the same time.


## 9. Describe the order of checks a `ThreadPoolExecutor` performs when a new task is submitted, with respect to `corePoolSize`, the task queue, and `maximumPoolSize`.
`ThreadPoolExecutor` first checks whether the number of worker threads is below `corePoolSize`. If it is, it creates a new worker thread. If the core pool is full, it would put the task into the task queue. If the queue is also full and the number of threads is below `maximumPoolSiz`e, it creates another worker thread. If the maximum pool size is also full, the task is rejected.


## 10. Explain the difference between `CountDownLatch` and `CyclicBarrier` (reusability, and who is waiting for whom), and give one appropriate use case for each.
`CountDownLatch` allows one or more threads to wait for other tasks to finish. Its count decreases when `countDown()` is called, and it cannot be reused after the count reaches zero. For example, a main thread can use it to wait for several initialization tasks to finish. 
`CyclicBarrier` allows a group of threads to wait for each other until all of them reach the same point. It can be reused for multiple rounds. For example, it can be used when multiple threads perform a calculation in phases and must all finish one phase before starting the next.
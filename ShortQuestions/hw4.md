# HW4 - Java Multithreading

## 1.
Explain why count++ is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute count++ concurrently.

count++ is not atomic because it involves three steps: reading the current value, adding 1, and writing the result back.

When multiple threads execute count++ concurrently, these steps can interleave. For example, if count is 5, two threads may both read 5, calculate 6, and write back 6. The final value is 6 instead of the expected 7. This is called a lost update.

## 2.
Explain the core difference between sleep() and wait() (whether the lock is released, and what wakes the thread up), and explain why wait() must be called inside a synchronized block.

sleep() pauses the current thread for a set amount of time. It does not release any locks the thread already holds. The thread wakes up when the time is over, or when it is interrupted.

wait() releases the lock of the object and waits for another thread to call notify() or notifyAll(). It can also wake up when a timeout is reached, when it is interrupted, or sometimes without a notification. It must get the same lock again before continuing.

wait() must be called inside a synchronized block or method that locks the same object, because the thread must own the lock before it can release it. Otherwise, Java throws IllegalMonitorStateException.


## 3.
Explain the difference between locking on this (an instance-level synchronized method) and locking on ClassName.class (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.

An instance synchronized method locks the current object (this). Threads calling synchronized methods on the same object must take turns. Different objects have different locks.

A static synchronized method locks the class object (ClassName.class). Threads calling static synchronized methods of the same class share this lock and must take turns.

An instance synchronized method and a static synchronized method use different locks, so they do not block each other just because they belong to the same class.


## 4.
What does volatile guarantee, and what does it NOT guarantee? Give a concrete scenario where adding volatile alone fails to fix a concurrency bug.

volatile guarantees visibility: when one thread changes the variable, other threads can see the change when they read it. It also provides ordering guarantees by preventing certain operations from being reordered.

However, volatile does not provide mutual exclusion or make compound operations like count++ atomic.

For example, even if count is volatile, two threads may both read 5, add 1, and write back 6. The final value is 6 instead of the expected 7. This is a lost update, so volatile alone does not fix the problem.


## 5.
List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using tryLock with a timeout.

The four necessary conditions for deadlock are:

1. Mutual exclusion: Only one thread can use a resource at a time.
2. Hold and wait: A thread holds one resource while waiting for another.
3. No preemption: A resource cannot be forcibly taken away from a thread.
4. Circular wait: Threads form a cycle, each waiting for a resource held by the next thread.

(a) Acquiring locks in the same order prevents circular wait.

(b) Using tryLock() with a timeout lets a thread stop waiting if it cannot get the next lock. It must then release the locks it already holds before retrying. This breaks the ongoing hold-and-wait situation. A timeout alone, without releasing held locks, is not enough.


## 6.
Explain what additional capabilities ReentrantLock provides over synchronized, and give a realistic use case for two of them.

ReentrantLock provides more control than synchronized. It supports:

- tryLock(): Try to get the lock without waiting.
- Timed tryLock(): Wait for the lock only for a set amount of time.
- lockInterruptibly(): Allow a thread to stop waiting when interrupted.
- An optional fair mode: Favor threads that have waited longer.
- Multiple Conditions: Keep different groups of waiting threads separate.

Two realistic use cases are:

1. During a bank transfer, a thread needs two account locks. If it cannot get the second lock within the time limit, it releases the first lock and retries later. This avoids waiting forever.

2. A task is waiting for a lock when the user clicks Cancel. With lockInterruptibly(), an interrupt lets the task stop waiting and handle the cancellation.

Unlike synchronized, ReentrantLock must be released manually with unlock(), usually in a finally block.

## 7.
Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like incrementAndGet() uses internally when the compare-and-set fails.

CAS (Compare-And-Swap) checks whether the current value matches an expected value. If they match, it replaces the current value with a new value. Otherwise, it makes no change and reports failure. The check and update happen as one atomic operation.

For example, two threads both read 5 and try to change it to 6. The first thread succeeds. The second thread fails because the current value is now 6, not 5.

In a CAS-based retry loop for an increment operation, the thread:

1. Reads the current value.
2. Calculates the current value + 1.
3. Uses CAS to try to write the new value.
4. If CAS fails, reads the value again, recalculates, and retries until successful.

After a successful update, incrementAndGet() returns the new value.

## 8.
Explain how ConcurrentHashMap achieves higher concurrency than a HashMap wrapped with Collections.synchronizedMap(), referencing the idea of lock striping.

Collections.synchronizedMap() uses one lock for the whole map. Threads must take turns, even when they access different keys.

ConcurrentHashMap allows threads to work on different parts of the map at the same time. Reads usually do not need a lock.

Lock striping means using different locks for different parts of the map. Older ConcurrentHashMap versions used segment locks. Java 8 and later use CAS and locks at the bucket level.

Because fewer threads need to wait for the same lock, ConcurrentHashMap supports higher concurrency.


## 9.
Describe the order of checks a ThreadPoolExecutor performs when a new task is submitted, with respect to corePoolSize, the task queue, and maximumPoolSize.

When a new task is submitted, ThreadPoolExecutor checks in this order:

1. If the number of threads is below corePoolSize, create a new thread to run the task.
2. Otherwise, try to put the task in the task queue.
3. If the queue cannot accept the task and the number of threads is below maximumPoolSize, create an extra thread to run it.
4. If the queue cannot accept the task and no more threads can be created, use the rejection policy.

The order is: core threads → task queue → extra threads → rejection policy.

## 10.
Explain the difference between CountDownLatch and CyclicBarrier (reusability, and who is waiting for whom), and give one appropriate use case for each.

CountDownLatch lets one or more threads wait until a count reaches zero. Other threads call countDown() to reduce the count, but they do not need to wait for each other. It cannot be reset or reused for another countdown.

Example: The main thread waits for three file downloads to finish before processing the files.

CyclicBarrier lets a group of threads wait for each other at a meeting point. Each thread calls await(), and they can continue only when all required threads have arrived. It can be reused for multiple rounds.

Example: Several threads finish their own part of a calculation, then wait for each other before starting the next round.
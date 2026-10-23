 1. Explain why count++ is not an atomic operation. Break it down into its individual steps, and describe what incorrect result can occur when multiple threads execute count++ concurrently.

 count++ has three steps, read, add, and write the new value back. So if two thread trying to add 1 in the same time, they may read both print 2, becuase they read the same value at the same time.

 2. Explain the core difference between sleep() and wait() (whether the lock is released, and what wakes the thread up), and explain why wait() must be called inside a synchronized block.

 Sleep does not release the lock, it only stop the thread for a fixed amount of time, wait release the lock, the tread waits until another thread call notify or notify all or until it is interrupted.


 3. Explain the difference between locking on this (an instance-level synchronized method) and locking on ClassName.class (a static synchronized method), and explain why a thread calling an instance method and a thread calling a static method on the same class do not block each other.

An instance level synchronized method lockes the specific object, a static synchronized method locks ClassName.class, os it locks the class object itself. So one thread can hold the instance lock while another holds the class lock, they do not block each other.



4. What does volatile guarantee, and what does it NOT guarantee? Give a concrete code scenario
where adding volatile alone fails to fix a concurrency bug.
Volatile guarantee visibility, if one thread changes a volatile variable, other threads can see the latest value. But volatile does not make compound operations atomic.
For example

volatile int count = 0;
count++; //count still read, add, and write
Two threads can still read the same value and overwrite each other.
So volatile does not fix this race condition.



5. List the four necessary conditions for deadlock (the Coffman conditions), and explain which condition each of these two prevention strategies breaks: (a) enforcing a consistent lock-acquisition order, (b) using tryLock with a timeout.

The four deadlock conditions are:
1. Mutual exclusion — only one thread can hold a lock at a time.
2. Hold and wait — a thread holds one lock while waiting for another.
3. No preemption — a lock cannot be forcibly taken away.
4. Circular wait — threads wait for each other in a cycle.
For prevention:
Consistent lock order breaks circular wait.
tryLock() with timeout helps break hold and wait, because a thread can give up and release its locks instead of waiting forever.




6. Explain what additional capabilities ReentrantLock provides over synchronized, and give a realistic use case for two of them.
Reetrantlock gives more control than synchronized, it supports trylock, timeout, so you can be more flexible to add the lock into the program.
Two realistic use cases:
1. tryLock() with timeout
   Useful when avoiding deadlock.
   If the lock is not available, the thread can give up and retry later.
2. Fair locking
   Useful when many threads compete for the same resource.
   It helps prevent one thread from waiting too long.


7. Explain how CAS (Compare-And-Swap) works, and describe the retry loop that a method like
incrementAndGet() uses internally when the compare-and-set step fails.

It works like this:
1. Read the current value.
2. Calculate the new value.
3. Compare the current value with the old value you read.
4. If they are still the same, update it.
5. If not, retry.
incrementAndGet() is like
while (true) {
    int oldValue = get();
    int newValue = oldValue + 1;

    if (compareAndSet(oldValue, newValue)) {
        return newValue;
    }
} If CAS fails, it keeps retrying until it succeeds.

8. Explain how ConcurrentHashMap achieves higher concurrency than a HashMap wrapped with
Collections.synchronizedMap(), referencing the idea of lock striping.

Collections.synchronizedMap() usually uses one lock for the whole map. So when one thread is writing, other threads may have to wait. ConcurrentHashMap allows more operations to happen at the same time. The idea is called lock striping. Instead of locking the whole map, different parts of the map can be locked separately.



9. Describe the order of checks a ThreadPoolExecutor performs when a new task is submitted, with
respect to corePoolSize, the task queue, and maximumPoolSize.
the order is:
corePoolSize → queue → maximumPoolSize → rejection.

If the number of threads is below corePoolSize, create a new thread.
Otherwise, put the task into the queue.
If the queue is full, create another thread.
Keep creating threads until maximumPoolSize is reached.
If the pool is at maximumPoolSize and the queue is full, reject the task.




10. Explain the difference between CountDownLatch and CyclicBarrier (reusability, and who is waiting for whom), and give one appropriate use case for each.
CountDownLatch is usually one-way and not reusable. One or more threads wait for other threads to finish some work.
Example:
Main thread waits
Worker 1 finishes → countDown()
Worker 2 finishes → countDown()
Worker 3 finishes → countDown()
count reaches 0
Main thread continues

CyclicBarrier is reusable.
A group of threads wait for each other.
Example:

Thread A reaches barrier
Thread B reaches barrier
Thread C reaches barrier

All arrived
→ all continue
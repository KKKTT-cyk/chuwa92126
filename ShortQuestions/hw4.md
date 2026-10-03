1. count++ is not atomic because it involves three steps:Read the current value of count. Add 1 to that value. Write the result back to count. Two threads can both read the same value before either writes. If count starts at 0, both may read 0 and then each write 1. The final value is 1 instead of 2. This is called a lost update.
2. sleep() pauses a thread without releasing its lock; it wakes when the time expires or it is interrupted. wait() releases the object’s lock; it wakes after notify()/notifyAll(), a timeout, or an interruption.
3. An instance synchronized method locks this, the particular object it is called on. A static synchronized method locks ClassName.class, the class object shared by its instances.
   A thread calling each method does not block the other because they acquire different locks, even though the methods belong to the same class
4. volatile guarantees that threads see the latest write to a variable and provides ordering between reads and writes of that variable. It does not make compound operations atomic.
5.The four Coffman conditions are mutual exclusion, hold and wait, no preemption, and circular wait.

(a) Consistent lock order breaks circular wait: every thread acquires locks in the same order, so they cannot form a cycle.
(b) tryLock with a timeout prevents a permanent hold and wait if the thread releases any locks it already holds when acquisition fails, then retries. A timeout alone is not enough; releasing the held locks is the key step.
6. ReentrantLock offers features beyond synchronized, including timed tryLock(), interruptible lock acquisition, optional fairness, and multiple Condition objects.
- Timed tryLock(): A bank transfer can give up and retry if it cannot acquire both account locks promptly.
- lockInterruptibly(): A worker waiting for a lock can stop when its task is cancelled, instead of continuing to wait.
7.CAS atomically checks whether a value still equals an expected value. If it does, CAS replaces it with a new value; otherwise, it fails without changing anything.
8.Collections.synchronizedMap() uses one lock for the whole map, so even operations on unrelated keys must wait for each other.
  ConcurrentHashMap uses finer-grained coordination, similar to lock striping: updates to different buckets can often proceed at the same time, and reads generally do not need to take a lock. This allows more threads to use the map concurrently.
9.When a task is submitted, ThreadPoolExecutor checks in this order:If fewer than corePoolSize threads are running, create a thread for the task. Otherwise, try to put the task in the queue. If the queue is full and fewer than maximumPoolSize threads are running, create another thread. If the queue is full and the pool is at maximumPoolSize, reject the task. With an unbounded queue, the pool normally never reaches step 
10.CountDownLatch is one-time use: one thread can wait until other threads finish a set number of tasks. For example, the main thread waits for three files to finish loading. CyclicBarrier is reusable: a group of threads waits for one another to reach the same point before all continue. For example, simulation workers synchronize after each round
11

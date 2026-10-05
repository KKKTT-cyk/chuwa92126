Question 1:

count++ can be divided into 3 steps: read, add 1, write. When multiple threads execute the count increment operation, if 2 thread simultaneously read count (for example when count is 5), then both adding 1 to count (making count = 6), and finally write it back (both write back count = 6), then 1 increment is actually lost in the process. So multiple threads executing count++ concurrently may incur lost increment.

Question 2:

sleep() belongs to Thread class which pauses current thread and doesn not release any locks it holds. It is the time eclapse that wakes the thread up. The main purpose of sleep() is to pause execution. While wait() belongs to Object class which releases the monitor lock of the object on which wait() is called, waiting and waits wake-up events such as notify(), notifyAll(). The main purpose of wait() is thread coordination. wait() must be called inside synchronized because wait() operates on an object's monitor and the calling thread must own that object's monitor before calling it. Calling wait() outside synchronized will incur "IllegalMonitorStateException" because the current thread doesn't own lock's monitor.

Question 3:

An instance synchronized method acquires the monitor lock of the current object, this. Different instances have different locks, so synchronized instance methods on different objects can excecute concurrently.
A static synchronized method acquires the monitor lock of the class object, ClassName.class, which is shared across instances of that class within the same class loader.
A thread calling an instance synchronized method and another thread calling a static synchronized method do not block each other because they acquire different monitor locks. Synchronization provides mutual exclusion only when threads compete for the same lock.

Question 4:

Volatile guarantees: 1. Visibility: when one thread modifies a volatile variable, other threads reading that variable can see the updated value. 2. Ordering: volatile also prevents certain compiler and CPU instruction reordering around volatile reads and writes. In Java, a write to a volatile variable happens before a subsequent read of that same variable.
Volatile does NOT guarantee atomicity for compound operations. Such as the race condition in question 1, even adding volatile keyword before count, count++ is not an atomic operation because it consists of three logical steps of read, add 1 and write the new value back. volatile doesn't prevent two threads from reading the same old value before either writes the the one back.

Question 5:

Four necessary conditions for deadlock:

- Multual exclusion: only one thread can hold a particular lock at a time.
- Hold and wait: a thread holds one lock while waiting to acquire another.
- No preemption: a lock cannot be forcibly taken away from a thread.
- Circular wait: two or more threads form a cycle each waiting for a lock held by another thread.

Consistent lock ordering breaks the circular wait condition because every thread acquires locks in the same order, preventing a curcular dependency.
Using tryLock() with a timeout prevents the hold and wait condition because it prevents threads from waiting indefinitely for locks. If a thread cannot acquire the next lock within the timeout, it can release its existing locks and retry later.

Question 6:

ReentrantLock provides several capabilities beyond synchronized:

- tryLock() for non-blocking lock acquisition
- timed lock attempts
- interruptible lock acquisition
- configurable fairness
- multiple condition variables

Two exaples to use ReentrantLock:

1. In a banking application, I can use tryLock() with a timeout when acquiring multiple account locks. If a lock is unavailable, the thread can release the locks it already holds and retry later, avoiding indefinite waiting.
2. I can use separate Condition objects for notFull and notEmpty in a bounded producer-consumer queue, allowing producers and consumers to wait for and signal their respective conditions.

Unlike synchronized, ReentrantLock requires explicit lock release, so I need to always call unlock() in a finally block.

Question 7:

CAS (Compare-And-Swap) is a hardware-supported atomic operation used by classes such as AtomicInteger to update a value without using synchronized. Its core idea is to update the value only if it still equals the value previously observed. If another thread changed the original value, the compare-and-set fails, so the thread rereads the latest value, recomputes the result and retries until the update succeeds. This is the basic pattern used internally by methods like AtomicInteger.incrementAndGet().

Question 8:

`ConcurrentHashMap` acieves higher concurrency because it does not use one signle glocal lock for the entire map but using a finer-grained locking strategy of lock striping, where different internal segments or buckets can be locked independently. So threads accessing different buckets or segments can often proceed concurrently. By contrast, `Collections.synchronizedMap()` uses one coarse-grained lock for the whole map, so only one thread can perform a synchronized map operation at a time.

Question 9:

`ThreadPoolExecutor` first tries to use a core thread. If all core threads are busy, it queues the task. If the queue is full, it creates additional threads up to `maximumPoolSize`. If both the queue and the maximum pool are full, it applies the rejection policy.

Question 10:

`CountDownLatch` is a one-time synchronization aid where one or more threads wait for a set of tasks to complete. `CyclicBarrier` is reusable and is used when a fixed group of threads must all reach the same synchronization point before any of them continue.

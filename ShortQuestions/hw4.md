## Question1
count++ looks like one operation in Java, but internally it involves multiple steps:
1.Read the current value of count.
2.Add 1 to that value.
3.Write the new value back to count.

For example, suppose:
int count = 10;
Two threads, Thread A and Thread B, execute:
count++;
The operations could happen like this:
Thread A reads count = 10
Thread B reads count = 10

Thread A calculates 10 + 1 = 11
Thread B calculates 10 + 1 = 11

Thread A writes 11
Thread B writes 11

The expected result is 12, because two increments occurred. However, the actual result is 11.
This is called a race condition or lost update.

## Question2
1.Thread.sleep() pauses the current thread for a specified amount of time.
If the thread currently owns a synchronized lock, sleep() does not release that lock.
A sleeping thread normally wakes up when:
The specified sleep time expires.
Another thread interrupts it.

wait() causes the current thread to wait and release the monitor lock.
The waiting thread can become eligible to continue when:
lock.notify(); or: lock.notifyAll();

It can also wake because:
A timeout expires when using wait(timeout).
The thread is interrupted.
A spurious wakeup occurs.

2.A thread must own an object's monitor before calling wait(), notify(), or notifyAll() on that object.

## Question3
1.An instance synchronized method locks the specific object instance.
A static synchronized method instead locks the class's Class object.
2.Because these are two different lock objects, the threads do not block each other.

## Question4
volatile primarily guarantees visibility of changes between threads.
volatile also provides certain memory-ordering guarantees, meaning reads and writes around a volatile variable cannot be freely reordered in ways that violate Java's happens-before rules.
However, volatile does not guarantee atomicity for compound operations.

private volatile int count = 0;

public void increment() {
count++;
}

## Question5
1.Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait
2.1The circular dependency cannot form.
Therefore, consistent lock ordering breaks the:
Circular Wait condition.
2.2 This approach prevents a thread from indefinitely holding one lock while waiting for another. When implemented by releasing previously acquired locks after a timeout, it breaks the Hold and Wait condition.

## Question6
1.tryLock()
A thread can attempt to acquire a lock without waiting forever. It can also use a timeout.
Realistic use case: avoiding deadlock when a transaction needs multiple locks.
2. Interruptible locking
   With: lock.lockInterruptibly();
a thread waiting for the lock can be interrupted.
This is useful for long-running server operations that need cancellation.
3.Fairness
A lock can optionally be created as a fair lock:
This tries to give the lock to threads approximately in the order they requested it.
Realistic use case: a service where many worker threads compete for the same resource and starvation should be reduced.
4. Multiple Condition objects
ReentrantLock supports multiple condition queues.
5. Lock information

## Question7
CAS (Compare-And-Swap) is a hardware-supported atomic operation used by classes such as AtomicInteger to update a value without using synchronized. Its core idea is to update the value only if it still equals the value previously observed. If another thread changed the original value, the compare-and-set fails, so the thread rereads the latest value, recomputes the result and retries until the update succeeds. This is the basic pattern used internally by methods like AtomicInteger.incrementAndGet().

## Question8
A synchronized map can be created using:
Map<String, Integer> map =
Collections.synchronizedMap(new HashMap<>());
Conceptually, operations are protected by a common map-level lock.
If Thread A is modifying one part of the map, another thread may have to wait even if it wants to work with a completely different key.

Historically, Java's ConcurrentHashMap used an approach commonly described as lock striping, where different portions of the map could be protected independently.
Modern ConcurrentHashMap implementations no longer use the old fixed Segment architecture in exactly the same way, but the important principle remains: they avoid using one single coarse-grained lock for every operation and use finer-grained synchronization and atomic operations.
This allows much greater concurrency.
Another important feature is that reads generally do not require locking the entire map.
Therefore, ConcurrentHashMap usually scales much better when many threads access a map simultaneously.

## Question9
`ThreadPoolExecutor` first tries to use a core thread. If all core threads are busy, it queues the task. If the queue is full, it creates additional threads up to `maximumPoolSize`. If both the queue and the maximum pool are full, it applies the rejection policy.

## Question10
CountDownLatch causes main thread to wait until all worker threads finish. A CountDownLatch is one-time use. Once its count reaches zero, it cannot be reset.
application use: an application must load three resources before starting.

A CyclicBarrier causes a group of threads to wait for each other.a CyclicBarrier can be reused for multiple rounds.
application use: Workers synchronize at phases
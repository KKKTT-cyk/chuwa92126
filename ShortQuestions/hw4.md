1. 
Reason: count++ is not an atomic operation because:
race condition
count++ could actually break into 3 steps:
first READ count from memory, for example count =5;
then ADD 1 to the read value, here is 5+1 =6
the last step is to WRITE the new value back which is count =6.

When multiple threads access count++ concurrently, every thread may access the count++ operation at the same time and 
add 1 to the count, like thread A and thread B both read the count=5 from memory and add 1 to the read value to 6 , 
at the end lost one update which means we expect count =7 while get count=6 instead.

2. 
The core difference of sleep() and wait() when lock is released:
sleep() will not release lock, and it will wake up other threads after time elapse while wait() will release lock, and 
it will wake up other threads when notify() or notifyALL() is called or timeout.

The method wait() should be called inside a synchronized block because synchronized make current thread hold the lock 
and when wait() is called, it released the lock and make the current thread wait, when notify() or notifyALL() is called 
would wake up the thread, and only it gets the lock could continue work.

3. 
Difference between locking on this and ClassName.class:
lock on this should add synchronized on the method name, eg: public synchronized void instanceMethod(){}
lock on ClassName.class should add synchronized and static on the method name: 
eg: public synchronized static void staticMethod(){}

A thread calls an instance method hold an object lock while a thread calls a static method hold a class lock, they hold
different locks thus they will not block each other.

4. 
volatile guarantee the visibility which means the change made by a thread can be seen by other threads and also 
guarantee the ordering, which has a happens before relationship between write to a volatile variable and a subsequent
read of that variable.

But volatile does not guarantee atomicity
Here is an example code:
private volatile int count = 0;
public void increment(){
    count++;
}

In the upper code, the count++ operation is not atomic, it could break into 3 steps:
read the count from memory,
add 1 
write the update value back.

If two threads access the increment() concurrently, both of the two threads may read the same count value,like count=0,
then add 1 and write the updated value 1 back, the result may be 1 instead of 2. Therefore, the race condition still 
exists with volatile.

5. 
4 necessary conditions for deadlock:
mutual exclusive: only one thread can hold a lock at a time
hold and wait: a thread holds a lock while waiting for another
no preemption: a lock can't be forcibly taken away from a thread
circular wait: a circle of threads exists, each waiting for next's lock

a) enforcing a consistent lock-acquisition order: could break circular wait;
b) using tryLock with a timeout could avoid blocking forever and also break circular wait.

6. 
reentrantLock provides explicit lock() and unlock() to acquire or release locks, also provides tryLock() of try 
without blocking, tryLock(timeout,unit) to support timeout, provides timeout support with lockInterruptibly(), provide
fairness policy with new ReentrantLock(true), and use newCondition() to create multiple condition objects, thus the 
code of reentrantLock is more verbose.

two realistic cases:
1) tryLock with timeout:
scenario: money transaction need to acquire two bank account's locks
A transfer my need to lock two accounts. If the second thread can't acquire within the time limit, it will release 
the first lock and retry instead of waiting forever.
2) multiple condition
scenario: producer-consumer queue with capacity limit
ReentrantLock can create different lock objects like notEmpty and notFull. Consumers wait on notEmpty when the queue
is empty, and producers wait on notFull when the queue is full. This allows more precise notification than a single 
monitor wait set by using synchronized.


7. 
How the CAS works:
CAS is an atomic operation that compare the current value with the expected value. If the current value equals to the 
expected value, then replace the current value with a new one and true. Otherwise, another thread might modify that 
value, then CAS makes no change and return false.

A method like incrementAndGet():
public int incrementAndGet() {
    int current;
    int next;
    do {
        current = get();
        next = current + 1;
    } while (!compareAndSet(current, next));
    return next;
}
If the CAS fails, then it reads the latest value again, and recalculate the new value, and try another CAS attempt.
The loop continues until the update succeed.

8.
The concurrentHashMap does not use one lock for a whole map, it uses lock striping, which means dividing the internal
storage into segments and each independently lockable, so different thread can operate on different segments 
simultaneously. While HashMap wrapped with Collections.synchronizedMap() provide a shared lock to protect the map, only
one thread can preform a synchronized map operation at one time.

9. 
When a task is submitted, ThreadPoolExecutor performs as the following order:
If the current worker count is less than corePoolSize, it creates a new core thread to execute the task.
If the current worker count is greater than corePoolSize, then try to put the worker into the work queue.
If the queue is full, it checks whether the worker count is less than the maximumPoolSize, if so, it creates an
additional non-core thread to execute the task.
If the queue is full and the worker count has already reached the maximumPoolSize, the executor applies its rejection 
policy.

10. 
The difference between CountDownLatch and CyclicBarrier:
CountDownLatch is a one-time gate that opens once a counter reach zero, which cannot be reset. It is initialized with
a count and one or more threads call await(). Other threads call countDown() when finishing their work. When the count
reaches to 0, other threads are released. The worker threads don't need to wait for each other.

A suitable use case for CountDownLatch is waiting for multiple parallel initialization tasks, for example loading the
database, connecting to the service, to finish before starting the application.

CyclicBarrier is reusable and automatically resets after all participating threads reach the barrier. Each thread calls
await(), so each thread wait for another before continuing to the next task. Besides, it can execute an optional barrier
when the barrier is reached.

A suitable use case for CyclicBarrier is parallel simulations, which is multiphase parallel computations where all
threads need to finish phase 1 before any one can start phase 2.
















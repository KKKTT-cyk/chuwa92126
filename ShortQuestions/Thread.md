# Q1.
`count++` is actually `count = count + 1`. At machine level, this is a read-modify-write sequence made up for three separate steps, and another thread can run in between any of them.
Read: load the current value of `count` from memory into CPU register.  
Modify: add 1 to the value in register.  
Write: store the new value back to memory.  
So when there are multiple threads read the same old value before either one wrote its result, each one computed and wrote, 
so the second write overwrote the first, and one increment has been silently lost.

# Q2.
They both pause a thread, but they exist for different purposes. `sleep` is about timing, pause for a while. `wait` is about coordination between threads. pause until some condition becomes true and another thread tells me so.
When a thead calls `sleep` it keeps holding the lock the entire time, this is why sleeping inside a synchronized block is usually a bad ida, it stalls everyone else.  
When a thread calls `wait` it releases the lock on that object and goes into the objects wait set, this allows other threads to enter the synchronized block, change the shared state, and eventually notify the waiting thread.  
A sleeping thread wakes up when the specified time has passed, or earlier if another thread calls interrupt on it, which causes InterruptedException to be thrown.  
A waiting thread wakes up when another thread calls notify or notifyAll on the same object. It can also wake up if a timeout was given, as in wait(1000), if it is interrupted, or because of a spurious wakeup, which the JVM is allowed to produce with no reason at all.

# Q3.
Every object has its own build in lock, called a monitor. A synchronized method or block always locks one specific object.  
Each instance has its own monitor, the lock is pre object.  
A static method doesn't belong to any instance, so there is no this to lock. Instead, a static synchronized method locks the class object that represents the class.
Instance and Static are two different objects, and therefore they have two different monitors, holding one says nothing about the other.

# Q4. 
Volatile guarantee is visibility, without volatile, a thread may keep a variable's value in a CPU register or cache, and compiler may even hoist the read out of a loop entirely. As a result one thread's write may never be seen by another thread. With volatile every write is immediately made visible to other threads.
The second guarantee is ordering. The compiler and CPU are allowed to reorder instructions for performance, a write to a volatile variable acts as a barrier, everything a thread did before writing the volatile variable is visible to another thread after it reads that same variable.
The third, smaller guarantee is that reads and writes of volatile long and volatile double are atomic.  
What volatile does not guarantee, it doesnt make compound operation atomic, and it provides no mutual exclusion
```java
public class Counter {
    private volatile int count = 0;

    public void increment() {
        // still NOT atomic
        count++;  
    }

    public int get() {
        return count;
    }
}

Counter c = new Counter();
Runnable task = () -> {
    for (int i = 0; i < 100_000; i++) c.increment();
};
Thread t1 = new Thread(task), t2 = new Thread(task);
t1.start();
t2.start();
t1.join();  
t2.join();
System.out.println(c.get()); 

// to fix this we can use AtomicInteger to make whole operation atomic, either with a lock or with an atomic clas.
private final AtomicInteger count = new AtomicInteger();

public void increment() {
    count.incrementAndGet();   
}

```
# Q5.

1. Mutual exclusion: at least one resource can only be held by one thread at a time.
2. Hold and Wait: a thread holds at one resource while waiting to acquire another.
3. No preemption: a resource cannot be forcibly taken away fron the thread holding it.
4. Circular wait: there is a cycle of threads each waiting for a resource held by the next on in the cycle.

Consistent lock acquisition order breaks circular wait, if every thread always acquires locks in same global order, a cycle becomes impossible.
TryLock with a timeout breaks no preemption, it attempts to acquire a lock but gives up if it cannot do so within the time limit, so the resources arent held indefinitely.

# Q6.
Both provide mutual exclusion, both have the same memory visibility guarantees and both are reentrant, meaning a thread that already holds the lock can acquire it again without deadlocking itself. The difference is that synchronized is a language keyword with fixed, simple behavior,while ReentrantLock is a class with an API, so it exposes much more control.
```java
public Response updateProfile(Account account, ProfileUpdate update)
        throws InterruptedException {
    ReentrantLock lock = account.getLock();

    if (!lock.tryLock(2, TimeUnit.SECONDS)) {
        return Response.status(409)
                .entity("This account is busy. Please try again shortly.")
                .build();
    }
    try {
        account.applyUpdate(update);
        return Response.ok().build();
    } finally {
        lock.unlock();
    }
}
```

# Q7.
A CAS operation takes three inputs, A memory location, an expected value, a new value.
It reads the current value then compute the new value from it, and attempts to publish the new value with CAS.

# Q8.
`Colloctions.synchronizedMap` puts one lock around the entire map, so only one thread can use it at a time.  
`ConcurrentHashMap` divides the map into many independently locked pieces, and lets most reads happen without any lock at all. the technique of splitting one lock into many is called lock stripping. 

# Q9.
There are four steps, if fewer that `corepoolsize` threads are running, start a new thread. otherwise try to add the task to the work queue. if the queue is full, try to start a new non-core thread, if the pool already has `maximumpoolsize` threads and the queue is full, reject the task by passing it to the pools RejectedExecutionHandler.

# 10.
`CountDownLatch` and `CyclicBarrier` are both synchronization aids to make threads wait until some number of things have happened.
A `CountDownLatch` is created with a count, Threads that call await block until the count reaches zero. Other threads decrease the count by calling countDown, which never blocks, a thread counts down and immediately continues with whatever it was doing.
```java
public static void main(String[] args) throws InterruptedException {
    List<Service> services = List.of(
            new DatabasePool(), new CacheService(), new MessageQueueClient());

    CountDownLatch ready = new CountDownLatch(services.size());
    ExecutorService pool = Executors.newFixedThreadPool(services.size());

    for (Service s : services) {
        pool.submit(() -> {
            try {
                s.initialize();
            } finally {
                ready.countDown();   
            }
        });
    }
    if (!ready.await(30, TimeUnit.SECONDS)) {
        throw new IllegalStateException("Services failed to start in time");
    }
     
    pool.shutdown();
}
```
A `CyclicBarrier` is created with a number of parties, meaning the number of threads that must meet, each thread calls await when it reaches the barrier, and blocks until all parties have arrived, then all of them are released together.
```java
public static void main(String[] args) {

    CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        System.out.println("=== phase 1 done, start phase 2 ===");
    });

    for (int i = 1; i <= 3; i++) {
        int workerId = i;

        new Thread(() -> {
            try {
                System.out.println("Worker " + workerId + " start phase 1");
                Thread.sleep(workerId * 1000L);
                System.out.println("Worker " + workerId + " done，waiting for others");
                barrier.await();
                System.out.println("Worker " + workerId + " start phase 2");
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
```
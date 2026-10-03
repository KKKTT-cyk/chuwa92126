public class SafeCounter {
    private int count = 0;

    // Only one thread can execute a synchronized method on this instance at a time,
    // so the read-add-write steps of count++ cannot interleave
    public synchronized void increment() {
        count++;
    }

    // synchronized also guarantees visibility: readers always see the latest value
    public synchronized int getCount() {
        return count;
    }
}

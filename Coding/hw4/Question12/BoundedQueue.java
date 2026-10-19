import java.util.LinkedList;
import java.util.Queue;

public class BoundedQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BoundedQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // Releases the lock and waits - queue is full
        }
        queue.add(item);
        notifyAll(); // Wake up any waiting consumers
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // Releases the lock and waits - queue is empty
        }
        T item = queue.poll();
        notifyAll(); // Wake up any waiting producers
        return item;
    }
}

import java.util.LinkedList;
import java.util.Queue;

public class BoundedQueue<T> {

    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    public BoundedQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        T item = queue.remove();
        notifyAll();

        return item;
    }

    public static void main(String[] args) throws InterruptedException {
    BoundedQueue<Integer> queue = new BoundedQueue<>(2);

    queue.put(1);
    queue.put(2);

    System.out.println(queue.take());
    System.out.println(queue.take());
    }
}


import java.util.LinkedList;
import java.util.Queue;

public class BoundedQueue<T> {

    private final Queue<T> queue;
    private final int capacity;

    public BoundedQueue(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than zero"
            );
        }

        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void put(T item)
            throws InterruptedException {

        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);

        notifyAll();
    }

    public synchronized T take()
            throws InterruptedException {

        while (queue.isEmpty()) {
            wait();
        }

        T item = queue.remove();

        notifyAll();

        return item;
    }

    public synchronized int size() {
        return queue.size();
    }

    public static void main(String[] args) {

        BoundedQueue<Integer> queue =
                new BoundedQueue<>(5);

        Thread producer = new Thread(() -> {

            try {

                for (int i = 1; i <= 10; i++) {

                    queue.put(i);

                    System.out.println(
                            "Produced: " + i
                    );
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {

            try {

                for (int i = 1; i <= 10; i++) {

                    int value = queue.take();

                    System.out.println(
                            "Consumed: " + value
                    );
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
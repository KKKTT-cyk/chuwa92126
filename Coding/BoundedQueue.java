package Coding;

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
            wait();
        }
        queue.offer(item);
        System.out.println("Put: " + item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        System.out.println("Take: " + item);
        notifyAll();
        return item;
    }

    public static void main(String[] args) {

        BoundedQueue<Integer> queue = new BoundedQueue<>(2);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    queue.put(i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(1000);
                    queue.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
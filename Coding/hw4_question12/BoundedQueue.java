package chuwa92126.Coding.hw4_question12;

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

        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        T item = queue.poll();

        notifyAll();

        return item;
    }

    public static void main(String[] args) {
        BoundedQueue<Integer> queue = new BoundedQueue<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    int item = queue.take();
                    System.out.println("Consumed: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
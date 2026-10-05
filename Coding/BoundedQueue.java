package com.lant.hw4;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedQueue<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;
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
        BoundedQueue<Integer> queue = new BoundedQueue<>(3);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.put(i);
                    System.out.println("put: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(100);
                    int item = queue.take();
                    System.out.println("take: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println("done");
    }
}
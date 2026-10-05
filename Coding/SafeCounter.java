package com.lant.hw4;

import java.util.ArrayList;
import java.util.List;

public class SafeCounter {

    private final Object lock = new Object();
    private long count = 0;
    public synchronized long getCount() {
        return count;
    }
    public synchronized void increment() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10;
        int eachIncrease = 1000000;
        SafeCounter safeCounter = new SafeCounter();
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(()->{
                for (int j = 0; j < eachIncrease; j++) {
                    safeCounter.increment();
                }
            },"thread-"+i);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println(safeCounter.getCount() == eachIncrease * threadCount);
    }
}

package org.tiff.homework3.question11;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SafeCounter counter = new SafeCounter();
        Thread[] threads = new Thread[4];
        int incrementsPerThread = 10000;

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Expected count: " + threads.length * incrementsPerThread);
        System.out.println("Actual count: " + counter.getCount());
    }
}

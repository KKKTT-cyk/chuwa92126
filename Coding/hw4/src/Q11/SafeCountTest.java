package Q11;

import java.util.ArrayList;
import java.util.List;

public class SafeCountTest {
    public static void main(String[] args) throws InterruptedException {
        SafeCounter safeCounter = new SafeCounter();
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                safeCounter.increment();
            }
        };
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(safeCounter.getCount());


    }
}

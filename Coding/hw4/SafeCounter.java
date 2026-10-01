import java.util.ArrayList;
import java.util.List;

public class SafeCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SafeCounter counter = new SafeCounter();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(counter::increment);
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final count: " + counter.getCount());
    }
}
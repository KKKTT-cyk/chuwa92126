package Q12;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BoundedQueue(int capacity) {
        if (capacity<0){
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
    }

    public synchronized void put(T t) throws InterruptedException{
        while (queue.size()>=capacity){
            wait();
        }
        queue.add(t);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException{
        while (queue.isEmpty()){
            wait();
        }
        T t = queue.poll();
        notifyAll();

        return t;
    }
}

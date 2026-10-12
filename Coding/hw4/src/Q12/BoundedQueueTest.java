package Q12;

public class BoundedQueueTest {
    public static void main(String[] args) throws InterruptedException {
        BoundedQueue<Integer> queue= new BoundedQueue<>(2);
        Thread producer = new Thread(() ->{
            try {
                for (int i = 1; i < 5; i++) {
                    queue.put(i);
                    System.out.println("Produced: "+i);
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() ->{
            try {
                for (int i = 1; i < 5; i++) {
                    Integer t = queue.take();
                    System.out.println("Consumed: "+i);
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Test completed");

    }
}

package Q11;

public class EmailNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println("Send "+message+" by Email");
    }
}

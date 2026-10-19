package Q11;

public class SmsNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println("Send "+message+" by SMS");
    }
}

public class Main {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("email");
        email.send("Hello by email");

        Notification sms = NotificationFactory.create("sms");
        sms.send("Hello by SMS");
    }
}

public class Main {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("email");
        email.send("Welcome to our service!");

        Notification sms = NotificationFactory.create("sms");
        sms.send("Your verification code is 123456.");
    }
}

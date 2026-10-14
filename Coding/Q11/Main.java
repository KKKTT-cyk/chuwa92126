public class Main {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("email");
        email.send("Hello! Email!");

        Notification sms = NotificationFactory.create("sms");
        sms.send("Hi! SMS!");
    }
}
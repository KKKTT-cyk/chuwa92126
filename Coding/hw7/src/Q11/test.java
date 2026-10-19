package Q11;

public class test {
    public static void main(String[] args) {
        Notification n1= NotificationFactory.create("email");
        Notification n2 = NotificationFactory.create("sms");
        n1.send("hello");
        n2.send("hi");

    }
}

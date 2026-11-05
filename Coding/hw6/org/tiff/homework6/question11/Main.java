package org.tiff.homework6.question11;

public class Main {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("email");
        email.send("Hello");

        Notification sms = NotificationFactory.create("sms");
        sms.send("Hello");
    }
}

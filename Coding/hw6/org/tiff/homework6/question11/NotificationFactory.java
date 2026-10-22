package org.tiff.homework6.question11;

public class NotificationFactory {
    public static Notification create(String type) {
        if ("email".equals(type)) {
            return new EmailNotification();
        } else if ("sms".equals(type)) {
            return new SmsNotification();
        }
        throw new IllegalArgumentException("Unknown notification type: " + type);
    }
}

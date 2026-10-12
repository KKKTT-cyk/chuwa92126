public class NotificationFactory {
    public static Notification create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }

        if (type.equalsIgnoreCase("email")) {
            return new EmailNotification();
        } else if (type.equalsIgnoreCase("sms")) {
            return new SmsNotification();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported notification type: " + type
            );
        }
    }
}

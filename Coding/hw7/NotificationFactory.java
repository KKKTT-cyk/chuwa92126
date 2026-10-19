public final class NotificationFactory {
    private NotificationFactory() {
    }

    public static Notification create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Notification type must not be null.");
        }

        switch (type) {
            case "email":
                return new EmailNotification();
            case "sms":
                return new SmsNotification();
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
    }
}


import java.util.Locale;

// Step 1: Define the Notification interface
interface Notification {
    void send(String message);
}

// Step 2: Implement EmailNotification
class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}

// Step 3: Implement SmsNotification
class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

// Step 4: Create the NotificationFactory
class NotificationFactory {

    private NotificationFactory() {
        // Prevent instantiation
    }

    public static Notification create(String type) {
        if (type == null) {
            throw new IllegalArgumentException(
                    "Notification type cannot be null"
            );
        }

        switch (type.trim().toLowerCase(Locale.ROOT)) {
            case "email":
                return new EmailNotification();

            case "sms":
                return new SmsNotification();

            default:
                throw new IllegalArgumentException(
                        "Unsupported type: " + type
                );
        }
    }
}

// Step 5: Test the notification system
public class NotificationDemo {
    public static void main(String[] args) {

        Notification email =
                NotificationFactory.create("email");

        Notification sms =
                NotificationFactory.create("sms");

        email.send("Your order has shipped!");

        sms.send("Your verification code is 123456.");
    }
}

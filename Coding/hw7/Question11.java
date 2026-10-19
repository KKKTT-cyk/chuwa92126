import java.util.Locale;

interface Notification {
  void send(String message);
}

class EmailNotification implements Notification {
  @Override
  public void send(String message) {
    System.out.println("Sending email: " + message);
  }
}

class SmsNotification implements Notification {
  @Override
  public void send(String message) {
    System.out.println("Sending SMS: " + message);
  }
}

class NotificationFactory {
  public static Notification create(String type) {
    if (type == null) {
      throw new IllegalArgumentException("Notification type must not be null");
    }
    switch (type.toLowerCase()) {
      case "email":
        return new EmailNotification();
      case "sms":
        return new SmsNotification();
      default:
        throw new IllegalArgumentException("Unknown notification type " + type);
    }
  }
}


public class Question11 {
  public static void main(String args[]) {
    Notification notice = NotificationFactory.create("email");
    notice.send("Your order has shipped");

    Notification sms = NotificationFactory.create("sms");
    sms.send("Your request has been accepted");
  }
}

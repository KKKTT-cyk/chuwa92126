package Q11;

public class NotificationFactory {
    public static Notification create(String type){
        switch (type){
            case "email" : return new EmailNotification();
            case "sms" : return new SmsNotification();
            default: throw new IllegalArgumentException("Unknowing type: "+type);
        }
    }
}


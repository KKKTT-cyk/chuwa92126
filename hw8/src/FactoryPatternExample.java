// Q11. Factory Method pattern for a notification system
interface Notification { 
    void send(String message); 
}


class EmailNotification implements Notification 
{ 
    @Override 
    public void send(String message) { 
        System.out.println("Sending Email Notification: " + message); 
    } 
}


class SMSNotification implements Notification 
{ 
    @Override 
    public void send(String message) { 
        System.out.println("Sending SMS Notification: " + message); 
    } 
}


class NotificationFactory 
{ 
    private NotificationFactory() {} 

    public static Notification createNotification(String type) { 
        if (type.equalsIgnoreCase("EMAIL")) { 
            return new EmailNotification(); 
        } else if (type.equalsIgnoreCase("SMS")) { 
            return new SMSNotification(); 
        } 
        throw new IllegalArgumentException("Unknown notification type: " + type); 
    } 
}


public class FactoryPatternExample 
{ 
    public static void main(String[] args) { 
        Notification emailNotification = NotificationFactory.createNotification("EMAIL"); 
        emailNotification.send("Hello via Email!"); 

        Notification smsNotification = NotificationFactory.createNotification("SMS"); 
        smsNotification.send("Hello via SMS!"); 

        try {
            NotificationFactory.createNotification("FAX");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }      
    } 
}
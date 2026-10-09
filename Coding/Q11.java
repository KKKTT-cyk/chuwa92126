class Q11{
    static interface Notification{
        void send(String message);
    }

    static class EmailNotification implements Notification{
        @Override
        public void send(String message){
            System.out.println(message);
        }
    }

    static class SmsNotification implements Notification{
        @Override
        public void send(String message){
            System.out.println(message);
        }
    }

    static class NotificationFactory{
        public static Notificaction create(String type){
            if(type.equals("email")){
                return new EmailNotification();
            } else if (type.equalsIgnoreCase("sms")) {
                return new SmsNotification();
            }

            throw new IllegalArgumentException("Invalid notification type");
        }
    }
    public class NotificationDemo {
        public static void main(String[] args) {
            Notification email = NotificationFactory.create("email");
            email.send("Hello by email!");

            Notification sms = NotificationFactory.create("sms");
            sms.send("Hello by SMS!");
        }
    }
}
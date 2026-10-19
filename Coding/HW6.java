package com.lant.hw6;


interface Notification{
    public void send(String message);
}
class EmailNotification  implements Notification{

    @Override
    public void send(String message) {
        System.out.printf("Email notification send: %s%n", message);
    }
}
class SmsNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.printf("Sms notification send: %s%n", message);
    }
}

 class NotificationFactory{
    public static Notification create(String type) {
        if("Email".equals(type)){
            return new EmailNotification();
        }
        if("Sms".equals(type)){
            return new SmsNotification();
        }
        return null;
    }

}


class Pizza{
    private int size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean mushrooms;
    private String crust;
    private Pizza(Builder builder){
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "size=" + size +
                ", cheese=" + cheese +
                ", pepperoni=" + pepperoni +
                ", mushrooms=" + mushrooms +
                ", crust='" + crust + '\'' +
                '}';
    }

    static class Builder{
        private int size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";
        public  Builder(int size){
            this.size = size;
        }
        public Builder setCheese(boolean cheese){
            this.cheese = cheese;
            return this;
        }
        public Builder setPepperoni(boolean pepperoni){
            this.pepperoni = pepperoni;
            return this;
        }
        public Builder setMushrooms(boolean mushrooms){
            this.mushrooms = mushrooms;
            return this;
        }
        public Builder setCrust(String crust){
            this.crust = crust;
            return this;
        }
        public Pizza build(){
            return new Pizza(this);
        }
    }


}
public class HW6 {
    public static void main(String[] args) {
        Notification email = NotificationFactory.create("Email");
        email.send("hello");
        Notification sms = NotificationFactory.create("Sms");
        sms.send("hello");


        System.out.println("build a pizza");
        Pizza regular = new Pizza.Builder(14).setCheese(true).setMushrooms(true).setCrust("regular").build();
        System.out.println(regular);
    }
}

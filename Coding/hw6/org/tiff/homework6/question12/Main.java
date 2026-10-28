package org.tiff.homework6.question12;

public class Main {
    public static void main(String[] args) {
        Pizza.Builder builder = new Pizza.Builder("small");
        Pizza first = builder.build();

        builder.cheese(true);
        Pizza second = builder.build();

        System.out.println("First has cheese: " + first.hasCheese());
        System.out.println("Second has cheese: " + second.hasCheese());
        System.out.println("Default crust: " + first.getCrust());

        Pizza custom = new Pizza.Builder("large")
                .cheese(true)
                .pepperoni(true)
                .mushrooms(true)
                .crust("thin")
                .build();

        System.out.println("Custom size: " + custom.getSize());
        System.out.println("Custom has cheese: " + custom.hasCheese());
        System.out.println("Custom has pepperoni: " + custom.hasPepperoni());
        System.out.println("Custom has mushrooms: " + custom.hasMushrooms());
        System.out.println("Custom crust: " + custom.getCrust());
    }
}

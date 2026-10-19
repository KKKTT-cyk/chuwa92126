public class Question12 {
    public static void main(String[] args) {
        Pizza pizza1 = new Pizza.Builder("large")
                .cheese(true)
                .pepperoni(true)
                .crust("thin")
                .build();

        Pizza pizza2 = new Pizza.Builder("medium")
                .mushrooms(true)
                .build();

        System.out.println(
                pizza1.getSize() + ", " +
                        pizza1.hasCheese() + ", " +
                        pizza1.hasPepperoni() + ", " +
                        pizza1.hasMushrooms() + ", " +
                        pizza1.getCrust()
        );

        System.out.println(
                pizza2.getSize() + ", " +
                        pizza2.hasCheese() + ", " +
                        pizza2.hasPepperoni() + ", " +
                        pizza2.hasMushrooms() + ", " +
                        pizza2.getCrust()
        );
    }
}

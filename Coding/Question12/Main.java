public class Main {
    public static void main(String[] args) {
        Pizza plain = new Pizza.Builder("medium").build();
        Pizza custom = new Pizza.Builder("large")
                .cheese(true)
                .pepperoni(true)
                .mushrooms(true)
                .crust("thin")
                .build();

        System.out.println(plain);
        System.out.println(custom);
    }
}

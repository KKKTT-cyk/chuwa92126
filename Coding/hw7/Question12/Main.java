public class Main {
    public static void main(String[] args) {
        Pizza plain = new Pizza.Builder(12).build();

        Pizza loaded = new Pizza.Builder(14)
                .cheese(true)
                .pepperoni(true)
                .mushrooms(true)
                .crust("thin")
                .build();

        System.out.println(plain);
        System.out.println(loaded);
    }
}

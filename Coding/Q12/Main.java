public class Main {
    public static void main(String[] args) {

        Pizza pizza1 = new Pizza.Builder(12)
                .cheese(true)
                .pepperoni(true)
                .crust("thin")
                .build();

        Pizza pizza2 = new Pizza.Builder(10)
                .mushrooms(true)
                .build();

        System.out.println(pizza1);
        System.out.println(pizza2);
    }
}
package Q12;

public class test {
    public static void main(String[] args) {
        Pizza pizza = new Pizza.Builder(12)
                .pepperoni(true)
                .cheese(true)
                .mushroom(true)
                .crust("thick")
                .build();

        System.out.println(pizza);
        Pizza p2 = new Pizza.Builder(10).build();
        System.out.println(p2);

    }
}

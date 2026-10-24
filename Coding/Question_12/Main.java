package Coding.Question_12;

public class Main {
    public static void main(String[] args) {
        Product prod_a = new Product("123", "abc",19.99 );
        Product prod_b = new Product("123", "abc",19.99 );

        System.out.println("Are products equal? " + prod_a.equals(prod_b));

        System.out.println("Product 1 hashCode: " + prod_a.hashCode());
        System.out.println("Product 2 hashCode: " + prod_b.hashCode());

        System.out.println("Same hashCode? "
                + (prod_a.hashCode() == prod_b.hashCode()));

        System.out.println(prod_a.toString());
        System.out.println(prod_b.toString());

    }
}

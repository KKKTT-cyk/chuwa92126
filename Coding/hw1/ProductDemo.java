public class ProductDemo {

    public static void main(String[] args) {

        Product product1 =
                new Product("P001", "Laptop", 999.99);

        Product product2 =
                new Product("P001", "Gaming Laptop", 1499.99);

        System.out.println("product1.equals(product2): "
                + product1.equals(product2));

        System.out.println("Same hashCode: "
                + (product1.hashCode() == product2.hashCode()));

        System.out.println(product1);
        System.out.println(product2);
    }
}
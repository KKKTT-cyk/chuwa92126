package chuwa92126.Coding;

// Question 12

import java.util.Objects;

class Product {

    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{id='" + id
                + "', name='" + name
                + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product other = (Product) obj;

        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class Question12 {

    public static void main(String[] args) {

        Product p1 =
                new Product("P001", "Laptop", 999.99);

        Product p2 =
                new Product("P001", "Gaming Laptop", 1299.99);

        System.out.println(p1.equals(p2));

        System.out.println(
                p1.hashCode() == p2.hashCode()
        );

        System.out.println(p1);
        System.out.println(p2);
    }
}

package Coding;
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
        return "Product {id='" + id + "', name= '" + name + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
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
        Product product1 = new Product("1", "phone", 100);
        Product product2 = new Product("1", "ipad", 200);
        System.out.println("Products equal: " + product1.equals(product2));
        System.out.println("Same hash code: "
                + (product1.hashCode() == product2.hashCode()));
        System.out.println(product1);
        System.out.println(product2);

    }
}

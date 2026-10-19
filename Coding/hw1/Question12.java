import java.util.Objects;

class Product {
    private String id, name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class Question12 {
    public static void main(String[] args) {
        Product p1 = new Product("1", "Car", 99999.22);
        Product p2 = new Product("1", "Book", 23.99);

        System.out.println("Verify equals(): " + p1.equals(p2));
        System.out.println("Verify hashCode() " + (p1.hashCode() == p2.hashCode()));
        System.out.println(p1.toString());
        System.out.println(p2.toString());
    }
}

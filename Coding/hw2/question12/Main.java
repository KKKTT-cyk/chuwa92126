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
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Product other = (Product) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

public class Main {
    public static void main(String[] args) {

        Product p1 = new Product("P001", "Laptop", 1000.99);
        Product p2 = new Product("P001", "SmartPhone", 1234.99);

        System.out.println(p1);
        System.out.println(p2);

        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 hashCode: " + p1.hashCode());
        System.out.println("p2 hashCode: " + p2.hashCode());
    }
}
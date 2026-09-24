/**
 * question 12
 */
public class Question12 {
    static class Product {
        private String id;
        private String name;
        private double price;

        public Product(String id, String name, Double price) {
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
            if (this == obj) return true;
            if (!(obj instanceof Product)) return false;
            Product p = (Product) obj;
            return this.id.equals(p.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    public static void main(String[] args) {
        Product p1 = new Product("01", "Hello", 1.0);
        Product p2 = new Product("02", "Bye", 2.0);
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode() == p2.hashCode());
        System.out.println(p1.toString());
        System.out.println(p2.toString());
    }
}

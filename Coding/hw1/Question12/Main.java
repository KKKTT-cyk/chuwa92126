public class Main {
    public static void main(String[] args) {
        Product p1 = new Product("P001", "Laptop", 999.99);
        Product p2 = new Product("P001", "Gaming Laptop", 1499.99);

        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 == p2: " + (p1 == p2));
        System.out.println("p1.hashCode(): " + p1.hashCode());
        System.out.println("p2.hashCode(): " + p2.hashCode());
        System.out.println("Same hashCode: " + (p1.hashCode() == p2.hashCode()));

        System.out.println(p1);
        System.out.println(p2);
    }
}

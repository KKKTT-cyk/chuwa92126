package org.tiff.question12;

public class Test {
    public static void main(String[] args) {
        Product p1 = new Product("001", "pen", 2.5);
        Product p2 = new Product("001", "notebook", 15.00);

        // print 2 items
        System.out.println(p1.toString());
        System.out.println(p2.toString());

        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 hashcode: " + p1.hashCode());
        System.out.println("p2 hashcode: " + p2.hashCode());
        boolean isSame = p1.hashCode() == p2.hashCode();
        System.out.println("is p1 and p2 same hash code? " + isSame);


    }
}

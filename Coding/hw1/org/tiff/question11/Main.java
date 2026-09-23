package org.tiff.question11;

public class Main {
    public static void main(String[] args) {
        Shape[] arr = new Shape[2];
        Rectangle rectangle = new Rectangle("blue", 3.0, 4.0);
        Circle circle = new Circle("yellow", 5.0);
        arr[0] = rectangle;
        arr[1] = circle;


        for (Shape shape : arr) {
            double area = shape.getArea();
            double perimeter = shape.getPerimeter();
            System.out.println("The Area is: " + area + " and the perimeter is: " + perimeter);
            if (shape instanceof Drawable d) {
                d.draw();
            }

        }
    }
}
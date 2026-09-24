package com.lant.hw1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Q11.
interface Drawable{
    void draw();
}
abstract class Shape{
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double getArea();
    public abstract double getPerimeter();
    public String getColor() {
        return color;
    }
}

class Rectangle extends Shape implements Drawable{
    private double width;
    private double height;
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("draw rectangle");
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
}

class Circle extends Shape implements Drawable{
    private double radius;
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("draw circle");
    }

    @Override
    public double getArea() {
        return radius * radius * Math.PI;
    }

    @Override
    public double getPerimeter() {
        return radius * 2 * Math.PI;
    }
}
// Q12
class Product{
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
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



public class OOP {
    public static void main(String[] args) {
        // Q11
        List<Shape> shapes = Arrays.asList(new Rectangle("red", 10, 10), new Circle("blue", 10));
        for (Shape shape : shapes) {
            System.out.println(shape.getArea());
            System.out.println(shape.getPerimeter());
            System.out.println(shape.getColor());
            if(shape instanceof Drawable){
                Drawable d = (Drawable) shape;
                d.draw();
            }
        }


        // Q12
        System.out.println("== Q12 ==");
        Product p1 = new Product("p1", "p1", 10);
        Product p2 = new Product("p1", "p2", 20);
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode() == p2.hashCode());
        System.out.println(p1.toString());
        System.out.println(p2.toString());

    }
}

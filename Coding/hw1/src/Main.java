import question_11.Circle;
import question_11.Drawable;
import question_11.Rectangle;
import question_11.Shape;
import question_12.Product;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Question 11
        Shape shape1=new Rectangle("red",5.0,7.0);
        Shape shape2 = new Circle("yellow",4);
        Shape shape3 = new Rectangle("blue",3.0,8.0);
        Shape[] shapes = {shape1,shape2,shape3};
        System.out.println("--------Question 11: Shape--------");
        for (Shape s:shapes){
            System.out.println("The area is: "+s.getArea());
            System.out.println("The perimeter is: "+s.getPerimeter());

            if (s instanceof Drawable){
                ((Drawable) s).draw();
            }
            System.out.println("---------------------");
        }

        // Question 12
        Product p1=new Product("1","cup",15.0);
        Product p2 = new Product("1","table",110);
        System.out.println("--------Question 12: Product--------");
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode()==p2.hashCode());
//        if (p1.hashCode()==p2.hashCode()){
//            System.out.println("Both p1 and p2 has the same hashcode: "+p1.hashCode());
//        }
//        else {
//            System.out.println("Hash code of the two product are different.");
//        }
        System.out.println(p1);
        System.out.println(p2);


    }
}
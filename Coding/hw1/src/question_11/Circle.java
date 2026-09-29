package question_11;
import static java.lang.Math.PI;
import static java.lang.Math.pow;
public class Circle extends Shape implements Drawable{
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }


    @Override
    public void draw() {System.out.println("Draw a circle");}

    @Override
    public double getArea() {
        return PI*pow(2,radius);
    }

    @Override
    public double getPerimeter() {
        return 2*PI*radius;
    }
}

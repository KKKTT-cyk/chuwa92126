package question_11;

public class Rectangle extends Shape implements Drawable{
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }


    @Override
    public void draw() {System.out.println("Draw a rectangle");}

    @Override
    public double getArea() {return width*height;}

    @Override
    public double getPerimeter() {return (width+height)*2;}
}

package question_11;

abstract public class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    abstract public double getArea();

    abstract public double getPerimeter();

    public String getColor(){
        return color;
    }
}

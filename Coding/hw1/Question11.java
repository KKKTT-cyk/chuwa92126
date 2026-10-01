/**
 * question 11
 */
public class Question11 {
    interface Drawable { public void draw();}

    static abstract class Shape {
        protected String color;
        public Shape(String color){
            this.color = color;
        }
        public abstract double getArea();
        public abstract double getPerimeter();
        public String getColor(){
            return color;
        }

    }

    static class Rectangle extends Shape implements Drawable {
        private double width;
        private double height;
        public Rectangle(String color, double width, double height){
            super(color);
            this.width = width;
            this.height = height;
        }

        @Override
        public double getArea(){
            return width * height;
        }
        @Override
        public double getPerimeter(){
            return 2 * (width +  height);
        }
        @Override
        public void draw(){
            System.out.println("Drawing Rectangle");
        }
    }

    static class Circle extends Shape implements Drawable{
        private double raidus;
        public Circle(String color, double raidus){
            super(color);
            this.raidus = raidus;
        }
        @Override
        public double getArea(){
            return raidus * raidus * Math.PI;
        }

        @Override
        public String getColor() {
            return super.getColor();
        }

        public double getPerimeter(){
            return Math.PI * 2 * raidus;
        }
        @Override
        public void draw(){
            System.out.println("Drawing Circle");
        }


    }
    public static void main(String[ ] args){
        Shape[] shape = new Shape[2];
        shape[0] = new Rectangle("Grey", 3.0, 3.0);
        shape[1]= new Circle("Grey", 2.0);

        for(int i = 0; i  < 2; i++){
            System.out.println(shape[i].getArea());
            System.out.println(shape[i].getPerimeter());
            if (shape[i] instanceof Drawable){
                ((Drawable) shape[i]).draw();
            }
        }
    }
}
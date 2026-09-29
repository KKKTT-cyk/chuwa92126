public class ShapeDemo {

    public static void main(String[] args) {

        Shape[] shapes = {
                new Rectangle("Blue", 4.0, 5.0),
                new Circle("Red", 3.0)
        };

        for (Shape shape : shapes) {

            System.out.println("Color: " + shape.getColor());
            System.out.println("Area: " + shape.getArea());
            System.out.println("Perimeter: " + shape.getPerimeter());

            if (shape instanceof Drawable) {
                Drawable drawable = (Drawable) shape;
                drawable.draw();
            }

            System.out.println();
        }
    }
}
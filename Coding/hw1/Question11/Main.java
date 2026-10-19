public class Main {
    public static void main(String[] args) {
        // Upcasting: each element is referenced as a Shape
        Shape[] shapes = {
                new Rectangle("red", 3, 4),
                new Circle("blue", 2)
        };

        for (Shape shape : shapes) {
            // Runtime polymorphism: the subclass's getArea()/getPerimeter() is called
            System.out.printf("area = %.2f, perimeter = %.2f%n",
                    shape.getArea(), shape.getPerimeter());

            // Shape itself does not implement Drawable, so check before downcasting
            if (shape instanceof Drawable) {
                Drawable drawable = (Drawable) shape;
                drawable.draw();
            }
        }
    }
}

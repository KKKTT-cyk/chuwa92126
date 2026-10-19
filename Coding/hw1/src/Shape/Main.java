package Shape;

public class Main {
  public static void main(String[] args) {
    Shape[] shapes = {
        new Rectangle("Black", 3, 8),
        new Circle("Pink", 5)
    };

    for (Shape s : shapes) {
      System.out.println("Color: " + s.getColor());
      System.out.println("Area: " + s.getArea());
      System.out.println("Perimeter: " + s.getPerimeter());

      if (s instanceof Drawable) {
        Drawable d = (Drawable) s;
        d.draw();
      }
      System.out.println();
    }
  }
}
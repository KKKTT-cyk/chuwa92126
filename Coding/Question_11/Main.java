package Coding.Question_11;

public class Main {
    public static void main(String[] args) {
        Shape[] arr_Shapes = {
            new Rectangle("Blue", 5.0, 3.0),
            new Circle("Red", 4.0),
            new Rectangle("Green", 7.0, 6.0),
            new Circle("Yellow", 8.0)
        };
        for(int i = 0;i<arr_Shapes.length;i++){
            System.out.println("The area is: " + arr_Shapes[i].getArea());
            System.out.println("The perimeter is: " + arr_Shapes[i].getPerimeter());
            if (arr_Shapes[i] instanceof Drawable) {
                Drawable drawable = (Drawable) arr_Shapes[i];
                drawable.draw();
            }
            System.out.println();
        }
    }
}

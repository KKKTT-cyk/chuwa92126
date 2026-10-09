# Java Design Patterns Homework — Answers to Questions 1–10

## 1. Using the shape-area example covered in class, contrast the instanceof-based implementation with the interface-based refactor, and explain why the refactored version follows the Open/Closed Principle (OCP).

Answer: The Open/Closed Principle says software should be open for extension but closed for modification. The first implementation checks concrete types, so adding a new shape means editing the existing method. The refactor delegates area calculation to the `Shape` interface, allowing each new shape to supply its own behavior without changing existing calculation code.

```java
// VIOLATES OCP — must MODIFY this method whenever a new shape is added.
public double calculateArea(Object shape) {
    if (shape instanceof Rectangle) {
        Rectangle r = (Rectangle) shape;
        return r.getWidth() * r.getHeight();
    } else if (shape instanceof Circle) {
        Circle c = (Circle) shape;
        return Math.PI * c.getRadius() * c.getRadius();
    }
    // Every new shape requires editing THIS method.
    return 0;
}

// FOLLOWS OCP — add a new implementation without changing existing callers.
interface Shape {
    double calculateArea();
}

class Rectangle implements Shape {
    private final double width, height;
    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    public double calculateArea() { return width * height; }
}

class Circle implements Shape {
    private final double radius;
    Circle(double radius) { this.radius = radius; }
    public double calculateArea() { return Math.PI * radius * radius; }
}

// Later: add Triangle implements Shape; no edits to Rectangle or Circle.
// Caller: double area = shape.calculateArea();
```

## 2. Explain the difference in terms of what each creates (one product vs. a family of matching products).

Answer:Factory method defines a creation method for one product type; subclasses can decide which concrete product to return. Abstract factory provides a group of creation methods for a family of related products that are meant to work together.

```java
// FACTORY METHOD — one product category (Notification).
interface Notification { void send(String message); }
class EmailNotification implements Notification {
    public void send(String message) { System.out.println("Email: " + message); }
}
abstract class NotificationCreator {
    abstract Notification createNotification();
}
class EmailCreator extends NotificationCreator {
    Notification createNotification() { return new EmailNotification(); }
}

// ABSTRACT FACTORY — a matching family of products (Button + Checkbox).
interface Button { void render(); }
interface Checkbox { void render(); }
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
class WindowsFactory implements UIFactory {
    public Button createButton() { return () -> System.out.println("Windows button"); }
    public Checkbox createCheckbox() { return () -> System.out.println("Windows checkbox"); }
}
```

## 3.Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly, and describe how the Builder pattern solves this problem.

Answer: Telescoping constructors become hard to read and maintain because callers must remember the order of many parameters, especially booleans or similarly typed values. A Builder gives optional settings descriptive names, supports method chaining, and keeps required values explicit. It can also validate values before building an immutable object.

```java
// HARD TO READ — what do these booleans mean?
// Pizza pizza = new Pizza("large", true, false, true, "thin");

// BUILDER — self-documenting, chainable options.
Pizza pizza = new Pizza.Builder("large")
    .cheese(true)
    .mushrooms(true)
    .crust("thin")
    .build();
```

The full Pizza.Builder implementation is the separate task in Question 12.

## 4.Describe the roles of the Context, the Strategy interface, and the concrete Strategy implementations in the Strategy pattern, and explain the benefit of being able to swap the active strategy at runtime.

Answer:The Strategy interface declares a common algorithm operation. Concrete strategies implement different versions of that algorithm. The context holds a Strategy reference and delegates work to it. Swapping strategies at runtime changes behavior without rewriting the Context or using a long `if/else` chain.

```java
interface PaymentStrategy { void pay(double amount); }
class CardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Card: " + amount); }
}
class PayPalPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("PayPal: " + amount); }
}
class Checkout { // Context
    private PaymentStrategy strategy;
    Checkout(PaymentStrategy strategy) { this.strategy = strategy; }
    void setStrategy(PaymentStrategy strategy) { this.strategy = strategy; }
    void pay(double amount) { strategy.pay(amount); }
}

// Checkout checkout = new Checkout(new CardPayment());
// checkout.pay(20);
// checkout.setStrategy(new PayPalPayment()); // swap at runtime
// checkout.pay(20);
```

## 5.Describe the one-to-many relationship between Subject and Observer in the Observer pattern, and explain why adding a new type of observer does not require changing the Subject's code.

Answer:One subject keeps a collection of **Observers** and notifies each observer when its state changes. Every observer implements a common interface, so the Subject calls `update()` without knowing the concrete observer class. A new observer type only needs to implement that interface and subscribe; the Subject stays unchanged.

```java
interface Observer { void update(String news); }
class EmailSubscriber implements Observer {
    public void update(String news) { System.out.println("Email: " + news); }
}
class NewsPublisher { // Subject
    private final java.util.List<Observer> observers = new java.util.ArrayList<>();
    void subscribe(Observer observer) { observers.add(observer); }
    void publish(String news) {
        for (Observer observer : observers) observer.update(news);
    }
}
// Adding SmsSubscriber implements Observer needs no changes to NewsPublisher.
```

## 6.Explain what problem the Decorator pattern solves (referencing the "subclass explosion" example), and give one real-world Java example that follows this pattern?

Answer:When features are combined through inheritance, each combination may require another subclass (for example, `CoffeeWithMilk`, `CoffeeWithSugar`, `CoffeeWithMilkAndSugar`). This subclass explosion is hard to maintain. Decorator uses composition: wrap an object in one or more objects with the same interface to add responsibilities dynamically. Java's I/O streams are a real example: `BufferedInputStream` wraps an `InputStream` to add buffering.

```java
// DECORATOR — BufferedInputStream adds buffering without subclassing FileInputStream.
try (java.io.InputStream in = new java.io.BufferedInputStream(
        new java.io.FileInputStream("data.txt"))) {
    int firstByte = in.read();
}
// Other wrappers can be composed without making a subclass for each combination.
```

## 7.Explain the core difference in intent between Proxy and Decorator, even though the two patterns have nearly identical structure.

Answer: Both typically implement the same interface as a wrapped object and forward calls to it. A proxy primarily controls access to the real object (lazy initialization, access checks, remote access, caching). A decorator primarily adds behavior or responsibilities (buffering, logging, compression) while preserving the interface.

```java
interface Image { void display(); }
class RealImage implements Image {
    RealImage(String path) { System.out.println("Expensive load: " + path); }
    public void display() { System.out.println("Showing image"); }
}
class LazyImageProxy implements Image { // Proxy: delays object creation
    private RealImage real;
    private final String path;
    LazyImageProxy(String path) { this.path = path; }
    public void display() {
        if (real == null) real = new RealImage(path);
        real.display();
    }
}
class BorderedImage implements Image { // Decorator: adds behavior
    private final Image wrapped;
    BorderedImage(Image wrapped) { this.wrapped = wrapped; }
    public void display() {
        System.out.println("Draw border");
        wrapped.display();
    }
}
```

## 8. Explain the difference between Adapter, Decorator, and Proxy, based on (a) whether the interface being wrapped is the same or different, and (b) what each pattern's primary intent is.

Answer: The main difference between Adapter, Decorator, and Proxy is their interface and purpose. Adapter wraps an object with a different interface to make incompatible classes work together. For example, an `HDMIAdapter` can implement a `USB` interface and internally call an HDMI device's methods, allowing the device to work with code expecting USB. Decorator wraps an object using the same interface to add new functionality without modifying the original class. For example, a `MilkDecorator` implements the `Coffee` interface and wraps a `BasicCoffee` object, adding $1 to its original cost. Proxy also uses the same interface as the original object, but its main purpose is to control access rather than add functionality. For example, an `ImageProxy` implements the `Image` interface and delays creating a `RealImage` object until the `display()` method is called, saving memory and resources. In short, Adapter changes the interface, Decorator adds behavior, and Proxy controls access.


## 9. Explain why a naive lazily-initialized Singleton is not thread-safe, and explain how Double-Checked Locking combined with volatile fixes this.

Answer: A naive `if (instance == null)` check is unsafe because two threads can both see `null` and each create a Singleton. Double-checked locking first checks without a lock, synchronizes only when needed, and checks again inside the lock so only one thread initializes the instance. `volatile` ensures visibility across threads and prevents unsafe reordering that could expose a partially constructed instance.

```java
// NOT THREAD-SAFE — two threads can create separate instances.
class UnsafeSingleton {
    private static UnsafeSingleton instance;
    private UnsafeSingleton() {}
    static UnsafeSingleton getInstance() {
        if (instance == null) instance = new UnsafeSingleton();
        return instance;
    }
}

// THREAD-SAFE — double-checked locking + volatile.
class SafeSingleton {
    private static volatile SafeSingleton instance;
    private SafeSingleton() {}
    static SafeSingleton getInstance() {
        if (instance == null) {
            synchronized (SafeSingleton.class) {
                if (instance == null) {
                    instance = new SafeSingleton();
                }
            }
        }
        return instance;
    }
}
```

## 10. For each of the following situations, name the design pattern that best fits it: (a) you need exactly one shared instance of a class, accessible globally; (b) you have several interchangeable algorithms and need to switch between them at runtime; (c) you need to delay the creation of an expensive object until it is actually used, without changing the calling code.

**Answer:**

- (a) Exactly one shared instance, globally accessible → Singleton :Ensures one instance and provides a global access point.
- (b) Interchangeable algorithms that can change at runtime → Strategy:  Encapsulates algorithms behind a shared interface and lets the Context switch implementations.
- (c) Delay expensive object creation until first use without changing calling code → Proxy (Virtual/Lazy Proxy):  The proxy implements the same interface and creates the real object only when needed.

```java
// (a) Singleton
// SafeSingleton service = SafeSingleton.getInstance();

// (b) Strategy
// checkout.setStrategy(new PayPalPayment());

// (c) Lazy Proxy — RealImage is constructed only when display() is called.
// Image image = new LazyImageProxy("large-photo.png");
// image.display();
```

---
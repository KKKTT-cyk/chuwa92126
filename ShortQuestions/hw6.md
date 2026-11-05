# Homework 6 - Design Patterns

## 1. Shape Area Calculation and the Open/Closed Principle

In the instanceof-based version, calculateArea() checks the type of each shape and contains all the area formulas. Adding a new shape requires modifying this method and adding another condition.

In the interface-based version, each shape implements the Shape interface and provides its own area() method. The calculator simply calls shape.area(), and polymorphism selects the correct implementation.

This follows the Open/Closed Principle because we can support new shapes by adding new classes without modifying the existing calculator.

## 2. Factory Method vs. Abstract Factory

Factory Method defines a method for creating a product and lets subclasses decide which concrete product to create. For example, a MouseFactory can define createMouse(), and its subclasses can return different kinds of mice. It focuses on one product type, but the method can be called multiple times to create multiple objects.

Abstract Factory provides methods for creating a family of related or matching products. For example, a VintagePeripheralFactory can create a vintage mouse, keyboard, and monitor, while a ModernPeripheralFactory creates matching modern versions. Both factories implement the same factory interface, with methods such as createMouse(), createKeyboard(), and createMonitor().

The main difference is that Factory Method focuses on creating a single product type, while Abstract Factory creates a family of related product types. An Abstract Factory does not have to contain several factories; one concrete factory can provide multiple creation methods for its matching products.

## 3. Telescoping Constructors and the Builder Pattern

Telescoping constructors are overloaded constructors with increasing numbers of parameters. They become difficult to maintain when a class has many optional fields. Calls with several similar parameters, such as true, false, and true, are also hard to read and easy to get wrong.

The Builder pattern lets us create an object step by step using named methods. Required values, such as pizza size, can be passed to the Builder constructor. Optional values have defaults in the Builder, and we only set the options we want to change.

```java
Pizza pizza = new Pizza.Builder("small")
        .pepperoni(true)
        .build();
```

This is easier to read than a long constructor call and avoids adding constructors for many combinations of options. In this example, cheese and mushrooms remain false, and crust remains "regular", according to the Builder's defaults.

The following class shows where the defaults are stored and how build() creates the Pizza:

```java
public class Pizza {
    // These fields belong to the finished Pizza.
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final String crust;

    private Pizza(Builder builder) {
        // Copy the selected options from Builder to Pizza.
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    public static class Builder {
        // These fields belong to Builder, with defaults for optional values.
        private final String size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";

        public Builder(String size) {
            this.size = size;
        }

        public Builder cheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        public Builder pepperoni(boolean pepperoni) {
            this.pepperoni = pepperoni;
            return this;
        }

        public Builder mushrooms(boolean mushrooms) {
            this.mushrooms = mushrooms;
            return this;
        }

        public Builder crust(String crust) {
            this.crust = crust;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}
```

In the short usage example above:

1. new Pizza.Builder("small") creates a Builder with size "small" and the default options.
2. pepperoni(true) changes the Builder's pepperoni field. It returns this, meaning the same Builder, so we can chain another method call.
3. build() calls new Pizza(this). Here, this is the Builder, which is passed to the private Pizza constructor.
4. The constructor copies the Builder's values into the new Pizza. For example, this.mushrooms = builder.mushrooms copies false from the Builder into the Pizza's separate mushrooms field.

The Builder is like an order form, and Pizza is the finished product. The Builder's optional fields can change while we configure the order. Pizza's fields are private final and it has no setters, so its settings cannot be changed after construction. Changing the Builder later does not change an existing Pizza in this example.

The final keyword prevents reassignment; it does not define a default value. Builder alone does not guarantee immutability. This Pizza uses primitive values and immutable Strings. If a class contains mutable data, such as a List, it may also need defensive copies.

## 4. The Context in the Strategy Pattern

The Context holds a reference to the current strategy and delegates work to it through a common strategy interface. The interface defines the operation, while concrete strategies provide different algorithms. The Context does not need to know the details of each algorithm.

For example, Navigator is the Context, RouteStrategy is the interface, and DrivingStrategy and WalkingStrategy are concrete strategies. The following simplified example prints the selected route type instead of implementing actual route calculations:

```java
interface RouteStrategy {
    void calculateRoute();
}

class DrivingStrategy implements RouteStrategy {
    @Override
    public void calculateRoute() {
        System.out.println("Calculate a driving route.");
    }
}

class WalkingStrategy implements RouteStrategy {
    @Override
    public void calculateRoute() {
        System.out.println("Calculate a walking route.");
    }
}

class Navigator {
    private RouteStrategy strategy;

    public Navigator(RouteStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RouteStrategy strategy) {
        this.strategy = strategy;
    }

    public void calculateRoute() {
        strategy.calculateRoute();
    }
}
```

We can replace the strategy at runtime through setStrategy():

```java
Navigator navigator = new Navigator(new DrivingStrategy());
navigator.calculateRoute(); // Uses the driving algorithm.

navigator.setStrategy(new WalkingStrategy());
navigator.calculateRoute(); // Uses the walking algorithm.
```

The Navigator object stays the same, but its strategy changes. We do not need to modify the Navigator class to switch algorithms. Navigator holds a RouteStrategy; it does not have to implement that interface itself.

Like the Shape example in Question 1, this uses interfaces and polymorphism. Polymorphism is the language mechanism, while Strategy is a design pattern that uses interchangeable objects to encapsulate algorithms. Using an interface alone does not automatically make a design the Strategy pattern.

## 5. The Observer Pattern

Observer defines a one-to-many relationship between a Subject and its Observers. The Subject maintains a list of observers, allows them to register or unregister, and notifies them when its state changes or an event occurs. Each Observer receives the notification through a common method and handles it in its own way.

For example, a video channel is the Subject and its subscribers are Observers. When the channel publishes a video, it calls update() on each registered subscriber. The update() method belongs to the Observer, and the Subject calls it.

```java
import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String videoTitle);
}

class EmailSubscriber implements Observer {
    @Override
    public void update(String videoTitle) {
        // Simulate an email notification for this example.
        System.out.println("Email notification: " + videoTitle);
    }
}

class Channel {
    private final List<Observer> subscribers = new ArrayList<>();

    public void subscribe(Observer observer) {
        subscribers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
    }

    public void publishVideo(String videoTitle) {
        for (Observer subscriber : subscribers) {
            subscriber.update(videoTitle);
        }
    }
}
```

Example usage:

```java
Channel channel = new Channel();
Observer subscriber = new EmailSubscriber();
channel.subscribe(subscriber);
channel.publishVideo("Java Design Patterns");
channel.unsubscribe(subscriber);
```

The Subject depends on the Observer interface rather than specific subscriber classes. To add a new notification behavior, we can create another class implementing Observer and register its object with the Subject. The notification loop does not need to change. Creating a new Observer class alone is not enough: its object must be registered to receive notifications.

## 6. The Decorator Pattern

Decorator adds behavior by wrapping an object while keeping a compatible interface. A decorator delegates work to the wrapped object and adds its own behavior. Multiple decorators can be combined without creating a subclass for every combination of features.

For example, coffee can have milk, sugar, or chocolate. Using only subclasses for combinations could require CoffeeWithMilk, CoffeeWithSugar, CoffeeWithMilkAndSugar, and many more classes as options increase. This is called subclass explosion. With decorators, we can add one ChocolateDecorator and combine it with existing decorators.

Conceptually, the usage looks like this, with all the classes implementing Coffee:

```java
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new ChocolateDecorator(coffee);
```

Each decorator's cost() method calls the wrapped object's cost() and adds its own ingredient's price.

A real Java example is BufferedInputStream wrapping FileInputStream:

```java
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class ReadExample {
    public static void main(String[] args) throws IOException {
        try (InputStream input = new BufferedInputStream(
                new FileInputStream("notes.txt"))) {
            int firstByte = input.read();
            System.out.println(firstByte);
        }
    }
}
```

This example requires notes.txt to exist in the working directory. FileInputStream is the wrapped object that reads the file; the file itself is not the wrapped Java object. BufferedInputStream is the decorator that adds buffering. It serves reads from its buffer when possible and obtains more data from the underlying stream when needed, reducing underlying read operations.

Both classes can be used as InputStream, so the caller still uses read(). Here, the shared type InputStream is an abstract class. The pattern's common interface does not have to be declared with Java's interface keyword. The try-with-resources statement closes the outer stream, which also closes the wrapped stream.

## 7. Proxy vs. Decorator

Proxy and Decorator can have similar structures: both can hold another object and provide the same interface to the caller. Their main difference is their purpose. A Proxy controls access to another object, while a Decorator adds behavior to an object.

For example, a protection proxy checks whether a user has permission before forwarding a request to the real object. A virtual proxy can delay creating an expensive object until it is actually needed. Access control therefore does not only mean checking permissions; it can also mean controlling when the real object is created or accessed.

The following simplified example shows lazy loading:

```java
interface Image {
    void display();
}

class RealImage implements Image {
    public RealImage(String filename) {
        // Simulate expensive image loading.
        System.out.println("Loading " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying the image.");
    }
}

class ImageProxy implements Image {
    private RealImage realImage;
    private final String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
```

Creating an ImageProxy only stores the filename. The first display() call creates the RealImage; later calls reuse it. This simple example assumes single-threaded use.

In contrast, an image decorator might add a border or watermark when displaying an image. Its goal is to extend behavior. We should identify the pattern by its intent, not just by whether one object wraps another.

## 8. Adapter, Decorator, and Proxy

These patterns can all wrap another object, but their purposes and interface relationships differ.

| Pattern | Main purpose | Interface relationship |
| --- | --- | --- |
| Adapter | Make incompatible interfaces work together | Exposes the interface the caller expects and translates calls to the wrapped object's different interface |
| Decorator | Add behavior that can be combined with other decorators | Preserves the common component interface |
| Proxy | Control access to another object | Provides the same subject interface as the real object |

For example, a new application expects pay(), but an old payment system provides makePayment(). An Adapter connects them without modifying the old system:

```java
interface Payment {
    void pay();
}

class OldPaymentSystem {
    public void makePayment() {
        // Simulate payment for this example.
        System.out.println("Payment completed.");
    }
}

class PaymentAdapter implements Payment {
    private final OldPaymentSystem oldSystem;

    public PaymentAdapter(OldPaymentSystem oldSystem) {
        this.oldSystem = oldSystem;
    }

    @Override
    public void pay() {
        oldSystem.makePayment();
    }
}
```

Example usage:

```java
Payment payment = new PaymentAdapter(new OldPaymentSystem());
payment.pay();
```

The caller invokes pay(), and the Adapter delegates to makePayment(). The @Override annotation refers to implementing pay() from the Payment interface; it does not mean the Adapter overrides the old makePayment() method. Adapters can also translate arguments or return values when needed.

In contrast, a Decorator adds behavior through a compatible interface, such as buffering a stream, while a Proxy controls access through the same interface, such as checking permissions or delaying expensive initialization. The design intent is more important than the similar wrapper structure.

## 9. Thread-Safe Lazy Singleton

A naive lazy Singleton is not thread-safe because checking instance and creating an object are separate actions. Two threads can both see instance == null before either assigns the new object, so both can create a Singleton. Making instance volatile alone does not prevent this race.

Double-checked locking uses a lock and a second check to prevent duplicate creation:

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

The first check avoids acquiring the lock once the object has been initialized. If the field is null, the thread acquires the lock and checks again. The second check is necessary because another thread may have created the object while this thread was waiting for the lock.

For example, threads A and B may both pass the first check. A acquires the lock and creates the object. After A releases the lock, B acquires it and checks again. B now sees a non-null instance and does not create another object.

The volatile keyword provides visibility and ordering guarantees. In this double-checked locking implementation, some reads occur outside the synchronized block, so volatile is needed for safe publication. A thread that reads the published reference can also see the initialization writes that happened before it was published. Without this guarantee, a non-null reference alone would not ensure that the reading thread sees a fully initialized object.

Publishing here means storing the object's reference in the shared instance field so other threads can obtain it. It is different from merely constructing the object. Conceptually, the process is:

```java
Singleton temp = new Singleton(); // Construct and initialize the object.
instance = temp;                  // Publish its reference in the shared field.
```

The keywords have different jobs:

- static makes instance a class-level field.
- synchronized provides mutual exclusion and visibility through the shared lock.
- volatile provides visibility and ordering for accesses to instance, without making the check-and-create sequence atomic.

Not every Singleton implementation requires volatile. For example, synchronizing the entire getInstance() method can provide the necessary guarantees when all accesses to instance use that same lock. However, the double-checked locking implementation above needs volatile.

Also, volatile on instance does not automatically make later operations on the Singleton thread-safe. For example, a count++ operation inside the object still needs appropriate synchronization or an atomic operation if multiple threads update it.

## 10. Choosing Design Patterns

1. **One shared instance: Singleton.** Singleton provides one shared instance and an access point to it. If it is lazily initialized and used by multiple threads, its creation must be thread-safe. This does not automatically make every operation on the shared object thread-safe.

2. **Interchangeable algorithms at runtime: Strategy.** Strategy encapsulates different algorithms behind a common interface. The Context holds a strategy and can replace it at runtime, such as changing a Navigator from DrivingStrategy to WalkingStrategy. These are different implementations of the same interface, not separate interfaces for each algorithm.

3. **Delay expensive object creation: Virtual Proxy.** A virtual proxy exposes the interface expected by the caller and creates the real object only when it is needed. For example, an image proxy loads the real image on the first display() call and reuses it afterward. It controls the creation time; permission checking is not required for this use case. The implementation code already exists, but object creation is delayed.

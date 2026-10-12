# Java Design Patterns Homework

## 1. Open/Closed Principle (OCP)

In the instanceof-based implementation, the program checks the type of each shape using conditions such as `if (shape instanceof Circle)` or `if (shape instanceof Rectangle)` to calculate its area. Whenever a new shape is added, the existing calculation code must be modified.

In the interface-based implementation, each shape implements a common `Shape` interface with an `area()` method. Each shape calculates its own area, so the calling code simply invokes `shape.area()`.

The refactored version follows the Open/Closed Principle because the system is open for extension (adding new shapes) but closed for modification (existing calculation code does not need to change).

## 2. Factory Method vs. Abstract Factory

The main difference is what they create.

- **Factory Method:** Creates one type of product through a factory method. For example, a notification factory creates an email or SMS notification.
- **Abstract Factory:** Creates families of related products that are designed to work together. For example, a Windows UI factory creates Windows buttons and checkboxes, while a Mac UI factory creates Mac buttons and checkboxes.

Factory Method focuses on creating a single product, while Abstract Factory focuses on creating a family of matching products.

## 3. Telescoping Constructors vs. Builder Pattern

Telescoping constructors use multiple overloaded constructors to support different combinations of parameters. They become difficult to use when a class has many optional fields because there are too many constructor combinations, and it is easy to pass arguments in the wrong order.

The Builder pattern solves this problem by allowing objects to be constructed step by step using readable, chainable methods.

For example:

```java
Pizza pizza = new Pizza.Builder("large")
    .cheese(true)
    .pepperoni(true)
    .build();
```

This improves readability, avoids unnecessary constructor overloads, and makes it easier to create immutable objects.

## 4. Strategy Pattern

The Strategy pattern defines a family of interchangeable algorithms.

- **Context:** The class that uses a strategy to perform an operation.
- **Strategy interface:** Defines a common method that all algorithms must implement.
- **Concrete Strategies:** Provide different implementations of the algorithm.

For example, a payment system may support `CreditCardPayment`, `PayPalPayment`, and `ApplePayPayment`, all implementing a `PaymentStrategy` interface.

The main benefit is that the program can switch algorithms at runtime without modifying the Context class. This improves flexibility and reduces conditional logic.

## 5. Observer Pattern

The Observer pattern establishes a one-to-many relationship between a Subject and multiple Observers.

The Subject maintains a collection of registered observers. When its state changes, it notifies them by calling a method such as `update()`.

For example, a weather station (Subject) can notify multiple displays (Observers) whenever the temperature changes.

Adding a new type of observer does not require modifying the Subject because the Subject communicates with observers through a common Observer interface, rather than depending on their concrete implementations.

## 6. Decorator Pattern

The Decorator pattern solves the problem of subclass explosion, which happens when many subclasses are needed to represent different combinations of features.

For example, a coffee system might require subclasses such as `CoffeeWithMilk`, `CoffeeWithSugar`, and `CoffeeWithMilkAndSugar`. As more options are added, the number of subclasses grows rapidly.

The Decorator pattern solves this by wrapping objects with additional functionality dynamically, without creating a new subclass for every combination.

A real-world Java example is the Java I/O library:

```java
BufferedInputStream input =
    new BufferedInputStream(
        new FileInputStream("file.txt")
    );
```

Here, `BufferedInputStream` decorates `FileInputStream` by adding buffering functionality.

## 7. Proxy vs. Decorator

Proxy and Decorator have similar structures because both wrap another object and usually implement the same interface.

However, their primary intentions are different.

- **Proxy:** Controls access to an object, such as delaying object creation, checking permissions, or handling remote access.
- **Decorator:** Adds new functionality or behavior to an object dynamically.

For example, a virtual proxy may delay loading a large image until it is needed, while a decorator may add buffering to an input stream.

The key difference is that Proxy controls access, while Decorator extends behavior.

## 8. Adapter vs. Decorator vs. Proxy

| Pattern | Interface | Primary Intent |
|---|---|---|
| Adapter | Different interface | Converts an incompatible interface into one the client expects |
| Decorator | Same interface | Adds functionality dynamically |
| Proxy | Same interface | Controls access to an object |

An Adapter allows incompatible classes to work together by translating one interface into another.

A Decorator preserves the original interface while adding extra behavior.

A Proxy also preserves the original interface but manages access to the underlying object.

Therefore, Adapter focuses on compatibility, Decorator focuses on additional functionality, and Proxy focuses on access control.

## 9. Singleton: Thread Safety and Double-Checked Locking

A naive lazily initialized Singleton is not thread-safe because two threads may simultaneously check whether the instance is `null`. Both threads might then create separate objects, violating the requirement that only one instance exists.

Double-Checked Locking solves this by checking the instance twice: once before synchronization and once inside a synchronized block.

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

The `synchronized` block ensures that only one thread can initialize the instance at a time.

The `volatile` keyword ensures visibility between threads and prevents unsafe instruction reordering during object initialization.

This implementation provides thread-safe lazy initialization while avoiding synchronization after the instance has been created.

## 10. Identify the Design Patterns

### (a) Exactly one shared instance of a class, accessible globally

**Answer: Singleton Pattern**

It ensures that only one instance of a class exists and provides a global access point to that instance.

### (b) Several interchangeable algorithms that can be switched at runtime

**Answer: Strategy Pattern**

It encapsulates different algorithms behind a common interface and allows the program to switch between them dynamically.

### (c) Delay creation of an expensive object until it is actually used, without changing the calling code

**Answer: Proxy Pattern (Virtual Proxy)**

A virtual proxy acts as a placeholder for an expensive object and creates the real object only when needed.

## 11. Factory Method — Notification System

The following implementation uses a `Notification` interface, two concrete implementations, and a factory that creates the appropriate notification based on the input type.

```java
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationFactory {
    public static Notification create(String type) {
        if ("email".equalsIgnoreCase(type)) {
            return new EmailNotification();
        } else if ("sms".equalsIgnoreCase(type)) {
            return new SmsNotification();
        } else {
            throw new IllegalArgumentException(
                "Unknown notification type: " + type
            );
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Notification email =
            NotificationFactory.create("email");
        email.send("Hello via Email!");

        Notification sms =
            NotificationFactory.create("sms");
        sms.send("Hello via SMS!");
    }
}
```

**Output:**

```text
Email: Hello via Email!
SMS: Hello via SMS!
```

**Explanation:**

The `Notification` interface defines a common `send()` method. `EmailNotification` and `SmsNotification` implement this interface with different behaviors.

The `NotificationFactory.create()` method determines which implementation to instantiate based on the provided string.

The client code does not need to instantiate the concrete notification classes directly.

Note: This matches the factory API explicitly requested in the homework. Strictly speaking, a static `create()` method selecting implementations with conditionals is usually called a Simple Factory, rather than the classical GoF Factory Method pattern, which uses inheritance and an overridable factory method.

## 12. Builder Pattern — Immutable Pizza Class

The following implementation uses a static inner `Builder` class, a required `size` field, and optional fields with their specified defaults.

```java
final class Pizza {
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final String crust;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    public static class Builder {
        private final String size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";

        public Builder(String size) {
            if (size == null || size.isBlank()) {
                throw new IllegalArgumentException(
                    "Size is required"
                );
            }
            this.size = size;
        }

        public Builder cheese(boolean value) {
            this.cheese = value;
            return this;
        }

        public Builder pepperoni(boolean value) {
            this.pepperoni = value;
            return this;
        }

        public Builder mushrooms(boolean value) {
            this.mushrooms = value;
            return this;
        }

        public Builder crust(String value) {
            if (value == null) {
                throw new IllegalArgumentException(
                    "Crust cannot be null"
                );
            }
            this.crust = value;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return "Pizza{" +
            "size='" + size + '\'' +
            ", cheese=" + cheese +
            ", pepperoni=" + pepperoni +
            ", mushrooms=" + mushrooms +
            ", crust='" + crust + '\'' +
            '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Pizza pizza = new Pizza.Builder("large")
            .cheese(true)
            .pepperoni(true)
            .mushrooms(true)
            .crust("thin")
            .build();

        System.out.println(pizza);
    }
}
```

**Output:**

```text
Pizza{size='large', cheese=true, pepperoni=true, mushrooms=true, crust='thin'}
```

**Explanation:**

The `Pizza` class has a private constructor, so objects can only be created through its `Builder`.

The `size` parameter is required when creating a Builder. The optional fields `cheese`, `pepperoni`, and `mushrooms` default to `false`, while `crust` defaults to `"regular"`.

Each Builder method returns `this`, allowing methods to be chained together. Finally, `build()` creates the Pizza object.

The Pizza object is immutable because the class is `final`, all its instance fields are `private final`, and it provides no setter methods. The Builder stores the configuration, while the constructed Pizza cannot be modified.
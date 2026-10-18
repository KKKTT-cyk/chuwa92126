# HW8 – Java Design Patterns

## Q1. instanceof vs. Interface (OCP)

**instanceof-based:** A single `AreaCalculator` checks each shape's type and computes the area in an if-else chain.

```java
double area(Object shape) {
    if (shape instanceof Circle c) return Math.PI * c.radius * c.radius;
    if (shape instanceof Rectangle r) return r.width * r.height;
    throw new IllegalArgumentException("Unknown shape");
}
```

Adding a new shape (e.g. `Triangle`) requires **modifying** this method, and a missed case only fails at runtime.

**Interface-based:** Each shape implements `Shape` and computes its own area.

```java
interface Shape { double area(); }

class Circle implements Shape {
    double radius;
    public double area() { return Math.PI * radius * radius; }
}

class Rectangle implements Shape {
    double width, height;
    public double area() { return width * height; }
}
```

The caller only depends on `Shape` and calls `shape.area()`; polymorphism picks the right implementation at runtime.

**Why it follows OCP:** OCP means *open for extension, closed for modification*. To add `Triangle`, we just create a new class implementing `Shape`. No existing code changes, and the compiler forces every new shape to implement `area()`.



## Q2. Factory Method vs. Abstract Factory

Both patterns hide object creation so the client depends on interfaces instead of calling `new` on concrete classes. The difference is **what they create**.

**Factory Method** creates **a single product**. A method (often overridden in subclasses) decides which concrete class to instantiate.

```java
abstract class Logistics {
    abstract Transport createTransport();
}
class RoadLogistics extends Logistics {
    Transport createTransport() { return new Truck(); }
}
```

**Abstract Factory** creates **a family of related products** that are designed to work together. One factory interface has multiple creation methods, and each concrete factory produces a matching set.

```java
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
class MacFactory implements GUIFactory {
    public Button createButton()     { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

Choosing `MacFactory` guarantees all components are Mac-style, so products from different families are never mixed.

**In short:** Factory Method = one method, one product. Abstract Factory = one factory, a consistent family of products.


## Q3. Telescoping Constructors and the Builder Pattern

**Problem:** With many optional fields, classes often end up with a chain of constructors taking more and more parameters, e.g. `new Pizza("large", true, false, true, "thin")`. This is hard to use correctly because:
- It is unclear what each argument means.
- Parameters of the same type (like several `boolean`s) can be swapped without a compiler error.
- Callers must pass values for earlier parameters even if they only want to set a later one.

**Builder solution:** Fields are set through named, chainable methods, and `build()` creates the object.

```java
Pizza pizza = new Pizza.Builder("large")
        .cheese(true)
        .mushrooms(true)
        .build();
```

Each value is labeled, order does not matter, unset fields keep defaults, required fields go in the Builder's constructor, and `build()` returns an immutable object.



## Q4. Strategy Pattern

The Strategy pattern separates **what** needs to be done from **how** it is done, so different algorithms can be swapped without changing the code that uses them.

- **Strategy interface:** defines the common method every algorithm must implement.
- **Concrete Strategies:** classes that implement the interface, each providing a different algorithm.
- **Context:** holds a reference to a Strategy and delegates the work to it, without knowing which concrete class it is.

```java
interface PaymentStrategy { void pay(double amount); }

class CardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Paid " + amount + " by card"); }
}
class WechatPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Paid " + amount + " by WeChat"); }
}

class Checkout {                                   // Context
    private PaymentStrategy strategy;
    void setStrategy(PaymentStrategy s) { strategy = s; }
    void checkout(double amount) { strategy.pay(amount); }
}

Checkout c = new Checkout();
c.setStrategy(new CardPayment());
c.checkout(100);
c.setStrategy(new WechatPayment());   // swapped at runtime
c.checkout(100);
```

**Benefit of swapping at runtime:** behavior can change based on user choice or conditions without if-else chains or modifying the Context. New algorithms are added as new classes (following OCP), and each strategy can be tested independently.



## Q5. Observer Pattern

**One-to-many relationship:** One Subject keeps a list of many Observers. Observers can subscribe or unsubscribe at any time. When the Subject's state changes, it notifies every registered Observer by calling their `update()` method.

```java
interface Observer { void update(String news); }

class Subject {
    private List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o)   { observers.add(o); }
    void unsubscribe(Observer o) { observers.remove(o); }
    void notifyObservers(String news) {
        for (Observer o : observers) o.update(news);
    }
}
```

**Why adding a new observer type doesn't change the Subject:** The Subject only depends on the `Observer` interface, not on concrete classes. To add a new type (e.g. `SmsObserver`), we just create a class that implements `Observer` and subscribe it. The Subject's code stays the same, which keeps the two loosely coupled and follows OCP.


## Q6. Decorator Pattern

**Problem (subclass explosion):** To add optional features with inheritance, we need a subclass for every combination. For a coffee with milk, sugar, and cream, that means `MilkCoffee`, `SugarCoffee`, `MilkSugarCoffee`, ... With n options there are up to 2ⁿ − 1 subclasses, and things like "double milk" can't be expressed at all.

**Solution:** A Decorator implements the same interface as the object it wraps, holds a reference to it, and adds behavior before or after delegating to it. Each feature becomes one class, and features are combined at runtime by wrapping.

```java
class Milk implements Coffee {
    private final Coffee inner;
    Milk(Coffee c) { inner = c; }
    public double cost() { return inner.cost() + 0.5; }
}

Coffee c = new Sugar(new Milk(new SimpleCoffee()));
```

**Real-world Java example:** `java.io` streams.

```java
BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream("data.txt")));
```

`InputStreamReader` adds byte-to-character conversion, and `BufferedReader` adds buffering and `readLine()`, each wrapping the layer inside it.



## Q7. Proxy vs. Decorator

Both patterns implement the same interface as the object they wrap and forward calls to it, so their structure is nearly identical. The difference is **intent**:

- **Decorator adds behavior.** It gives the object new responsibilities, and multiple decorators can be stacked.
- **Proxy controls access.** It does not add new features; it decides whether, when, or how the real object is accessed, for example lazy loading, permission checks, caching, or remote calls.

**In short:** Decorator = "do more"; Proxy = "control access".



## Q8. Adapter vs. Decorator vs. Proxy

| Pattern       |                (a) Interface                       |           (b) Primary intent                |
|---------------|----------------------------------------------------|---------------------------------------------|
| **Adapter**   | **Different**: converts one interface into another | Make incompatible interfaces work together  |
| **Decorator** | **Same** as the wrapped object                     | Add new behavior; decorators can be stacked |
| **Proxy**     | **Same** as the wrapped object                     | Control access (lazy loading, permissions, caching, remote) |

**Examples:**
- **Adapter:** `InputStreamReader` adapts an `InputStream` (bytes) to a `Reader` (characters).
- **Decorator:** `BufferedReader` wraps a `Reader` and adds buffering and `readLine()`.
- **Proxy:** `ImageProxy` delays creating the real `Image` until `display()` is first called.

**In short:** Adapter changes the interface; Decorator keeps it and adds behavior; Proxy keeps it and controls access.



## Q9. Thread-Safe Singleton with Double-Checked Locking

**Why the naive version is not thread-safe:**

```java
public static Singleton getInstance() {
    if (instance == null) {
        instance = new Singleton();
    }
    return instance;
}
```

"Check" and "create" are two separate steps. Two threads can both see `instance == null` before either assigns it, so both create an instance and the class is no longer a singleton (a check-then-act race condition). Making the whole method `synchronized` fixes this but forces every call to acquire a lock, even after the instance exists.

**Double-Checked Locking with `volatile`:**

```java
class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {                    // 1st check, no lock
            synchronized (Singleton.class) {
                if (instance == null) {            // 2nd check, with lock
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

- **First check:** once the instance exists, threads return immediately without locking.
- **Second check:** a thread that waited for the lock re-checks, so only one instance is created.
- **`volatile`:** `new Singleton()` is not atomic (allocate memory, run constructor, assign reference), and the JVM may reorder it so the reference is assigned before construction finishes. Another thread could then pass the first check and use a half-initialized object. `volatile` prevents this reordering and guarantees visibility, so any thread that sees a non-null `instance` sees a fully constructed object.



## Q10. Choosing the Right Pattern

- **(a) Singleton:** ensures a class has exactly one instance and provides a global access point to it.
- **(b) Strategy:** encapsulates interchangeable algorithms behind a common interface, so the Context can swap them at runtime.
- **(c) Proxy (Virtual Proxy):** the proxy implements the same interface as the real object and creates the expensive object only when it is first used, so the calling code does not change.



## Q11 & Q12
See code in `src/ FactoryPatternExample/` and `src/PizzaBuilderExample/`.
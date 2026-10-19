# HW7 – Java Design Patterns

## Question 1.

The `instanceof`-based version has to be **modified every time a new shape is added**: each new shape needs another
`else if` in the same method, which is risky and error-prone. In the interface-based refactor, every shape implements
`Shape.calculateArea()`, so a new shape such as `Triangle` is added by writing a **new class**, with **zero changes to
existing code**. The Open/Closed Principle says software should be **open for extension, closed for modification**: the
refactored version extends by adding a class, while the `instanceof` version has to edit an existing method.

---

## Question 2.

**Factory Method** creates **one product**: it delegates creation to a factory method (often overridden by subclasses),
e.g. `create(type)` returns one `PaymentProcessor`. **Abstract Factory** is "a factory of factories": an interface that
groups multiple related factory methods and creates a **family of related products** that must be used together, e.g.
`UIFactory` returns a matching `Button` and `Checkbox`, so a Mac factory always gives a Mac button with a Mac checkbox.

---

## Question 3.

A constructor with many parameters is hard to read and error-prone, e.g.
`new Pizza(12, true, false, true, "thin", 2, false)`: which parameter is which? Arguments of the same type are easy to
swap by mistake, and the code still compiles.

**Builder** constructs the object step by step with readable, named methods. Required parameters go in the Builder's
constructor, optional parameters have default values and are set by chainable methods that return `this`, and `build()`
creates the final object through a private constructor:

```java
Pizza pizza = new Pizza.Builder(12)
    .cheese(true)
    .pepperoni(true)
    .crust("thin")
    .build();
```

Benefits: readable construction, the order doesn't matter, an immutable object (`final` fields set once in `build()`),
no many overloaded constructors, and optional parameters can be added later without breaking existing calls.

---

## Question 4.

The Strategy pattern defines a family of interchangeable algorithms, each in its own class, and selects one at runtime.

- **Strategy interface** (`DiscountStrategy`): declares the common method of all algorithms (`applyDiscount`).
- **Concrete strategies** (`PercentageDiscount`, `FixedAmountDiscount`, `NoDiscount`): each implements one algorithm.
- **Context** (`PriceCalculator`): holds a reference to the current strategy and delegates the work to it.

**Benefit of swapping at runtime:** the context just calls whichever strategy is plugged in, so the behavior changes by
swapping the object with `setStrategy(...)`, without editing the context. This removes `if/else` chains, adding a new
algorithm only needs a new class (no existing code touched, so it follows OCP), and each strategy can be tested
independently.

---

## Question 5.

Observer defines a **one-to-many** dependency: one **Subject** keeps a list of many **Observers** (`subscribe()` /
`unsubscribe()`), and when its state changes it loops over the list and calls `onUpdate()` on every observer.

Adding a new type of observer does not change the Subject because the Subject only knows the **`Observer` interface**,
not what each observer does. A new observer (e.g. `PushNotifier`) just implements `Observer` and calls `subscribe()`;
`OrderService` never changes.

---

## Question 6.

Decorator adds responsibilities to an object **dynamically, without modifying its class or creating a subclass for every
combination of features**. With inheritance we get a subclass explosion: `CoffeeWithMilk`, `CoffeeWithSugar`,
`CoffeeWithMilkAndSugar`, `CoffeeWithMilkAndSugarAndWhip`, ... which gets out of hand fast.

With Decorator, an object is wrapped by another object that **implements the same interface** and adds behavior, so
decorators can be stacked in any combination:

```java
Coffee coffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
coffee.getDescription();   // "Coffee + Milk + Sugar"
coffee.getCost();          // 2.7
```

**Real-world Java example:** the `java.io` streams, e.g.
`new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")))`, where each class wraps and enhances the one
inside it.

---

## Question 7.

Their structure is almost identical (both wrap an object with the **same interface**), so the difference is the
**intent**. **Proxy controls access to an object** (lazy loading, access control, logging/caching, remote calls) and
often creates and manages the real object itself; the client is unaware it is talking to a proxy. **Decorator adds new
behavior/responsibilities** to an already-existing object, so optional features can be stacked.

So if the wrapper changes **what** the object does, it is a Decorator. If it controls **whether or when** the object does
it, it is a Proxy.

---

## Question 8.

| Pattern | Interface Relationship | Primary Intent |
|---|---|---|
| **Adapter** | Wraps a class with a **different** interface and converts it to a compatible one | Make incompatible things work together |
| **Decorator** | Wraps a class with the **same** interface, adds behavior | Add responsibilities dynamically |
| **Proxy** | Wraps a class with the **same** interface, controls access | Control/manage access to an object |

A different interface means Adapter. The same interface and it adds behavior means Decorator. The same interface and it
controls access means Proxy.

---

## Question 9.

A naive lazily-initialized Singleton checks `if (instance == null)` without any lock, so two threads can both see
`instance == null` and each create an instance. We end up with **two instances**, which breaks the Singleton guarantee.

**Double-Checked Locking** fixes it:

```java
private static volatile DoubleCheckedSingleton instance;

public static DoubleCheckedSingleton getInstance() {
    if (instance == null) {                    // First check (no locking)
        synchronized (DoubleCheckedSingleton.class) {
            if (instance == null) {            // Second check (with lock)
                instance = new DoubleCheckedSingleton();
            }
        }
    }
    return instance;
}
```

The first check avoids locking once the instance exists. Inside the `synchronized` block only one thread at a time runs,
and the second check makes sure that a thread which waited for the lock does not create a second instance.

`volatile` is essential: `new DoubleCheckedSingleton()` is really three steps (allocate memory, construct the object,
assign the reference to `instance`). Without `volatile`, the assignment can be reordered before the constructor
finishes, so another thread could see a non-null but partially constructed object. `volatile` forbids that reordering
(ordering) and makes the write visible to all threads (visibility).

---

## Question 10.

- (a) Exactly one shared instance, accessible globally: **Singleton**.
- (b) Several interchangeable algorithms, switched at runtime: **Strategy**.
- (c) Delay the creation of an expensive object until it is used, without changing the calling code: **Proxy** (virtual
  proxy / lazy loading). The proxy has the same interface as the real object, so the client code doesn't change.

**1. Factory Method vs. Abstract Factory**

The **Factory Method** pattern defines a method for creating a product, while allowing subclasses to decide which concrete product to create. For example, `CarFactory` creates a `Car`, and `BikeFactory` creates a `Bike`. Both products implement the same `Transport` interface.

The **Abstract Factory** pattern provides methods for creating a family of related or matching products. For example, a dark-theme factory creates both dark buttons and dark text fields, while a light-theme factory creates matching light versions.

The main difference is that **Factory Method focuses on one product type, while Abstract Factory focuses on a family of related product types.**

**2. Telescoping Constructors and the Builder Pattern**

Telescoping constructors are multiple overloaded constructors with increasing numbers of parameters. They become difficult to use and maintain when a class has many optional settings. Callers must remember the meaning and order of the arguments, and arguments of the same type can easily be mixed up.

For example, this call is difficult to understand:

```
new Burger("large", true, false, true);
```

The **Builder pattern** allows an object to be configured step by step using clearly named methods:

```
Burger burger = new Burger.Builder()
    .size("large")
    .cheese(true)
    .bacon(false)
    .lettuce(true)
    .build();
```

This makes the code easier to read and reduces mistakes caused by argument order. The builder can also provide defaults for optional settings and validate required values before creating the object.

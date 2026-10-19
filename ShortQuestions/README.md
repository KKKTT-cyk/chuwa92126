# Java Design Patterns Homework

## Question 1

### Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates (a single product vs. a family of matching products).

The Factory Method pattern is used to create a single type of product. It defines a method for creating an object, while subclasses decide which concrete object should be created.

For example, in a transportation system, a `CarFactory` creates a `Car`, while a `BikeFactory` creates a `Bike`. Each factory is responsible for creating one specific type of product.

The Abstract Factory pattern is used to create a family of related or matching products** without specifying their concrete classes.

For example, suppose an application supports different UI themes. A `WindowsFactory` could create a Windows-style button, checkbox, and menu, while a `MacFactory` could create matching Mac-style versions of those components.

In short:

- Factory Method: creates one product.
- Abstract Factory: creates a family of related products.

---

## Question 2

### Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly, and describe how the Builder pattern solves this problem.

Telescoping constructors happen when a class has many constructor parameters or multiple overloaded constructors for different combinations of parameters. They are difficult to use because the caller must remember the correct parameter order, and it can be hard to understand what each argument represents.

For example:

```java
User user = new User("May", 30, "Seattle", true, false, "Engineer");
```

It is not immediately clear what `true` and `false` mean, and accidentally putting arguments in the wrong order can cause bugs.

The Builder pattern solves this problem by creating an object step by step using clearly named methods. Instead of passing every value into one large constructor, the caller specifies the desired properties through a builder.

For example:

```java
User user = new User.Builder()
        .setName("May")
        .setAge(30)
        .setCity("Seattle")
        .setActive(true)
        .setAdmin(false)
        .setJob("Engineer")
        .build();
```

This approach makes the code easier to read and maintain. It also handles optional parameters more cleanly and reduces mistakes caused by confusing constructor arguments.

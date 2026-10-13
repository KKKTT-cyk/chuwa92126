# Java Design Patterns Homework

## Question 1: Factory Method vs. Abstract Factory

The main difference between Factory Method and Abstract Factory is what they create.

**Factory Method:**
- Creates a single product through a factory method.
- Subclasses decide which specific product to create.
- For example, a TransportFactory can create a Car, Bike, or Scooter.

**Abstract Factory:**
- Creates a family of related or matching products.
- Ensures that the products created together are compatible.
- For example, a GUI factory can create matching Buttons and Checkboxes for Windows or macOS.

**Conclusion:**

Factory Method focuses on creating a single product, while Abstract Factory focuses on creating a family of related products.


## Question 2: Telescoping Constructors vs. Builder Pattern

Telescoping constructors occur when a class has multiple constructors with different numbers of parameters.

For example:

    public User(String name) { ... }
    public User(String name, int age) { ... }
    public User(String name, int age, String email) { ... }

**Problems with Telescoping Constructors:**
- Difficult to manage when there are many parameters.
- Easy to pass arguments in the wrong order.
- Makes code harder to read and maintain.
- Adding optional parameters may require more constructors.

**How the Builder Pattern Solves These Problems:**

The Builder Pattern allows objects to be created step by step instead of passing all parameters into a constructor.

For example:

    User user = new User.Builder("Alice")
        .age(25)
        .email("alice@example.com")
        .build();

**Advantages of the Builder Pattern:**
- Improves code readability.
- Makes optional parameters easier to manage.
- Reduces errors caused by incorrect parameter order.
- Makes object creation more flexible.

**Conclusion:**

The Builder Pattern solves the problems of telescoping constructors by providing a more readable, flexible, and maintainable way to create objects.
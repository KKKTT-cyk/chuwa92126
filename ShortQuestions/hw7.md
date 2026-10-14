# java-design-patterns-homework_1008

1. Using the shape-area example covered in class, contrast the instanceof-based implementation with the interface-based refactor, and explain why the refactored version follows the Open/Closed Principle (OCP).

The instanceof-based implementation uses if/else statements to check different shape types, so we need to modify the existing code whenever a new shape is added. With interface-based refactor, each shape implements Shape interface and defines its own calculateArea() method. This follows the Open/Closed Principle because we can add new shapes without changing the existing code.


2. Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates (a single product vs. a family of matching products).

Factory Method is used to create a single type of product, where subclasses decide which specific object to create. Abstract Factory is used to create a family of related products that are designed to work together. For example, a Factory Method can create a Car or Bike, while an Abstract Factory can create matching UI components, such as buttons and checkboxes for Windows or Mac.


3. Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly, and describe how the Builder pattern solves this problem.
   
Constructors with many parameters are hard to use because it is difficult to remember what each parameter means and easy to pass values in the wrong order. The Builder pattern allows us to create objects step by step using clearly named methods, which makes the code easier to read, more flexible, and less error-prone.


4. Describe the roles of the Context, the Strategy interface, and the concrete Strategy implementations in the Strategy pattern, and explain the benefit of being able to swap the active strategy at runtime.

In Strategy pattern, Context holds and uses the current strategy, while Strategy interface defines a common method for different algorithms. Concrete Strategy classes provide the actual implementations. This allows the program to switch between different algorithms at runtime without changing the Context's code, making the system more flexible and easier to extend.


5. Describe the one-to-many relationship between Subject and Observer in the Observer pattern, and explain why adding a new type of observer does not require changing the Subject's code.

In Observer pattern, one subject can have multiple observers subscribed to it. When the subject's state changes, it notifies all its registered observers. Since subject communicates with observers through a common interface, we can add new types of observers without modifying Subject's code.


6. Explain what problem the Decorator pattern solves (referencing the "subclass explosion" example), and give one real-world Java example that follows this pattern.

Decorator pattern allows new behaviors to be added to objects without creating a separate subclass for every combination of features. For example, instead of creating different Coffee subclasses for milk, sugar, and other toppings, we can wrap a Coffee object with different decorators. A real-world Java example is the java.io stream classes, such as BufferedReader, which wraps another Reader to add buffering functionality.


7. Explain the core difference in intent between Proxy and Decorator, even though the two patterns have nearly identical structure.

Proxy focuses on controlling access to an object, such as lazy loading, caching, or access control. 
Decorator focuses on adding new behaviors or responsibilities to an object dynamically without modifying its original class.


8. Explain the difference between Adapter, Decorator, and Proxy, based on (a) whether the interface being wrapped is the same or different, and (b) what each pattern's primary intent is.

Adapter wraps an object with a different interface and converts it into the interface expected by the client, allowing incompatible components to work together. 
Decorator keeps the same interface and adds new behaviors or responsibilities to an object. 
Proxy also keeps the same interface, but its main purpose is to control access to the original object, such as through lazy loading or caching.


9.  Explain why a naive lazily-initialized Singleton is not thread-safe, and explain how Double-Checked Locking combined with volatile fixes this.

A naive lazy Singleton is not thread-safe because multiple threads may check that the instance is null at the same time and create separate objects. 
Double-checked Locking solves this by checking the instance twice and using synchronization to ensure that only one thread creates the object. The `volatile` ensures that changes to the instance are visible across threads and prevents unsafe instruction reordering during object initialization.


10.  For each of the following situations, name the design pattern that best fits it: (a) you need exactly one shared instance of a class, accessible globally; (b) you have several interchangeable algorithms and need to switch between them at runtime; (c) you need to delay the creation of an expensive object until it is actually used, without changing the calling code.

(a)Singleton
(b)Strategy
(c)Proxy
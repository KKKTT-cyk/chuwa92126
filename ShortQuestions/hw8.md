## Question1:
The Open/Closed Principle (OCP) states that software components should be open for extension but closed for modification. This means developers should be able to add new functionality without modifying existing, working code.
In the original instanceof-based implementation, the calculateArea() method uses conditional statements to determine whether the shape is a Rectangle or Circle. Whenever a new shape, such as a Triangle, is introduced, developers must modify the existing method by adding another conditional branch. This violates OCP because adding functionality requires changing existing code, increasing the risk of introducing errors.
In contrast, the interface-based implementation defines a Shape interface containing a calculateArea() method. Each concrete shape class implements this interface and provides its own area calculation.

## Question2 and Question3 have been answered in hw7.

## Question4:
Context: The class that uses a strategy to perform an operation. It maintains a reference to a Strategy object and delegates the operation to that strategy.
Strategy Interface: Defines a common contract that all supported algorithms must implement.
Concrete Strategies: Implement the Strategy interface and provide specific algorithms or behaviors.

The Context can switch between these payment strategies at runtime without modifying its own implementation.
The primary benefit is flexibility and loose coupling. Developers can add new algorithms without changing the Context, reduce complex conditional statements, and select the most appropriate algorithm based on runtime requirements.

## Question5:
The Subject maintains a collection of Observers and provides methods to register, remove, and notify them. Each Observer implements a common interface, typically containing an update() method.

Adding a new Observer, such as an email notification service, does not require modifying the Subject because the Subject interacts with Observers through their shared interface rather than depending on specific implementations.

## Question6:
The Decorator pattern is a structural design pattern that allows additional functionality to be attached to an object dynamically without modifying its original class.
It addresses the problem of subclass explosion, which occurs when inheritance produces too many subclasses to represent different combinations of features.

A real-world Java example is the Java I/O API:
InputStream input = new BufferedInputStream(
new FileInputStream("data.txt")
);
Here, FileInputStream provides basic file-reading functionality, while BufferedInputStream decorates it by adding buffering capabilities.

## Question7:
The Proxy pattern focuses on controlling access to an object. It acts as a substitute or representative for the original object and may manage lazy initialization, access permissions, caching, or remote communication.
For example, a virtual proxy can delay loading a large image until it is actually needed, improving application startup performance.
The Decorator pattern, on the other hand, focuses on dynamically adding new functionality to an object without modifying its existing implementation.
For example, BufferedInputStream adds buffering functionality to an existing input stream.
The key distinction is that Proxy controls how an object is accessed, while Decorator enhances what the object can do.

## Question8:
Pattern	|Interface Relationship	|Primary Intent
Adapter	|Converts a different or incompatible interface into the expected interface.|	Enable incompatible components to work together.
Decorator	|Implements the same interface as the wrapped object.	|Add functionality dynamically.
Proxy	|Implements the same interface as the real object.	|Control access to the real object.

## Question9:
9.1 
A naive lazily-initialized Singleton creates its instance only when getInstance() is first called. However, this implementation is not thread-safe because multiple threads may simultaneously check whether the instance is null.
For example:
public static Singleton getInstance() {
if (instance == null) {
instance = new Singleton();
}
return instance;
}
If two threads both observe that instance is null, they may each create a separate object, violating the Singleton pattern's requirement that only one instance exists.

9.2
Double-Checked Locking solves this issue by checking whether the instance is null both before and after acquiring a synchronization lock.
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
The first null check avoids unnecessary synchronization after the instance has been initialized. The second null check ensures that only one thread creates the instance, even if multiple threads previously passed the first check.
The volatile keyword provides visibility and ordering guarantees, ensuring that other threads see a fully initialized Singleton object. In particular, it prevents unsafe publication caused by reordering of initialization-related operations.

## Question10:
(a)Singleton Pattern. (b)Strategy Pattern  (c)Proxy Pattern

## Java Design Patterns Homework

### Dengtai Wang

#### Conceptual Questions

1. **Using the shape-area example covered in class, contrast the instanceof-based implementation with**
   **the interface-based refactor, and explain why the refactored version follows the Open/Closed Principle**
   **(OCP)**

```java
interface Shape {
    double getArea();
}

class Rectangle implements Shape {
    private double width;
    private double height;

    @Override
    public double getArea() {
        return width * height;
    }
}

class Circle implements Shape {
    private double radius;

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}
```

​	In the instanceof implementation, when a new shape is added, the existing method must be modified with another instanceof branch. In the interface-based version, each shape implements a common Shape interface and provides its own getArea() method. The caller only invokes shape.getArea() and does not need to know the concrete type. 

2. **Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates**
   **(a single product vs. a family of matching products).**

   ​	Factory method creates a single product through a common interface, with concrete implementations deciding which product to return. For example, CreditcardPayment and ApplePayPayment will both implement PaymentProcessor Interfece. When we need to use payment method, PaymentFactory will return the corresponding Payment class to process the payment.

   ​	Abstract Factory creates a family of related, matching products through multiple product interfaces. For example, if we need both pay and refund function for each payment method, we can use Abstract Facotry Method, an CreditcardFactory creates both payment and refund for credit card, so the components belong to the same provider.

   ​	Single product means one product type, a family of matching products is a group of different product types designed to work together or follow the same provider.

3. **Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly,**
   **and describe how the Builder pattern solves this problem.**

   For Telescoping constructors, they have too many parameters and callers must remember parameter orderes epecially when several parameters share the same type. Like some consequential boolean type:	

   ```java
   new User("A", true, true, false, true)
     // Can be explained as A user Name is A, and has verified email, enabled email notification, disabled text notification, enabled mail notification.
   ```

   ​	Builder pattern replaces a long argument list with named steps

   ```java
   User user = new User.Builder("A") 
     	.verifiedEmail(true)
       .emailNotifications(true)
       .smsNotifications(false)
     	.mailNotifications(true)
       .build();
   ```

   This makes each value’s purpose clear, lets callers omit optional settings and use defaults, and allows build() to validate the configuration before creating the object. The tradeoff is additional builder code, so it is most useful for objects with many optional parameters.

4. **Describe the roles of the Context, the Strategy interface, and the concrete Strategy implementations in**
   **the Strategy pattern, and explain the benefit of being able to swap the active strategy at runtime.**

   ​	Strategy interface: defines a common operation that all strategies must implement.
   ​	Concrete Strategy classes: provide different implementations of that operation.
   ​	Context: uses a Strategy object to perform the behavior instead of implementing the behavior itself.

   ​	Because the Context works with the interface instead of a specific implementation, the active strategy can be replaced at runtime. This allows the program to change behavior dynamically without modifying the Context and avoids large conditional statements. It also makes the code easier to extend, test, and maintain.

5. **Describe the one-to-many relationship between Subject and Observer in the Observer pattern, and**
   **explain why adding a new type of observer does not require changing the Subject's code**

   ​	In the Observer pattern, one Subject can have many Observers registered with it. When the Subject changes, it notifies every registered Observer through a common Observer interface. The Subject depends only on the interface and does not depend on specific observer classes. Therefore, a new observer type can be added by implementing the Observer interface without modifying the Subject code. This reduces coupling and also follows the Open/Closed Principle because the system can be extended with new observers without changing existing code.

6. **Explain what problem the Decorator pattern solves (referencing the "subclass explosion" example), and**
   **give one real-world Java example that follows this pattern.**

   ​	The Decorator pattern solves the subclass explosion problem that occurs when many combinations of optional features would require separate subclasses. Instead of creating a subclass for every combination, decorators wrap an existing object and add behavior dynamically. This makes the design more flexible and avoids a large inheritance hierarchy. A real-world Java example is the I/O stream API, like wrapping a FileInputStream inside a BufferedInputStream to add buffering behavior.

7. **Explain the core difference in intent between Proxy and Decorator, even though the two patterns have**
   **nearly identical structure.**

   ​	The Decorator pattern is used to dynamically add new behavior or responsibilities to an object, but the Proxy pattern is used to control access to the real object, such as for security, lazy loading, caching, or remote access.

8. **Explain the difference between Adapter, Decorator, and Proxy, based on (a) whether the interface being**
   **wrapped is the same or different, and (b) what each pattern's primary intent is.**

   ​	Adapter, Decorator, and Proxy are all wrap another object, but their intent is different. Adapter usually wraps an object with a different interface and converts it into the interface expected by the client. Decorator usually keeps the same interface and adds new behavior dynamically. Proxy also keeps the same interface, but its main purpose is to control access to the real object, such as through security checks, lazy loading, caching, or remote access.

9. **Explain why a naive lazily-initialized Singleton is not thread-safe, and explain how Double-Checked**
   **Locking combined with volatile fixes this.**

   ​	A naive lazy singleton is not thread-safe because two threads may both see "instance == null" and create separate objects at the same time. Double-Checked Locking fixes this by synchronizing only during initialization and checking instance again inside the synchronized block. The volatile keyword is required to ensure that all threads see the latest instance reference and to prevent instruction reordering that could expose a partially initialized object.

10. **For each of the following situations, name the design pattern that best fits it: (a) you need exactly one**
    **shared instance of a class, accessible globally; (b) you have several interchangeable algorithms and**
    **need to switch between them at runtime; (c) you need to delay the creation of an expensive object until**
    **it is actually used, without changing the calling code.**

    ​	a is Singleton, b is Strategy, c is Proxy.
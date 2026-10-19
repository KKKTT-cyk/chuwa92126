# Q1.
The open close principle states that software entitles should be open for extension but closed for modification.
When requirements change, you should be able to add new behavior by writing new code, not by changing existing, testing code.
The second design replaces type checking conditionals with an abstraction and polymorphism, so new shapes can be added without modifying existing code.

# Q2.
Factory method creates one product, a method decides which concrete class of that single product to instantiate. Abstract factory is factory of factory method, it creates a family of related product types.

# Q3.
When a class has contractor with many parameters it's hard to read and know the meaning of each parameter, and the parameters with same type are easy to swap, and cannot skip a parameter in the middle and may cause lots of overloading method.
use a builder we can know meaning of every value, it's labeled by method name, and each option has its own method, order doesnt matter, and only call method we need.

# Q4.
Context:It holds a reference to a strategy, delegates the varying part of its work to it, decides when to do and with which strategy.  
Strategy interface: declare the operation that every algorithm must provide it. context relies on it.  
Concrete Strategy implementations: each class implements the interfaces with one specific algorithm.

# Q5. 
One subject with many observers, observers subject to a subject when the state changes, all registered observers are notified.  Observers are independent to each other, then there is a notification they just do their work dont need to care about what others are doing.

# Q6.
Decorator can add responsibilities to an object dynamically, without modifying its class or creating explosion of subclasses for every combination. Decorators can be stacked, more flexible.

# Q7.
Both patterns warp an object behind the same interface and forward calls to it, so the class diagrams look almost the same.
Decorator adds behaviors, it enhances or extends what the object does. while proxy controls access, it stands in for the real object and manages whether when or how the client reaches it.

# Q8.
Adapter wraps a class with a different interface, converts it to a compatible one, while Decorator wraps a class with same interface, add behavior, and Proxy wraps a class with same interface, controls access.
Adapter makes incompatible things work together and Decorator adds responsibilities dynamically and proxy control access to an object.

# Q9.
Because multiple threads can see a null instance at same time and then they both will create instance.
```java
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
```
Once the instance exists, almost every call return here immediately without touching the lock, this is the fast path,
If thread A and B both pass first check, A enters the lock and create the instance, and when B gets the lock afterward, it rechecks, sees the instance is no longer null, and doesnt create a second one.

# Q10.
singleton   
strategy  
proxy
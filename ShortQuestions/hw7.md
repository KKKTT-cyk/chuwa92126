### Question 1

The instance-based version requires changing the method calculateArea every time a new shape is added, which violates the "Closed for modification" principle in Open/Closed Principle. While the interface version follows the principle by enabling adding a new class without changing existing code but through adding a class to implement the interface and the method to calculate the area of the newly added shape.

### Question 2

Factory Method is used to create a single product by deferring creation of the product to subclasses overriding a method. While Abstract Factory is "a factory of factories" which is a pattern used to create families of related objects that must be used together by grouping multiple related Factory Methods into one interface.

### Question 3

Constructors with many parameters is error prone in that: 1. Different parameters with the same type can be inputted in a swapped way incorrectly while the compiler can't catch the error. The code then compiles and run in the wrong way silently; 2. Positional arguments are unreadable and the caller or reviewer has to look up the constructor signature to know which parameter means what; 3. Overload explosion issue brought by each new optional field, which means more constructors and more call-site changes. Also, to set a late optional parameter, the caller must pass every earlier one.

Builders solve these problems by moving construction into a separate builder object. Required fields go in the builder's constructor, optional ones are set via named, chainable methods, and build() creates the object. This makes construction readable with immutable result and avoids half-built object by validating invariants in one place. It is also extensible in that a new option just adds one new builder method with no impact on existing callers.

### Question 4

Strategy pattern defines a family of interchangeable algorithms, encapsulates each one and lets the caller select which one to use at runtime:

- Strategy Interface is the common contract which defines what is done, wo all algorithms are interchangeable.

- Concrete Strategies means each class implements one algorithm, each independently testable with a single responsibility.

- Context holds a reference to the Strategy Interface and delegates to it. It doesn't know which concrete strategy it has and it has a setStrategy() method for swapping.

Benefits of runtime swapping:

- Behavior can change based on runtime conditions without recreating the object.

- It removes if/else chains from the context.

- It follows the Open/Closed Principle that adding a new algorithm is enabled by adding a new class without changing the existing code.

- It makes testing easier since each strategy is tested in isolation the the Context can use a mock.

### Question 5

Observer pattern defines a one-to-many dependency: when the Subject changes state, all its registered Observers are notified automatically.

One-to-many: The Subject keeps a List<Observer>. A single state change loops over that list and calls onUpdate() on each observer. Observers and subscribe and unsubscribe at runtime.

The reason why a new observer needs no Subject change is that the Subject depends only on the Observer interface, never on concrete classes. Registration happens outside the Subject via subscribe(). And Polymorphism dispatches onUpdate() to whatever implementation is in the list.

### Question 6

The problem that Decorator pattern solves is subclass explosion. When features can be combined freely, using inheritance forces a subclass for every combination and the class count grows exponentially. The logic is duplicated and behavior is fixed at compile time.

The Decorator pattern wraps an object in another object that implements the same interface, adding behavior before or after delegating to the wrapped object. Because the type match, decorators can be stacked in any combination at runtime. In this way n features need only n decorator class, and adding a new one is a single class with no changes to existing code (following Open/Closed Principle and composition over inheritance).

A real-word Java example is java.io streams:
`new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")))`

Each layer wraps the one inside it and adds behavior (decoding, buffering).

### Question 7

Both decorator and Proxy patterns wrap an object behind the same interface and delegate to it, so their structure is nearly identical. The core difference is intent:

- Decorator: add behavior. It enhances the object with new responsibilities (cost, buffering, compression). The client knowingly stacks decorators in any combination and they usually wrap an already-existing object.

- Proxy: control access. It stands in for the real object and manages how or when it is reached: lazy loading, permission checks, caching, logging, remote calls. The client is unaware of it, and the proxy often creates and manages the real object's lifecycle.

So if the wrapper changes what the object does, it's a Decorator. If it controls whether or when the object does it, it's a Proxy.

### Question 8

All Adapter, Decorator and Proxy patterns wrap another object and delegate to it. But they differ in interface and intent:

| Pattern       | Interface                                                                         | Intent                                                                  |
| ------------- | --------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **Adapter**   | Different: converts the wrapped class's interface into the one the client expects | Make incompatible things work together                                  |
| **Decorator** | Same                                                                              | Add responsibilities/behavior dynamically                               |
| **Proxy**     | Same                                                                              | Control access to the object (lazy load, permissions, caching, logging) |

---

So different interface means Adapter. Same interface and it adds behavior means Decorator. Same interface and it gates access means Proxy.

### Question 9

The naive lazy Singleton is not thread-safe because `if (instance == null) instance = new Singleton();` is a non-atomic check-then-act. Two threads can both see null and each create an instance, creating two objects and the Singleton guarantee is broken.

The double-checked locking with volatile fixes the issue by:

- First check `if (instance == null)`: avoids synchronization cost once the instance exists.

- `synchronized` + second check: only one thread creates the instance, and a thread that waited for the lock re-checks before creating.

- `volatile` is essential: new Singleton() is a process of alllocate memory, construct, and assign. Without volatile, the assign can be reordered before the constructor finishes. Another thread could then see a non-null but partially constructed object. volatile forbids that reordering (ordering) and makes the write visible to all threads (visibility).

### Question 10

- (a) Singleton: a class has only one instance and provides a global access point.

- (b) Strategy: puts each algorithm in its own class behind a common interface, and the Context holds a reference to the current one which can be swapped.

- (c) Proxy: implements the same interface as the real object so the client code is unchanged and it controls access, hereby deferring creation (lazy loading) until the first real use.

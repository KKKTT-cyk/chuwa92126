# Java Advanced Syntax Homework

## Question 1: Dynamic Proxy

`java.lang.reflect.Proxy` creates a runtime-generated class that implements the interfaces supplied to `Proxy.newProxyInstance(classLoader, interfaces, handler)`. The resulting object implements those interfaces, and its interface method calls are dispatched to the `InvocationHandler`.

For each intercepted method call, `invoke(Object proxy, Method method, Object[] args)` receives the proxy instance, a reflection `Method` describing the invoked method, and the arguments (`null` when there are none). The handler can inspect arguments, add behavior, and delegate to a target using reflection.

JDK dynamic proxies implement interfaces rather than subclassing arbitrary concrete classes. The generated proxy class is based on the supplied interface contracts, so a class without a suitable interface cannot be proxied this way.

When delegation uses `Method.invoke`, an exception thrown by the target is wrapped in `InvocationTargetException`. The handler should throw `e.getCause()` so the caller receives the original target exception rather than the reflection wrapper. This also preserves the proxy method's normal checked-exception handling rules.

## Question 2: Class Loading

Java class processing consists of three phases:

1. Loading: Find the binary representation of a class and create its `Class` object.
2. Linking: Verify bytecode, prepare static fields with default values, and resolve symbolic references (resolution may be lazy).
3. Initialization: Execute static field initializers and static initializer blocks in textual order, after required superclass initialization.

Class initialization is triggered by active use, including creating an instance with `new`, invoking a static method, reading or assigning a non-constant static field, and certain reflective operations such as `Class.forName` with initialization enabled. Initialization also occurs for the main class when an application starts. Reading a compile-time constant generally does not initialize its declaring class.

The traditional built-in class loaders are bootstrap (core Java classes), platform (platform modules), and application/system (application classpath/module-path classes). Under parent delegation, a loader first asks its parent to load a class before attempting to load it itself. This avoids duplicate definitions and helps protect core Java classes from being replaced by application-provided versions.

## Question 3: Java Serialization

- `serialVersionUID`: A version identifier used during deserialization to check compatibility between the serialized class descriptor and the local class. An incompatible identifier normally causes `InvalidClassException`. Explicitly declaring it helps manage versioning.
- `transient`: Marks an instance field to be skipped by Java's default serialization mechanism. After default deserialization, it has its default value unless restored through custom logic. It is not a universal security boundary against custom serialization behavior.
- `readResolve`: A method that can substitute another object for the newly deserialized object, commonly used to preserve singleton or canonical-instance semantics.

Security: Deserializing untrusted bytes with `ObjectInputStream` is dangerous because attacker-controlled object graphs can trigger code in `readObject`, `readResolve`, and other gadget methods, potentially causing remote code execution, denial of service, or other harmful side effects. Prefer safer data formats and strict validation; where Java serialization is unavoidable, use `ObjectInputFilter` allowlists and limits, and never treat filtering as a complete substitute for a trusted data boundary.

# HW15


## 1. Java Dynamic Proxy

`Proxy.newProxyInstance()` generates a proxy class at runtime that implements the specified interfaces and returns a proxy instance.

On each method call, the `InvocationHandler` receives:
- `proxy`: the proxy object.
- `method`: a `Method` object representing the invoked method.
- `args`: the method arguments, or `null` if there are no arguments.

JDK dynamic proxies work with interfaces because the generated proxy class implements the specified interfaces rather than extending arbitrary concrete classes.

When the handler invokes the target using `Method.invoke()`, exceptions thrown by the target are wrapped in `InvocationTargetException`. The handler should unwrap and rethrow `getCause()` so callers receive the original exception rather than the reflection wrapper.



## 2. Class Loading and Parent Delegation

Class loading has three phases:
1. **Loading:** Reads class bytecode and creates a `Class` object.
2. **Linking:** Verifies bytecode, prepares static fields, and resolves symbolic references.
3. **Initialization:** Executes static field initializers and `static` blocks.

Initialization is triggered by active uses such as creating an instance, calling a static method, accessing a non-constant static field, or certain reflective operations.

The three built-in class loaders are **Bootstrap**, **Platform**, and **Application**. Under parent delegation, a loader asks its parent to load a class first. This helps protect core Java classes and avoid duplicate loading.



## 3. Java Serialization

- **`serialVersionUID`** identifies a serializable class version and helps detect incompatible versions during deserialization.
- **`transient`** marks a field to be excluded from default serialization.
- **`readResolve`** allows an object to be replaced with another object after deserialization, often to preserve singleton instances.

Deserializing untrusted data is dangerous because malicious input may trigger unsafe deserialization logic, potentially causing arbitrary code execution or denial of service. Avoid deserializing untrusted data or use strict validation and safer formats.
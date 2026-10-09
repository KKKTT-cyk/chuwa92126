# Question 1. What is the difference between method overloading and method overriding? In which type of polymorphism does each belong?
1. **Method Overloading represents Compile-Time (Static) Polymorphism.**
    - It occurs when methods share the same name but have **different parameter lists** (parameter types, number, or order). These methods may be declared in the same class or inherited from a superclass.
    - The compiler selects the applicable overload at compile time based on the available methods and the number and compile-time types of the arguments. A classic example is a `Calculator` class with `add(int, int)` and `add(double, double)`.

2. **Method Overriding represents Runtime (Dynamic) Polymorphism.**
    - It occurs when a subclass provides a specific implementation of an inherited instance method with the **same signature** and a compatible return type (the same type or a covariant reference return type). A class can also override an interface method.
    - The resolution happens at runtime via dynamic dispatch based on the **actual object type in memory**, not the reference type—like calling `.speak()` on an `Animal dog = new Dog();` reference.
- Overloading supports different inputs under the same method name, while overriding enables custom subtype behavior under a common contract.

# Question 2. What will be the output of the following code? Explain why.
```java
class Animal {
	public void makeSound() {
		System.out.println("Some sound");
	}
}
class Dog extends Animal {
	@Override
	public void makeSound() {
		System.out.println("Bark!");
	}
}
public class Test {
	public static void main(String[] args) {
		Animal a = new Dog();
		a.makeSound();
	}
}
```

- **Output: "Bark!"**
- In `main`: `Animal a = new Dog();`
    - The reference type is `Animal`, so the compiler checks that `makeSound()` exists in the `Animal` class.
    - The actual runtime object allocated on the heap is an instance of `Dog`.
- This demonstrates runtime polymorphism (dynamic method dispatch). Even though the reference variable a is of type `Animal`, the underlying object created in memory is of type `Dog`. At runtime, Java binds virtual method calls to the actual object type, invoking the overridden `makeSound()` implementation in `Dog`.


# Question 3. Why does Java NOT support multiple inheritance with classes? What is the "Diamond Problem"? How does Java solve this issue?
The **Diamond Problem** refers to the ambiguity that arises when a child class inherits from two parent classes that both derive from a single common grandparent class.
```
	   Class A
      /       \
  Class B   Class C
      \       /
       Class D  (extends B, C)
```
If both `B` and `C` override a method `display()` inherited from `A`, calling `d.display()` creates an ambiguity: which parent's implementation should it execute—`B`'s or `C`'s? On top of that, inheriting multiple class states introduces issues with duplicate instance fields and messy constructor chaining.

- **Prior to Java 8 (Pure Contracts):** Java allowed classes to implement multiple **interfaces** instead of multiple classes. Interface methods were abstract and had no implementation bodies, so there were no competing default method implementations. Interfaces could contain constants, but no instance state. A concrete implementing class had to provide or inherit implementations satisfying all compatible abstract method declarations.

- **Java 8 and Later (Default Methods):** When `default` methods were introduced in interfaces, classes could inherit competing method implementations from multiple interfaces. Java solved this at **compile time** using strict precedence rules:

    - **Classes win:** An inherited public instance method from a superclass takes precedence over an interface default method with the same signature.
    - **Sub-interfaces win:** A default method declared in a more specific subinterface takes precedence over the corresponding default method in its parent interface.
    - **Explicit disambiguation:** If two unrelated interfaces provide conflicting default methods and no superclass method resolves the conflict, the implementing concrete class must override the method. It can provide its own implementation or delegate to an eligible direct superinterface using `InterfaceName.super.method()`; delegation is optional.

# Question 4. What will happen when you try to compile and run the following code? Explain your answer.
```java
abstract class Shape {
	public abstract double getArea();
}

public class Test {

	public static void main(String[] args) {
		Shape s = new Shape();
		System.out.println(s.getArea());

	}
}

```

The code fails to compile on the line `Shape s = new Shape();`:

```text
error: Shape is abstract; cannot be instantiated
```

There is no runtime output because compilation fails.

- **Abstract Classes Cannot Be Instantiated:**
    In Java, declaring a class with the `abstract` keyword marks it as an incomplete blueprint intended solely for extension (subclassing). Direct instantiation via `new Shape()` is strictly forbidden by the compiler.
- **Missing Implementation:**
    `Shape` declares an abstract method: `public abstract double getArea();` with no method body. If Java allowed instantiating `new Shape()`, the subsequent call `s.getArea()` would have no executable code to run, breaking runtime safety.

# Question 5. What is the difference between an abstract class and an interface? Give one scenario where you would prefer using an interface over an abstract class.

### Core Differences

|**Feature**|**Abstract Class**|**Interface**|
|---|---|---|
|**Inheritance**|Single inheritance only (`extends OneClass`)|Multiple inheritance supported (`implements A, B, C`)|
|**State / Fields**|Can have instance variables with any access modifier (`private`, `protected`, `public`) and mutable state|Fields are implicitly **`public static final`** (no instance state; a final reference can still refer to a mutable object)|
|**Constructors**|Can define constructors (invoked via `super()` in subclasses)|Cannot define constructors|
|**Method Types**|Abstract, concrete, `final`, `static`, and `private` methods|Abstract methods, `default` methods, `static` methods, and `private` helper methods (Java 9+)|
|**Access Modifiers**|Concrete methods can be `public`, `protected`, package-private, or `private`; abstract methods cannot be `private`|Methods are implicitly `public` (or explicitly `private` for internal reuse)|
|**Design Intent**|**Identity ("is-a"):** Shared core identity, base state, and code reuse across closely related classes|**Capability/Contract ("can-do"):** Defines an external contract or capability across unrelated classes|
The choice between an abstract class and an interface comes down to identity versus capability, and state versus contract:

1. **Inheritance & State:** An abstract class supports single inheritance and can maintain **mutable instance state**, define constructors, and use any access modifier. In contrast, a class can implement **multiple interfaces**, but interfaces cannot hold instance state—their fields are implicitly `public static final` and cannot be reassigned after initialization.

2. **Design Intent:** An abstract class is useful for shared state and implementation among related classes. An interface defines a contract that otherwise unrelated classes can implement. Both establish subtype relationships; "is-a" versus "can-do" is a design guideline, not an absolute language rule.

3. **Methods:** While Java 8+ introduced `default` and `static` methods in interfaces, an abstract class is appropriate when the shared abstraction needs to declare instance fields or `protected` methods. An interface default method can still work with object state indirectly by calling methods implemented by the class.

**A classic scenario where an interface is preferred:**
When you need to define a cross-cutting capability across completely unrelated classes, such as Java’s `Comparable<T>` or `Serializable`.

For example, a `User` class and a `Product` class share no common identity, and `Product` might already extend a base class like `BaseEntity`. Because Java forbids multiple class inheritance, making `Comparable` an abstract class would prevent `Product` from extending both `BaseEntity` and `Comparable`. Using an interface allows any class—regardless of where it sits in an inheritance tree—to implement `compareTo()` without disrupting its primary class hierarchy.


# Question 6. What will be the output of the following code? Explain the concept of upcasting and downcasting.
```java
class Animal {

	public void eat() {
		System.out.println("Animal eating");
	}

}

class Cat extends Animal {

	public void meow() {
		System.out.println("Meow!");
	}

}

public class Test {

	public static void main(String[] args) {

		Animal a = new Cat(); // Line 1
		a.eat(); // Line 2
		// a.meow(); // Line 3 (commented out)

		if (a instanceof Cat) {
			Cat c = (Cat) a; // Line 4
			c.meow(); // Line 5

		}

	}

}

```
output
```
Animal eating
Meow!
```

Line 1 demonstrates **upcasting**—assigning a subclass instance (`Cat`) to a superclass reference (`Animal`). Upcasting is implicit, type-safe, and foundational to polymorphism, though it restricts method calls to the methods available through the superclass type (which is why `a.meow()` would fail compilation).

Line 4 demonstrates **downcasting**—narrowing the parent reference back down to a `Cat` using an explicit `(Cat)` cast so we can invoke subtype-specific methods like `meow()`. Downcasting can throw a `ClassCastException` if the object is not an instance of the target type. An `instanceof` check is recommended when the runtime type is uncertain; it is not mandatory when the actual type is already known.

# Question 7. What are the rules for overriding the equals() method? Why must we also override hashCode() when we override equals()?
According to the `Object` contract, overriding `equals()` must satisfy five core properties:
1. **Reflexive:** An object must equal itself (`x.equals(x)`).
2. **Symmetric:** If `x.equals(y)`, then `y.equals(x)` must also be true.
3. **Transitive:** If `x` equals `y`, and `y` equals `z`, then `x` must equal `z`.
4. **Consistent:** It should consistently return the same result unless information used in the equality comparison changes.
5. **Non-nullity:** Comparing against `null` must safely return `false` without throwing an exception.

We **must override `hashCode()` whenever we override `equals()`** to maintain their contract: **if two objects are equal by `equals()`, they must produce the identical hash code.**

**In practice, if we omit `hashCode()`:** Hash-based collections such as **`HashMap` and `HashSet` may behave incorrectly**. They use hash codes to locate candidate entries and equality checks to identify matching keys or elements. The default `Object.hashCode()` is not guaranteed to be based on a memory address or to return the same value for distinct objects that our `equals()` considers equal. If equal objects have different hash codes, lookups may fail and a set may retain logically duplicate elements.

The reverse is not required: two unequal objects may have the same hash code.

# Question 8. What is the difference between shallow copy and deep copy? Given an object Person with a field Address address, explain what happens to the address field in each type of copy.
The difference between shallow copy and deep copy comes down to how **nested reference types** are duplicated in memory:

- **A Shallow Copy** creates a new outer object, but for any non-primitive fields, it copies the **reference values**. Primitive field values are copied as well. It does not create new nested objects.

    - **For `Person` and `Address`:** Both the original and the copied `Person` will point to the **exact same `Address` object** on the heap. If you change `copy.getAddress().setCity("New York")`, the original person's city also silently changes.

- **A Deep Copy** creates a new outer object **and recursively copies the mutable referenced objects** needed to keep the original and copy independent. Immutable objects, such as strings, can safely be shared.

    - **For `Person` and `Address`:** A completely separate `Address` object is instantiated on the heap for the copy. Mutating the address on the copied `Person` leaves the original untouched.


In Java, `Object.clone()` performs a shallow copy when cloning is supported; the class must implement `Cloneable`, otherwise it throws `CloneNotSupportedException`. To achieve a deep copy, we typically implement a **copy constructor**, a static factory method, or use deep cloning libraries/serialization.


# Question 9. What will be the output of the following code? Explain why.
```java
interface Flyable {

	default void takeOff() {

	System.out.println("Taking off from Flyable");

	}

}

interface Swimmable {

	default void takeOff() {
	System.out.println("Diving in from Swimmable");


	}

}

class Duck implements Flyable, Swimmable {

	@Override

	public void takeOff() {
		Flyable.super.takeOff();

	}

}

public class Test {

	public static void main(String[] args) {

		Duck d = new Duck();
		d.takeOff();

	}

}
```

The output of this code is:

```
Taking off from Flyable
```

The code works for the following reasons:

- **Step 1: The interfaces provide competing defaults.**
    Both `Flyable` and `Swimmable` declare a default method with the same signature, `takeOff()`. Inheriting both implementations without resolving the conflict would cause a compilation error. The supplied code resolves the conflict, so it compiles successfully.

- **Step 2: The compiler checks for an explicit override.**
    Java rules state that whenever a class inherits conflicting default methods from unrelated interfaces, it **must** override that method. If `Duck` did not provide its own `takeOff()`, the code would fail to compile with an _'inherits unrelated defaults'_ error.

- **Step 3: Disambiguation via `Flyable.super.takeOff()`.**
    Inside `Duck`'s `takeOff()`, Java allows explicit resolution using the syntax `<InterfaceName>.super.<methodName>()`. `Duck` explicitly delegates the call to `Flyable`'s default implementation rather than `Swimmable`'s.

- **Step 4: Runtime execution in `main`.**
    When `main` executes `d.takeOff()`, dynamic dispatch enters `Duck.takeOff()`. That method executes `Flyable.super.takeOff()`, printing: `Taking off from Flyable`.


# Question 10. Consider the following code. Which methods are valid overloads of calculate(int a, int b)? Which are NOT valid and why?

```java
public class Calculator {

	public int calculate(int a, int b) {
		return a + b;
	}

	// Method A

	public int calculate(int a, int b, int c) {
		return a + b + c;
	}

	// Method B

	public double calculate(double a, double b) {
		return a + b;
	}

	// Method C

	public double calculate(int a, int b) {
		return (double)(a + b);
	}

	// Method D

	private int calculate(int x, int y) {
		return x * y;
	}

}
```

In Java, method overloading is strictly determined by the parameter list—specifically the number, types, or order of arguments. Return types, parameter names, and access modifiers are completely ignored during signature matching.
- **Method A: `public int calculate(int a, int b, int c)`**
    - **Status:** **Valid Overload**
    - **Reason:** The parameter count is different (3 parameters vs. 2 parameters). The compiler can easily differentiate calls based on argument count.

- **Method B: `public double calculate(double a, double b)`**
    - **Status:** **Valid Overload**
    - **Reason:** The parameter types are different (`double, double` vs. `int, int`). The compiler resolves calls based on argument types.

- **Method C: `public double calculate(int a, int b)`**
    - **Status:** **INVALID**
    - **Reason:** It has the exact same parameter list `(int, int)` as the original method. Changing only the return type (`double` instead of `int`) is not valid for overloading. The return type is not part of the method signature, so these are duplicate declarations rather than distinct overloads. This produces a compile-time error: _`method calculate(int, int) is already defined`_.

- **Method D: `private int calculate(int x, int y)`**
    - **Status:** **INVALID**
    - **Reason:** Renaming parameter names (`x, y` vs. `a, b`) and changing the access modifier (`private` vs. `public`) does not change the method signature. The parameter types and count remain identical `(int, int)`. This also produces a compile-time error: _`method calculate(int, int) is already defined`_.

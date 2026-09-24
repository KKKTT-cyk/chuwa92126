# Java OOP Homework 2 --- Conceptual Questions

## Question 1

**What is the difference between method overloading and method
overriding? In which type of polymorphism does each belong?**

### Method Overloading

Method overloading means having multiple methods with the **same name
but different parameter lists**.

``` java
public int add(int a, int b) {
    return a + b;
}

public double add(double a, double b) {
    return a + b;
}
```

Java determines which method to call at compile time.

**Overloading = compile-time polymorphism.**

### Method Overriding

Method overriding happens when a subclass provides its own
implementation of a method inherited from its parent class.

``` java
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
```

The actual object determines which implementation is executed at
runtime.

**Overriding = runtime polymorphism.**

------------------------------------------------------------------------

## Question 2

**What will be the output of the code? Explain why.**

### Output

``` text
Bark!
```

### Explanation

``` java
Animal a = new Dog();
```

Here:

-   Reference type = `Animal`
-   Actual object type = `Dog`

When:

``` java
a.makeSound();
```

is executed, Java uses the overridden `makeSound()` method in `Dog`
because the actual object is a `Dog`.

This demonstrates **runtime polymorphism** (dynamic method dispatch).

------------------------------------------------------------------------

## Question 3

**Why does Java NOT support multiple inheritance with classes? What is
the Diamond Problem? How does Java solve this issue?**

Java does not support multiple inheritance with classes because it can
create ambiguity.

The **Diamond Problem** can occur when a class inherits from two parent
classes that contain the same method.

Conceptually:

``` text
      A
     / \
    B   C
     \ /
      D
```

If both `B` and `C` provide their own version of the same method, `D`
would not know which version to inherit.

Java avoids this problem by allowing a class to **extend only one
class**.

Java does allow a class to **implement multiple interfaces**. If two
interfaces contain conflicting default methods, the implementing class
must override the method and explicitly resolve the conflict.

------------------------------------------------------------------------

## Question 4

**What will happen when you try to compile and run the code?**

The program **will not compile**.

The problem is:

``` java
Shape s = new Shape();
```

`Shape` is an abstract class:

``` java
abstract class Shape
```

An abstract class **cannot be instantiated directly**.

A concrete subclass must implement the abstract methods before an object
can be created.

For example:

``` java
class Circle extends Shape {
    @Override
    public double getArea() {
        return 10.0;
    }
}
```

Then:

``` java
Shape s = new Circle();
```

is valid.

------------------------------------------------------------------------

## Question 5

**What is the difference between an abstract class and an interface?
Give one scenario where you would prefer using an interface over an
abstract class.**

### Abstract Class

An abstract class can:

-   Have instance fields
-   Have constructors
-   Have abstract methods
-   Have concrete methods
-   Maintain object state

A class can extend only **one class**.

### Interface

An interface mainly defines a **contract or capability** that a class
agrees to implement.

For example:

``` java
interface Flyable {
    void fly();
}
```

A class can implement **multiple interfaces**.

``` java
class Duck implements Flyable, Swimmable {
}
```

### When to Prefer an Interface

Use an interface when different or unrelated classes need to share the
same capability.

For example:

``` java
interface Drawable {
    void draw();
}
```

Different classes can implement `Drawable` without needing to belong to
the same class hierarchy.

------------------------------------------------------------------------

## Question 6

**What will be the output of the code? Explain upcasting and
downcasting.**

### Output

``` text
Animal eating
Meow!
```

### Upcasting

``` java
Animal a = new Cat();
```

This is **upcasting**.

A child object (`Cat`) is referenced using its parent type (`Animal`).

Upcasting is normally automatic.

The call:

``` java
a.eat();
```

prints:

``` text
Animal eating
```

This line would not compile:

``` java
a.meow();
```

because the reference type `Animal` does not define `meow()`.

### Downcasting

The code first checks:

``` java
if (a instanceof Cat)
```

and then performs:

``` java
Cat c = (Cat) a;
```

This is **downcasting**.

Now `c` is a `Cat` reference, so:

``` java
c.meow();
```

prints:

``` text
Meow!
```

Using `instanceof` before downcasting helps avoid an invalid cast and a
possible `ClassCastException`.

------------------------------------------------------------------------

## Question 7

**What are the rules for overriding `equals()`? Why must we also
override `hashCode()` when we override `equals()`?**

The `equals()` method should satisfy these properties:

1.  **Reflexive:** `x.equals(x)` is true.
2.  **Symmetric:** If `x.equals(y)` is true, then `y.equals(x)` should
    also be true.
3.  **Transitive:** If `x.equals(y)` and `y.equals(z)` are true, then
    `x.equals(z)` should be true.
4.  **Consistent:** Repeated calls should return the same result if the
    objects have not changed.
5.  **Non-null:** `x.equals(null)` should return false.

If two objects are equal according to `equals()`, they must have the
**same hash code**.

Therefore, when overriding `equals()`, we should also override
`hashCode()`.

This is especially important when objects are used in hash-based
collections such as `HashMap` and `HashSet`.

------------------------------------------------------------------------

## Question 8

**What is the difference between shallow copy and deep copy? Given a
`Person` with an `Address address` field, what happens to the address
field?**

### Shallow Copy

A shallow copy creates a new `Person` object, but both `Person` objects
reference the **same `Address` object**.

``` text
Person1 ──┐
          ↓
       Address
          ↑
Person2 ──┘
```

Therefore, changing the shared `Address` through one person can affect
what the other person sees.

### Deep Copy

A deep copy creates a new `Person` object and a **new `Address`
object**.

``` text
Person1 → Address1

Person2 → Address2
```

Changes to `Address1` do not affect `Address2`.

**Shallow copy:** nested reference objects are shared.

**Deep copy:** nested mutable objects are copied too.

------------------------------------------------------------------------

## Question 9

**What will be the output of the code? Explain why.**

### Output

``` text
Taking off from Flyable
```

Both `Flyable` and `Swimmable` define a default `takeOff()` method.

Therefore, `Duck` overrides `takeOff()` to resolve the conflict:

``` java
@Override
public void takeOff() {
    Flyable.super.takeOff();
}
```

The statement:

``` java
Flyable.super.takeOff();
```

explicitly selects the `Flyable` implementation.

Therefore, the program prints:

``` text
Taking off from Flyable
```

------------------------------------------------------------------------

## Question 10

**Which methods are valid overloads of `calculate(int a, int b)`? Which
are NOT valid and why?**

Original method:

``` java
public int calculate(int a, int b)
```

### Method A

``` java
public int calculate(int a, int b, int c)
```

**Valid overload.**

It has a different number of parameters.

### Method B

``` java
public double calculate(double a, double b)
```

**Valid overload.**

It has different parameter types.

### Method C

``` java
public double calculate(int a, int b)
```

**Not a valid overload.**

It has the same parameter list as the original method. Changing only the
return type does not create a valid overload.

### Method D

``` java
private int calculate(int x, int y)
```

**Not a valid overload.**

Changing parameter names does not change the method signature. Changing
the access modifier from `public` to `private` also does not create a
different method signature.

### Summary

  Method   Valid Overload?   Reason
  -------- ----------------- -------------------------------------------------------------
  A        Yes               Different number of parameters
  B        Yes               Different parameter types
  C        No                Only the return type is different
  D        No                Parameter names/access modifier do not change the signature

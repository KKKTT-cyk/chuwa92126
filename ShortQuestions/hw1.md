# HW1 – Java OOP (Inheritance, Polymorphism, Abstract Classes, Interfaces, Object Class)

## Question 1.

**Overloading:** multiple methods in the same class with the same name but different parameters.
**Overriding:** a subclass provides a specific implementation of a method already defined in its superclass.

| Feature | Overloading | Overriding |
|---|---|---|
| Timing | Compile-time | Runtime |
| Location | Same class | Parent-Child classes |
| Method Name | Same | Same |
| Parameters | Must be different (number, type, or order) | Must be same |
| Return Type | Can be different (but not sufficient alone) | Must be same (or covariant) |
| Access Modifier | Can be different | Cannot be more restrictive |
| private/final methods | Can be overloaded | Cannot be overridden |
| Performance | Better (early binding) | Slightly slower (late binding) |
| Purpose | Convenience, readability | Specific implementation |

- Method overloading belongs to **compile-time (static) polymorphism**.
- Method overriding belongs to **runtime (dynamic) polymorphism**.

---

## Question 2. 

```
Bark!
```

**Why:** A parent reference can hold a child object. `Animal a = new Dog();` has the reference type `Animal`,
but the actual object is a `Dog`, and `Dog` overrides `makeSound()`. This is runtime polymorphism: which
method gets called is decided at runtime based on the **actual object type**, not the reference type.
So `Dog`'s `makeSound()` runs.

---

## Question 3. 

Java supports single, multi-level, and hierarchical inheritance with classes, but **not multiple inheritance**:
a class cannot extend two classes.

**The Diamond Problem:** if class `C` extends both `A` and `B`, and `A` and `B` both have a method with the same
signature, which one should `C` inherit? The call would be ambiguous, so Java does not allow multiple
inheritance with classes.

```
    ┌───────┐   ┌───────┐
    │   A   │   │   B   │
    └───┬───┘   └───┬───┘
        │     ✗     │       ← Java does NOT allow
        └─────┬─────┘
          ┌───────┐
          │   C   │
          └───────┘
```

**How Java solves it:** Java supports multiple inheritance through **interfaces**. A class can implement
multiple interfaces:

```java
interface Flyable { void fly(); }
interface Swimmable { void swim(); }

class Duck implements Flyable, Swimmable {
    public void fly() { System.out.println("Flying"); }
    public void swim() { System.out.println("Swimming"); }
}
```

If two interfaces have the same `default` method, the class **must override the conflicting default method**.
Inside the override, it can choose which one to use with `InterfaceName.super.method()` (see Question 9).

---

## Question 4. 

The code causes a **compile error**:

```
error: Shape is abstract; cannot be instantiated
```

An abstract class is a class that **cannot be instantiated** and may contain abstract methods. `getArea()` is an
abstract method (no method body), which must be implemented by subclasses.

An abstract class can still be used as a **reference type**:

```java
// Shape s = new Shape();          // Compile error!
Shape s = new Circle("red", 2.0);  // OK - polymorphism
```

---

## Question 5. 

| Feature | Interface | Abstract Class |
|---|---|---|
| Methods | Abstract, default, static, private | Any (abstract + concrete) |
| Fields | Only public static final constants | Any (including instance fields) |
| Constructors | No | Yes |
| Multiple Inheritance | Yes (class can implement multiple) | No (class can extend only one) |
| Access Modifiers | Methods: public (or private Java 9+) | Any access modifier |
| State | Cannot maintain state | Can maintain state |
| Use Case | Define capabilities/contracts | Share common implementation |
| Keyword | implements | extends |

**Use an interface when:**
- defining a contract/capability (what, not how)
- unrelated classes need the same behavior
- you need multiple inheritance
- you want flexibility and loose coupling (e.g. `Comparable`, `Serializable`, `Runnable`)

**Scenario:** different animals have different abilities. If the abstract class `Animal` declared both `fly()`
and `swim()`, `Fish` would be forced to implement `fly()`, which makes no sense. A better design is to
define each capability as an interface (`Flyable`, `Swimmable`, `Walkable`) and keep shared state in the
abstract class:

```java
public class Duck extends Animal implements Flyable, Swimmable, Walkable { ... }
public class Fish extends Animal implements Swimmable { ... }  // No fly()
```

---

## Question 6. 

Output:

```
Animal eating
Meow!
```


**Upcasting (Child → Parent):** assigning a subclass object to a superclass reference, e.g.
`Animal a = new Cat();` (Line 1). It is **implicit and always safe**. However, through the parent
reference you can only call methods defined in the parent class. That is why `a.meow()` (Line 3)
would be a compile error: `Animal` has no `meow()` method.

**Downcasting (Parent → Child):** converting a superclass reference back to a subclass type, e.g.
`Cat c = (Cat) a;` (Line 4). It requires an **explicit cast** and **may fail**: if the object is not
actually a `Cat`, it throws `ClassCastException` at runtime. Always check with `instanceof` before
downcasting. After the cast, Cat-specific methods like `meow()` can be called (Line 5).

---

## Question 7. 

**The equals() Contract (5 Rules):**
1. **Reflexive:** `x.equals(x)` must return true
2. **Symmetric:** if `x.equals(y)` then `y.equals(x)`
3. **Transitive:** if `x.equals(y)` and `y.equals(z)` then `x.equals(z)`
4. **Consistent:** multiple calls return the same result (if objects don't change)
5. **Null handling:** `x.equals(null)` must return false

**The hashCode() Contract:**
1. **Consistency:** the same object must return the same hashCode during execution
2. If `equals()` returns true, `hashCode()` must return the same value
3. If `equals()` returns false, `hashCode()` can return the same value (collision is allowed)

**Why we must also override hashCode():** if you override `equals()` without `hashCode()`, two equal objects
can have different hash codes, which breaks the contract, and **HashMap/HashSet will break**.

`hashCode()` **must use the same fields as `equals()`**.

---

## Question 8. 

- **Shallow copy:** the copy gets a copy of the `address` **reference**, so it **still points to the same
  `Address` object**. Both the original and the copy share one `Address`. Changing the address through
  one of them also changes it for the other. `super.clone()` performs a shallow copy.
- **Deep copy:** it **creates a new `Address` object** for the copy. Each `Person` has its own `Address`, so
  modifying the original's address does not affect the copy.

```
  SHALLOW COPY                         DEEP COPY

  original        copy                 original        copy
  ┌─────────┐   ┌─────────┐            ┌─────────┐   ┌─────────┐
  │ address─┼─┐ │ address─┼─┐          │ address─┼─┐ │ address─┼─┐
  └─────────┘ │ └─────────┘ │          └─────────┘ │ └─────────┘ │
              └──────┬──────┘                      ↓             ↓
                     ↓                     ┌──────────┐  ┌──────────┐
              ┌──────────┐                 │ Address  │  │ Address  │
              │ Address  │                 │ (copy 1) │  │ (copy 2) │
              │ (shared) │                 └──────────┘  └──────────┘
              └──────────┘
  Both point to same Address           Each has own Address copy
```

```java
public class Person implements Cloneable {
    private String name;
    private Address address; // Reference type

    // Shallow copy - address still points to same object
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Deep copy - copy constructor creates a new Address object
    public Person(Person other) {
        this.name = other.name;
        this.address = new Address(other.address);
    }
}
```

---

## Question 9. 

```
Taking off from Flyable
```

**Why:** `Flyable` and `Swimmable` both have a `default` method `takeOff()`. Because `Duck` implements both
interfaces, it **must override the conflicting default method**. In its override, `Duck` uses
`Flyable.super.takeOff()` to choose `Flyable`'s implementation, so it prints `Taking off from Flyable`.

---

## Question 10. 

A method signature consists of the **method name** and the **parameter types (in order)**.
**NOT included:** return type, access modifiers, exceptions.

| Method | Valid? | Reason |
|---|---|---|
| **A** `int calculate(int, int, int)` | Valid | Different number of parameters, so the signature is different |
| **B** `double calculate(double, double)` | Valid | Different parameter types, so the signature is different |
| **C** `double calculate(int, int)` | NOT valid | Only the return type differs, which is not sufficient. It has the same signature as `calculate(int, int)` |
| **D** `private int calculate(int x, int y)` | NOT valid | The access modifier and parameter names are not part of the signature. It has the same signature as `calculate(int, int)` |

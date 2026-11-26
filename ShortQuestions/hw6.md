# HW6 – Java Design Patterns

## Question 1.

| Aspect | Factory Method | Abstract Factory |
|---|---|---|
| Creates | **One product** | **A family of related products** |
| Mechanism | One method (often overridden by subclasses) | An interface with multiple factory methods |
| Example | `create(type)` returns one `PaymentProcessor` | `UIFactory` returns a matching `Button` + `Checkbox` |

**Factory Method** delegates the creation of **a single product** to a factory method. The client only depends on the
product interface, and subclasses decide which concrete class to create:

```java
abstract class NotificationCreator {
    // Factory Method - subclasses decide WHAT to create
    protected abstract Notification createNotification();
}
```

**Abstract Factory** is "a factory of factories": it groups multiple related Factory Methods into one interface, so
it creates **a family of related objects that must be used together** (e.g. UI components that must all match one
theme). Each concrete factory produces a **matching** family:

```java
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class MacUIFactory implements UIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

The client works with any `UIFactory` and always gets a **consistent family** of products (a Mac button always comes
with a Mac checkbox, never a Windows one).

---

## Question 2.

**Why telescoping constructors are hard to use correctly:** a constructor with many parameters is hard to read and
error-prone.

```java
// PROBLEM - which parameter is which?? Easy to swap by mistake.
Pizza pizza = new Pizza(12, true, false, true, "thin", 2, false);
```

The call does not say what each argument means, so the reader has to look up the constructor to know which
`true`/`false` is the cheese and which is the pepperoni. Arguments of the same type can be swapped by mistake and the
code still compiles.

**How the Builder pattern solves it:** Builder constructs a complex object **step by step, with readable, named
steps**.

```java
public class Pizza {
    private final int size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final String crust;

    // Private constructor - only the Builder can create a Pizza
    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.crust = builder.crust;
    }

    public static class Builder {
        // Required parameter
        private final int size;

        // Optional parameters - default values
        private boolean cheese = false;
        private boolean pepperoni = false;
        private String crust = "regular";

        public Builder(int size) {
            this.size = size;   // Required field enforced via constructor
        }

        public Builder cheese(boolean value) {
            this.cheese = value;
            return this;   // Returns "this" - enables method chaining
        }

        public Builder pepperoni(boolean value) {
            this.pepperoni = value;
            return this;
        }

        public Builder crust(String value) {
            this.crust = value;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}

// Usage - readable, self-documenting, order doesn't matter
Pizza pizza = new Pizza.Builder(12)
    .cheese(true)
    .pepperoni(true)
    .crust("thin")
    .build();
```

Benefits:
- **Readable construction:** every value is set by a named method, so the code documents itself, and the order doesn't
  matter.
- **Required vs optional parameters:** required fields are enforced through the Builder's constructor, and optional
  fields have default values.
- **Immutability:** the fields are `final` and set once in `build()`, so the final object is created in one atomic
  construction step.
- **No overloaded constructors:** easy to add optional parameters later without breaking existing calls.

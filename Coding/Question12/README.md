# Question 12 — Pizza Builder Pattern

`Pizza` is immutable: the class is `final`, all instance fields are `private final`, and no setters are exposed. The static inner `Builder` requires `size` and provides chainable optional setters. Defaults are `false` for cheese, pepperoni, and mushrooms, and `"regular"` for crust.

## Compile and run

```bash
javac Pizza.java Main.java
java Main
```

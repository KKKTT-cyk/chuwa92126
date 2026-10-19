# HW3 – Java 8 New Features

## Question 1.

A **Functional Interface** is an interface with **exactly one abstract method** (SAM - Single Abstract
Method). It can still have multiple `default` and `static` methods. Lambda expressions implement its abstract
method.

It is marked with the **`@FunctionalInterface`** annotation. The annotation is **optional but recommended**:
an interface with exactly one abstract method is a functional interface even without it. The annotation adds
**compile-time validation**, so the compiler reports an error if the interface does not have exactly one
abstract method.

Two built-in functional interfaces:

| Interface | Abstract method | Meaning |
|---|---|---|
| `Predicate<T>` | `boolean test(T t)` | Accepts one input of type `T`, returns a `boolean` |
| `Function<T, R>` | `R apply(T t)` | Accepts one input of type `T`, returns a transformed result of type `R` |

```java
Predicate<Product> inStock = product -> product.getQuantity() > 0;
Function<Product, String> getName = product -> product.getName();
```

---

## Question 2.

```
Hello from Greeting
And hello from Person
```

**Why:** `Greeting` and `Farewell` both have a `default` method `sayHello()`. Because `Person` implements both
interfaces, it must override the conflicting default method. In its override, `Greeting.super.sayHello()`
calls `Greeting`'s default implementation, which prints `Hello from Greeting`. Then `Person` prints its own
line, `And hello from Person`.

---

## Question 3.

```java
// STEP 0: Start with Anonymous Class
Comparator<String> comparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};
```

**Step 1: Remove boilerplate** (`new Comparator<String>()`, the method name, `@Override`). Keep only the
parameters and the method body, and connect them with `->`. This works because `Comparator` is a functional
interface, so the compiler knows the lambda implements `compare()`.

```java
Comparator<String> comparator = (String s1, String s2) -> {
    return s1.length() - s2.length();
};
```

**Step 2: Remove parameter types.** The compiler infers them from `Comparator<String>`.

```java
Comparator<String> comparator = (s1, s2) -> {
    return s1.length() - s2.length();
};
```

**Step 3: The body is a single statement, so remove the braces `{}` and the `return` keyword.** A single
expression is returned automatically.

```java
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();
```

---

## Question 4.

| Lambda | Valid? | Reason |
|---|---|---|
| **A** `() -> System.out.println("Running")` | Valid | `run()` takes no parameters, so empty `()` is required. Single void statement, no braces needed |
| **B** `s -> return s.isEmpty()` | NOT valid | `return` without braces. A single expression is returned automatically: `s -> s.isEmpty()` |
| **C** `x -> { x * 2; }` | NOT valid | Braces without `return`. A block body must use an explicit `return` for a non-void method: `x -> { return x * 2; }` or `x -> x * 2` |
| **D** `(String s) -> System.out.println(s)` | Valid | Parameter with explicit type, so `()` is required |
| **E** `(a, b) -> a + b` | Valid | Multiple parameters use `()`. Single expression is returned automatically |
| **F** `() -> { return "Hello"; }` | Valid | Block body with explicit `return` |

---

## Question 5.

| Lambda | Method reference | Type |
|---|---|---|
| 1. `x -> System.out.println(x)` | **B.** `System.out::println` | **Bound instance**: `System.out` is a specific object that already exists, and the parameter is passed to its method |
| 2. `s -> s.toUpperCase()` | **D.** `String::toUpperCase` | **Unbound instance**: the method is called on the parameter itself |
| 3. `x -> Math.abs(x)` | **C.** `Math::abs` | **Static**: the lambda calls a static method of a class |
| 4. `() -> new ArrayList<>()` | **A.** `ArrayList::new` | **Constructor**: the lambda creates a new instance |
| 5. `(s1, s2) -> s1.compareTo(s2)` | **E.** `String::compareTo` | **Unbound instance**: the first parameter becomes the object calling the method, the second becomes the method parameter |

---

## Question 6.

| Method | When to use | If value is null |
|---|---|---|
| `Optional.of(value)` | Value is guaranteed non-null | **Throws `NullPointerException`** |
| `Optional.ofNullable(value)` | Value might be null | Returns an **empty Optional** |

**What happens:** `Optional.of(value)` is called with `null`, so it **throws `NullPointerException`
immediately** on that line. The program stops there, so the following lines never run and nothing is printed.

---

## Question 7.

```
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

- **`orElse(default)`**: the default value is **always evaluated**, even if the Optional has a value.
  `createDefault()` is called as an argument before `orElse` runs, so `Creating default value` is printed,
  even though the result is still `Hello`.
- **`orElseGet(supplier)`**: the default is computed **only when needed** (lazy evaluation). The Optional has a
  value, so the supplier `() -> createDefault()` is never called.

Rule: use `orElseGet()` when the default value is expensive to create (database calls, network requests,
complex object creation, logging side effects).

---

## Question 8.

- **`map()`**: transforms **each element into one element** (one-to-one). The type can change, but the number of
  elements stays the same.
- **`flatMap()`**: used when **each element maps to multiple elements** (one-to-many). The function returns a
  stream, and `flatMap()` **flattens** all of those streams into a single stream.

With `map(Order::getProducts)` we would get a nested `Stream<List<Product>>`. `flatMap()` flattens it to
`Stream<Product>`:

```java
List<String> allProductNames = orders.stream()
    .flatMap(order -> order.getProducts().stream())   // Stream<Product>
    .map(Product::getName)                            // Stream<String>
    .collect(Collectors.toList());
```

---

## Question 9.

```
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
```

**Lazy evaluation:** intermediate operations (`filter`, `map`) are **lazy**. They don't execute until a
terminal operation is called. Building the pipeline only describes the steps, so nothing is printed before
`Stream created` and `Calling findFirst...`.

`findFirst()` is the terminal operation, and it **triggers the execution** of the whole pipeline. Elements go
through the pipeline one at a time: `1` is filtered out, `2` passes the filter and is mapped to `20`.
`findFirst()` only needs the first element, so processing **stops there**, and `3`, `4`, `5` are never
processed.

---

## Question 10.

Problems:

1. **`Optional` used as a method parameter.** This is an anti-pattern: `Optional` is meant for return types.
2. **`Optional` used with a collection** (`Optional<List<Product>>`). An empty collection should be used
   instead.
3. **`isPresent()` + `get()` pattern.** This is just a null check written differently. Use `ifPresent()` or
   `map()` instead.
4. **`calculateTotal()` returns `null` from a method whose return type is `Optional`.** This defeats the
   purpose of `Optional`: a caller who calls `calculateTotal(products).isPresent()` gets a
   `NullPointerException`. It should return `Optional.empty()`.

Fixed version:

```java
public class ProductService {

    public void processProducts(List<Product> products) {
        products.forEach(this::process);
    }

    public Optional<BigDecimal> calculateTotal(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal total = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(total);
    }
}
```

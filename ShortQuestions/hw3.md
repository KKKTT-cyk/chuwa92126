# Java 8 New Features Homework

## Conceptual Questions

### Question 1

**What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.**

A Functional Interface is an interface that contains exactly one abstract method.

The annotation used to mark a Functional Interface is:

```java
@FunctionalInterface
```

The annotation is not mandatory. An interface with exactly one abstract method is still considered a Functional Interface even without the annotation. However, using `@FunctionalInterface` allows the compiler to check that the interface satisfies the Functional Interface requirements.

Two built-in Functional Interfaces are:

1. `Predicate<T>`

```java
boolean test(T t);
```

It accepts one argument of type `T` and returns a `boolean`.

Example:

```java
Predicate<Integer> isPositive = n -> n > 0;
```

2. `Function<T, R>`

```java
R apply(T t);
```

It accepts one argument of type `T` and returns a value of type `R`.

Example:

```java
Function<String, Integer> length = s -> s.length();
```

---

### Question 2

**What will be the output of the code? Explain why.**

Output:

```text
Hello from Greeting
And hello from Person
```

Both `Greeting` and `Farewell` contain a default method named `sayHello()` with the same signature.

Because `Person` implements both interfaces, there is a conflict between the two default methods. Therefore, `Person` overrides `sayHello()`.

Inside the overridden method:

```java
Greeting.super.sayHello();
```

explicitly calls the default `sayHello()` method from the `Greeting` interface.

Then:

```java
System.out.println("And hello from Person");
```

prints the second line.

---

### Question 3

**Convert the anonymous class to a lambda expression. Explain each step of the conversion process.**

Original code:

```java
Comparator<String> comparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};
```

`Comparator` is a Functional Interface, so the anonymous class can be replaced by a lambda expression.

First, remove the anonymous class and method declaration:

```java
Comparator<String> comparator =
    (String s1, String s2) -> {
        return s1.length() - s2.length();
    };
```

Java can infer the parameter types:

```java
Comparator<String> comparator =
    (s1, s2) -> {
        return s1.length() - s2.length();
    };
```

Because there is only one expression, we can remove the braces and `return`:

```java
Comparator<String> comparator =
    (s1, s2) -> s1.length() - s2.length();
```

Final answer:

```java
Comparator<String> comparator =
    (s1, s2) -> s1.length() - s2.length();
```

---

### Question 4

**Which lambda expressions are valid? For invalid ones, explain the reason.**

#### A

```java
Runnable r = () -> System.out.println("Running");
```

**Valid.**

`Runnable.run()` takes no arguments and returns `void`.

#### B

```java
Predicate<String> p = s -> return s.isEmpty();
```

**Invalid.**

`return` cannot be used without braces.

Correct:

```java
Predicate<String> p = s -> s.isEmpty();
```

or:

```java
Predicate<String> p = s -> {
    return s.isEmpty();
};
```

#### C

```java
Function<Integer, Integer> f = x -> { x * 2; };
```

**Invalid.**

`Function.apply()` must return a value, but the lambda does not return anything.

Correct:

```java
Function<Integer, Integer> f = x -> x * 2;
```

or:

```java
Function<Integer, Integer> f = x -> {
    return x * 2;
};
```

#### D

```java
Consumer<String> c =
    (String s) -> System.out.println(s);
```

**Valid.**

#### E

```java
BiFunction<Integer, Integer, Integer> bi =
    (a, b) -> a + b;
```

**Valid.**

#### F

```java
Supplier<String> sup = () -> {
    return "Hello";
};
```

**Valid.**

Summary:

| Lambda | Valid? |
|---|---|
| A | Yes |
| B | No |
| C | No |
| D | Yes |
| E | Yes |
| F | Yes |

---

### Question 5

**Match each lambda expression with its corresponding method reference and explain the type of each method reference.**

| # | Lambda Expression | Method Reference | Type |
|---|---|---|---|
| 1 | `x -> System.out.println(x)` | `System.out::println` | Bound Instance |
| 2 | `s -> s.toUpperCase()` | `String::toUpperCase` | Unbound Instance |
| 3 | `x -> Math.abs(x)` | `Math::abs` | Static |
| 4 | `() -> new ArrayList<>()` | `ArrayList::new` | Constructor |
| 5 | `(s1, s2) -> s1.compareTo(s2)` | `String::compareTo` | Unbound Instance |

Therefore:

```text
1 → B
2 → D
3 → C
4 → A
5 → E
```

---

### Question 6

**What is the difference between `Optional.of()` and `Optional.ofNullable()`? What happens when the given code executes?**

`Optional.of()` requires the value to be non-null.

For example:

```java
Optional.of(null);
```

throws a `NullPointerException`.

`Optional.ofNullable()` accepts both null and non-null values.

If the value is null:

```java
Optional.ofNullable(null);
```

returns:

```java
Optional.empty()
```

In the given code:

```java
String value = null;

Optional<String> opt1 = Optional.of(value);
Optional<String> opt2 = Optional.ofNullable(value);
```

the program throws a `NullPointerException` when this line executes:

```java
Optional<String> opt1 = Optional.of(value);
```

Therefore, execution stops there, and the later statements are not executed.

If only `Optional.ofNullable(value)` were used:

```java
Optional<String> opt2 = Optional.ofNullable(value);
System.out.println(opt2.isPresent());
```

the output would be:

```text
false
```

---

### Question 7

**What will be the output? Explain the difference between `orElse()` and `orElseGet()`.**

Output:

```text
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

The `Optional` contains:

```java
Optional<String> opt = Optional.of("Hello");
```

With:

```java
opt.orElse(createDefault());
```

`createDefault()` is evaluated immediately, even though the Optional already contains `"Hello"`.

Therefore:

```text
Creating default value
```

is printed.

With:

```java
opt.orElseGet(() -> createDefault());
```

the supplier is only executed if the Optional is empty.

Since `opt` contains `"Hello"`, `createDefault()` is not called.

In summary:

- `orElse()` evaluates the default value eagerly.
- `orElseGet()` evaluates the default value lazily, only when it is needed.

---

### Question 8

**Explain the difference between `map()` and `flatMap()` in Stream API. Write code to get a list of all product names from all orders.**

`map()` transforms each element of a stream into another element.

For example:

```java
orders.stream()
      .map(Order::getProducts)
```

produces:

```text
Stream<List<Product>>
```

Each `Order` becomes a `List<Product>`.

`flatMap()` is used when each element produces another stream or collection. It combines the nested streams into one stream.

Conceptually:

```text
Stream<List<Product>>
        ↓
Stream<Product>
```

Solution:

```java
List<String> allProductNames =
    orders.stream()
          .flatMap(order -> order.getProducts().stream())
          .map(Product::getName)
          .collect(Collectors.toList());
```

Another version using method references:

```java
List<String> allProductNames =
    orders.stream()
          .map(Order::getProducts)
          .flatMap(List::stream)
          .map(Product::getName)
          .collect(Collectors.toList());
```

---

### Question 9

**What will be the output? Explain lazy evaluation in the Stream API.**

Output:

```text
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
```

Stream intermediate operations such as:

```java
filter()
map()
```

are lazy.

This means they do not execute when the stream pipeline is created.

The operations start executing when a terminal operation is called:

```java
stream.findFirst();
```

First, `1` is checked:

```text
Filtering: 1
```

It is odd, so it does not pass the filter.

Then `2` is checked:

```text
Filtering: 2
```

It passes the filter and is sent to `map()`:

```text
Mapping: 2
```

The mapping operation calculates:

```text
2 * 10 = 20
```

Because `findFirst()` only needs the first matching element, the stream stops processing after finding `20`.

Therefore, `3`, `4`, and `5` are never processed.

This demonstrates both **lazy evaluation** and **short-circuiting** in the Stream API.

---

### Question 10

**Analyze the code. What is wrong with it? How would you fix it?**

There are two main problems.

#### Problem 1: Using `isPresent()` followed by `get()`

The original code uses:

```java
if (productsOpt.isPresent()) {
    List<Product> products = productsOpt.get();

    for (Product p : products) {
        process(p);
    }
}
```

This works, but Java 8 provides a cleaner way using `ifPresent()` and `forEach()`:

```java
productsOpt.ifPresent(products ->
    products.forEach(this::process)
);
```

#### Problem 2: Returning `null` instead of `Optional.empty()`

The method returns:

```java
Optional<BigDecimal>
```

but contains:

```java
return null;
```

This defeats the purpose of using `Optional`.

It should return:

```java
return Optional.empty();
```

Corrected code:

```java
public class ProductService {

    public void processProducts(
            Optional<List<Product>> productsOpt) {

        productsOpt.ifPresent(products ->
            products.forEach(this::process)
        );
    }

    public Optional<BigDecimal> calculateTotal(
            List<Product> products) {

        if (products == null || products.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(
                    BigDecimal.ZERO,
                    BigDecimal::add
                );

        return Optional.of(total);
    }

    private void process(Product product) {
        // Process product
    }
}
```

The important rule is:

> If a method returns `Optional<T>`, use `Optional.empty()` to represent the absence of a value instead of returning `null`.
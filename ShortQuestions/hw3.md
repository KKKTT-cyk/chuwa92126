# Java 8 New Features - Homework 3

## Question 1

**What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.**

A functional interface is an interface that has exactly one abstract method. It may also contain default methods, static methods, and methods inherited from `Object`.

The annotation used to mark a functional interface is:

```java
@FunctionalInterface
```

The annotation is not mandatory. An interface is still a functional interface if it has exactly one abstract method, even without the annotation. However, the annotation is useful because the compiler can verify that the interface satisfies the functional-interface rule.

Two built-in functional interfaces are:

1. `Predicate<T>`

```java
boolean test(T t);
```

It accepts one value and returns a `boolean`.

2. `Function<T, R>`

```java
R apply(T t);
```

It accepts a value of type `T` and returns a value of type `R`.

---

## Question 2

**What will be the output of the given code? Explain why.**

Output:

```text
Hello from Greeting
And hello from Person
```

Both `Greeting` and `Farewell` define a default method named `sayHello()`. Because `Person` implements both interfaces, it must resolve the conflict by overriding `sayHello()`.

Inside the override:

```java
Greeting.super.sayHello();
```

explicitly calls the default method from `Greeting`.

Then:

```java
System.out.println("And hello from Person");
```

prints the second line.

---

## Question 3

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

Lambda version:

```java
Comparator<String> comparator =
        (s1, s2) -> s1.length() - s2.length();
```

Conversion steps:

1. `Comparator<String>` is a functional interface because it has one abstract method, `compare`.
2. Remove the anonymous class creation: `new Comparator<String>() { ... }`.
3. Remove `@Override`, the method name, and the return type.
4. Keep the method parameters `(s1, s2)`.
5. Add the lambda arrow `->`.
6. Because the method body contains only one return expression, remove the braces and the `return` keyword.

---

## Question 4

**Which of the following lambda expressions are valid? For invalid ones, explain the reason.**

### A

```java
Runnable r = () -> System.out.println("Running");
```

**Valid.** `Runnable.run()` takes no arguments and returns `void`.

### B

```java
Predicate<String> p = s -> return s.isEmpty();
```

**Invalid.** The `return` keyword cannot be used directly in an expression lambda. It must either be removed:

```java
Predicate<String> p = s -> s.isEmpty();
```

or placed inside braces:

```java
Predicate<String> p = s -> {
    return s.isEmpty();
};
```

### C

```java
Function<Integer, Integer> f = x -> { x * 2; };
```

**Invalid.** `Function<Integer, Integer>` must return an `Integer`, but this block does not return a value. It should be:

```java
Function<Integer, Integer> f = x -> x * 2;
```

or:

```java
Function<Integer, Integer> f = x -> {
    return x * 2;
};
```

### D

```java
Consumer<String> c =
        (String s) -> System.out.println(s);
```

**Valid.** A `Consumer<T>` accepts one argument and returns no value.

### E

```java
BiFunction<Integer, Integer, Integer> bi =
        (a, b) -> a + b;
```

**Valid.** A `BiFunction<T, U, R>` accepts two arguments and returns one result.

### F

```java
Supplier<String> sup = () -> {
    return "Hello";
};
```

**Valid.** A `Supplier<T>` accepts no arguments and returns a value.

---

## Question 5

**Match each lambda expression with its corresponding method reference. Explain the type of each method reference.**

| Lambda | Method Reference | Type |
|---|---|---|
| `x -> System.out.println(x)` | `System.out::println` | Bound instance method reference |
| `s -> s.toUpperCase()` | `String::toUpperCase` | Unbound instance method reference |
| `x -> Math.abs(x)` | `Math::abs` | Static method reference |
| `() -> new ArrayList<>()` | `ArrayList::new` | Constructor reference |
| `(s1, s2) -> s1.compareTo(s2)` | `String::compareTo` | Unbound instance method reference |

Therefore, the matches are:

```text
1 -> B
2 -> D
3 -> C
4 -> A
5 -> E
```

---

## Question 6

**What is the difference between `Optional.of()` and `Optional.ofNullable()`? What will happen when executing the given code?**

`Optional.of(value)` requires `value` to be non-null. If `value` is `null`, it immediately throws a `NullPointerException`.

`Optional.ofNullable(value)` accepts either a non-null value or `null`. If the value is `null`, it returns `Optional.empty()`.

Given:

```java
String value = null;

Optional<String> opt1 = Optional.of(value);
Optional<String> opt2 = Optional.ofNullable(value);
```

the first `Optional.of(value)` throws a `NullPointerException`.

Therefore, execution stops at that line. `opt2` is not created and the later `println` statements are not executed.

If `Optional.ofNullable(value)` were executed separately with `value == null`, it would produce an empty `Optional`, and `isPresent()` would return `false`.

---

## Question 7

**What will be the output of the given code? Explain the difference between `orElse()` and `orElseGet()`.**

Output:

```text
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

The `Optional` already contains `"Hello"`.

With:

```java
opt.orElse(createDefault())
```

the argument to `orElse()` is evaluated before the method call. Therefore, `createDefault()` runs even though the Optional already contains a value.

With:

```java
opt.orElseGet(() -> createDefault())
```

the supplier is evaluated lazily. Because the Optional already contains `"Hello"`, `createDefault()` is not called.

Therefore, `orElseGet()` is useful when creating the default value is expensive or has side effects.

---

## Question 8

**Explain the difference between `map()` and `flatMap()` in Stream API. Write code to get a list of all product names from all orders.**

`map()` transforms each stream element into another value. One input element produces one output element.

`flatMap()` is used when each input element can produce another stream or collection. It flattens those nested streams into one stream.

Given:

```java
List<Order> orders;
```

the product names can be collected with:

```java
List<String> allProductNames = orders.stream()
        .flatMap(order -> order.getProducts().stream())
        .map(Product::getName)
        .collect(Collectors.toList());
```

`flatMap()` converts the stream of orders into one stream containing all products from all orders. Then `map()` converts each `Product` into its name.

---

## Question 9

**What will be the output of the given code? Explain the concept of lazy evaluation in Stream API.**

Output:

```text
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
```

When the stream pipeline is created, the intermediate operations `filter()` and `map()` do not immediately process the data.

Processing begins only when the terminal operation:

```java
stream.findFirst();
```

is called.

`findFirst()` is also a short-circuiting terminal operation. The stream checks `1`, which fails the even-number filter. It then checks `2`, which passes, maps it to `20`, and stops because the first result has been found.

This demonstrates lazy evaluation: intermediate stream operations execute only when a terminal operation requires results.

---

## Question 10

**Analyze the given code. What is wrong with it? How would you fix it?**

There are two main problems.

First, this code:

```java
if (productsOpt.isPresent()) {
    List<Product> products = productsOpt.get();

    for (Product p : products) {
        process(p);
    }
}
```

works, but it does not use `Optional` in an idiomatic Java 8 style. It can be simplified with `ifPresent()` and a method reference:

```java
productsOpt.ifPresent(
        products -> products.forEach(this::process)
);
```

Second, this method is incorrect:

```java
public Optional<BigDecimal> calculateTotal(
        List<Product> products) {

    if (products == null || products.isEmpty()) {
        return null;
    }

    // ...
}
```

A method that returns `Optional<T>` should not return `null`. The purpose of `Optional` is to represent the absence of a value.

A corrected version is:

```java
public Optional<BigDecimal> calculateTotal(
        List<Product> products) {

    if (products == null || products.isEmpty()) {
        return Optional.empty();
    }

    BigDecimal total = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return Optional.of(total);
}
```

The caller can then handle the missing value safely with methods such as `ifPresent()`, `orElse()`, or `orElseThrow()`.

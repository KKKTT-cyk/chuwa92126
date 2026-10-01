# Homework 2 - Java 8 New Features

## Question 1. What is a functional interface? Is its annotation required? Give two built-in examples and their abstract methods.

A functional interface has one abstract method. It can also have default and static methods because those already have bodies. Declarations matching public methods of `Object`, such as `equals`, do not count toward the single abstract method.

`@FunctionalInterface` marks it as a functional interface. The annotation is optional, but it lets the compiler check that the interface follows the rule.

Two examples are:

- `Predicate<T>`: `boolean test(T t)` takes a value and returns true or false. For example, it can check whether a price is over 100.
- `Function<T, R>`: `R apply(T t)` takes a value of type `T` and returns a result of type `R`. For example, it can turn a student into the student's name.

## Question 2. What does the Greeting/Farewell example print, and why?

```text
Hello from Greeting
And hello from Person
```

Both interfaces define `sayHello()`, so `Person` overrides it to resolve the conflict. Inside that method, `Greeting.super.sayHello()` calls the default method from `Greeting`. Then the next statement prints the message from `Person`. The `Farewell` implementation is not called.

## Question 3. Convert the anonymous Comparator to a lambda and explain the steps.

```java
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();
```

1. `Comparator<String>` supplies the target type, so Java knows the lambda implements `compare`.
2. Remove `new Comparator<String>()`, `@Override`, and the method declaration.
3. Keep the two parameters. Their types can be omitted because Java knows they are strings.
4. Add `->` before the body. Since the body returns one expression, remove the braces and `return`.

This still sorts strings by length. Strings with equal lengths compare as zero.

## Question 4. Which lambda expressions are valid?

| Lambda | Valid? | Reason |
| --- | --- | --- |
| A | Yes | `Runnable.run()` takes no arguments and returns nothing. |
| B | No | A `return` statement needs a block body with braces. |
| C | No | The block must return an integer. Also, `x * 2;` alone is not a valid statement. |
| D | Yes | `Consumer.accept()` takes one argument and returns nothing. An explicit parameter type is allowed. |
| E | Yes | `BiFunction.apply()` takes two arguments and returns a result. |
| F | Yes | `Supplier.get()` takes no arguments. The block returns a string. |

Fix B:

```java
Predicate<String> p = s -> s.isEmpty();
```

Or use a block:

```java
Predicate<String> p = s -> { return s.isEmpty(); };
```

Fix C:

```java
Function<Integer, Integer> f = x -> x * 2;
```

Or use a block:

```java
Function<Integer, Integer> f = x -> { return x * 2; };
```

## Question 5. Match each lambda with a method reference and identify its type.

| Number | Match | Method reference | Type |
| --- | --- | --- | --- |
| 1 | B | `System.out::println` | Bound instance |
| 2 | D | `String::toUpperCase` | Unbound instance |
| 3 | C | `Math::abs` | Static |
| 4 | A | `ArrayList::new` | Constructor |
| 5 | E | `String::compareTo` | Unbound instance |

`System.out::println` is bound to the existing `System.out` object. For `String::toUpperCase`, the argument supplies the string object. For `String::compareTo`, the first argument is the object and the second is passed to `compareTo`. `Math::abs` calls a static method, and `ArrayList::new` creates an object.

## Question 6. How do Optional.of() and Optional.ofNullable() differ? What happens in the given code?

`Optional.of(value)` requires a non-null value. `Optional.ofNullable(value)` accepts null and returns `Optional.empty()` for it.

The line `Optional.of(value)` throws a `NullPointerException` because `value` is null. The rest of the code is not reached, so neither `println` runs. It does not print `true` or `false`.

If the failing line is removed, this prints `false`:

```java
Optional<String> opt2 = Optional.ofNullable(value);
System.out.println(opt2.isPresent());
```

## Question 7. What is the output? How do orElse() and orElseGet() differ?

```text
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

In `orElse(createDefault())`, Java calls `createDefault()` to evaluate the argument before calling `orElse`. This happens even though the Optional already contains `"Hello"`.

`orElseGet` takes a supplier. It only calls the supplier when the Optional is empty. Here it keeps `"Hello"` without calling `createDefault()`.

## Question 8. How do map() and flatMap() differ? Get all product names from all orders.

`map()` turns each element into one result. For example, mapping an order to its products gives a stream of lists.

`flatMap()` turns each element into a stream and joins those streams into one stream. Here it gives us the individual products from every order.

```java
List<String> allProductNames = orders.stream()
        .flatMap(order -> order.getProducts().stream())
        .map(Product::getName)
        .collect(Collectors.toList());
```

This keeps duplicate names. The code assumes the orders and product lists are non-null.

## Question 9. What is the output? Explain lazy evaluation.

```text
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20
```

`filter` and `map` are intermediate operations. They do not process the numbers when the stream is created. Processing starts when `findFirst()` runs.

The value 1 fails the filter. The value 2 passes and is mapped to 20. `findFirst()` has its result, so it stops without processing 3, 4, or 5.

## Question 10. What is wrong with ProductService, and how would you fix it?

The main bug is returning `null` from a method whose return type is `Optional<BigDecimal>`. A caller expects an Optional and could get a `NullPointerException`. Return `Optional.empty()` instead.

The `isPresent()` and `get()` combination in `processProducts` is safe as written, assuming the Optional itself is not null. It is just more verbose than `ifPresent`. Taking an Optional parameter is also usually unnecessary; taking a list and using an empty list for no products is simpler.

Keeping the original method signatures, I would write:

```java
public void processProducts(Optional<List<Product>> productsOpt) {
    productsOpt.ifPresent(products -> products.forEach(this::process));
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
```

These methods assume `process(Product)` already exists and each product has a non-null price. For the first method, callers should pass `Optional.empty()`, not null.

If I can change the first signature, I would use a non-null list instead:

```java
public void processProducts(List<Product> products) {
    products.forEach(this::process);
}
```

An empty list means there is nothing to process.

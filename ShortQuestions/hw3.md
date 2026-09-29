# Java 8 New Features Homework

## Question 1. What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.

Functional Interface is interface that has exactly one abstract method. The annotation `@FunctionalInterface`  is used to mark a functional interface, but is not mandatory.

Built-in examples:
`Predicate<T>`: boolean test(T t) — takes one value and returns a boolean.
`Function<T, R>`: R apply(T t) — takes one value of type T and returns a value of type R.


## Question 2. What will be the output of the following code? Explain why.
Output:
`Hello from Greeting`
`And hello from Person`

Both `Greeting` and `Farewell` define the same default method, so `Person` must override `sayHello()` to resolve the conflict.

`Greeting.super.sayHello()` calls the default method from `Greeting`, and the `Person` method prints the second line.


## Question 3. Convert the following anonymous class to a lambda expression. Explain each step of the conversion process.
Lambda expression:

`Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();`

The anonymous class can be converted to a lambda because `Comparator` is a functional interface. The parameter types can be inferred, and the method body contains only one return expression, so `{}` and `return` keyword can be omitted.


## Question 4. Which of the following lambda expressions are valid? For invalid ones, explain the reason.
VALID: ADEF
NOT VALID:
B: `return` cannot be used without braces. It should be `s -> s.isEmpty()` or `s -> { return s.isEmpty(); }`
C: The block body does not return a value. It should be `x -> x * 2`.


## Question 5. Match each lambda expression with its corresponding method reference. Explain the type of each method reference (Static, Bound Instance, Unbound Instance, or Constructor).
1.-B `System.out::println` — Bound Instance
2.-D `String::toUpperCase` — Unbound Instance
3.-C `Math::abs` — Static
4.-A `ArrayList::new` — Constructor
5.-E `String::compareTo` — Unbound Instance


## Question 6. What is the difference between Optional.of() and Optional.ofNullable()? What will happen when executing the following code?
`Optional.of()` does not allow a null value and throws Exception if the value is null.
`Optional.ofNullable()` allows null and returns `Optional.empty()` when the value is null.

In this code, `Optional.of(value)` will throw a null Exception.


## Question 7. What will be the output of the following code? Explain the difference between `orElse()` and `orElseGet()`.
Output:
"--- Using orElse ---"
"Creating default value"
"Result: Hello"
"--- Using orElseGet ---"
"Result: Hello"

`orElse()` evaluates its default value even when the Optional contains a value. `orElseGet()` evaluates its supplier only when the Optional is empty.


## Question 8. Explain the difference between `map()` and `flatMap()` in Stream API. Given the following class structure, write code to get a list of all product names from all orders.
`map()` transforms each element into another value, while `flatMap()` transforms element and flattens nested streams into one stream.

```java
List<String> allProductNames = orders.stream()
    .flatMap(order -> order.getProducts().stream())
    .map(Product::getName)
    .collect(Collectors.toList());
```


## Question 9. What will be the output of the following code? Explain the concept of lazy evaluation in Stream API.
Output:
"Stream created"
"Calling findFirst..."
"Filtering: 1"
"Filtering: 2"
"Mapping: 2"
"Result: 20"

Stream operations like `filter()` and `map()` are lazy, so they do not run until a terminal operation is called.


## Question 10. Analyze the following code. What is wrong with it? How would you fix it?
`Optional` should not return `null`, but should return `Optional.empty()` instead.

```java

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
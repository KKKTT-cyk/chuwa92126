Question 1:
A Functional Interface is an interface with exactly 1 abstract method although it can have multiple default or static methods. Annotation for Functional Interface is @FunctionalInterface, which is optional but recommended.
Examples of two built-in Functional Interfaces in Java 8:

- Predicate<T>: the one abstract method is boolean test(T t), which takes one argument of type T and returns a boolean. It can be used when needing to check a condition against something.
- Functional <T, R>: the one abstract method is R apply (T, t), which takes one argument of type T and returns a transformed result of a different type R. It's used for converting or mapping one type into another.

Question 2:
The output will be:

```
Hello from Greeting
And hello from Person
```

That's because both Greeting and Farewell declare a default method with the same signature of sayHello(). Person implements both interfaces. To avoid conflicts,it uses "Greeting.super.sayHello()" as well as "And hello from Person" to expicitly override the method sayHello() and resolve the conflict.

Question 3:
Lambda expression is: `Comparator<String> comparater = (s1, s2) -> s1.length() - s2.length`
Steps: 1. Remove the boilerplate of new Comparator<STring>() {...}, @Override, method name. 2. Omit types of s1 and s2 since the compiler infers them from Comparator<String>. Add "->" between parameters and the method body. 3. Since the body is a single expression, drop the brace and the keyword of "return".

Question 4:
A is valid by implementing void run() with no parameters nor return value.
B is invalid because incorrectly use the return keyword without using the {}, Return keywork is only valid inside a block body wrapped in {}.
C is invalid by using the brace {} without using the return keyword.
D is valid because explicit parameter types a allowed.
E is valid: two parameters correctly requires parentheses and the body is a single experession without braces or return keyword.
F is valid by correctly using () for no parameter and matched braces with explicit return.

Question 5:

1. x -> System.out.println(x) matches with B. System.out::println, method reference is Bound Instance.
2. s -> s.toUppderCase() matches with D. String::toUpperCase, method reference is Unbound Instance.
3. x -> Math.abs(x) matches with C. Math::abs, method reference is Static.
4. () -> new ArrayList<>() matches with A. ArrayList::New, method reference is Constructor.
5. (s1, s2) -> s1.compareTo(s2) matches with E. String::compareTo, method reference if Unbound Instance.

Question 6:
Both Optional.of(value) and Optional.ofNullable(value) wraps the value normally. But if value is null, Optional.of throws NullPointerException immediately so line 1 throws a NullPointerException immediately and the program crashes. However, Optional.ofNullable returns Optional.empty() safely. Optional.of can only be used when 100% sure the value is not null.

Question 7:
The output will be:

```
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
```

The difference between orElse() and orElseGet() is the eager evaluation vs. lazy evaluation of method argument. For orElse(), it takes a plain value as its argument. Even if the value in Optional.of(value) is not null, the method arguments are evaluated eagerly even before the method is called. So in orElse(createDefault()), createDefault() runs and prints its message, because Java has to compute the argument first in order to pass it to orElse(). While for orElseGet(), it takes a Supplier lambda that wraps the call but doesn't invoke it. orElseGet() only calls .get() on the supplier if the Optional is actually empty. So in the code since ops is present, createDefault() is never called thus "Creating default value" is not printed.

Question 8:
Difference between map() and flatMap(): map() is used when each element tranforms into exactly one new element. flatMap() is used when each element transforms into a stream/collection of elements (one-to-many). So if using map() on somethong whose transformation itself produces a collection, the result will be a nested structure (a collection of collection). While flatMap() takes each of the inner lists, converts each to its own stream and merges all of them into one single continous stream.
Code to get List<String> allProductNames from the given List<Order> orders:

```List<String> allProductNames = orders.stream().
        flatMap(order -> order.getProducts()).stream()
        .map(Product::getName)
        .collect(Collectors.toList());
```

Question 9:
The output is:

```Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20

```

Lazy evaluation means intermediate stream operations (filter, map, sorted, etc.) don't actually process any data when you call them — they just build up a pipeline description. Nothing runs until a terminal operation (collect, forEach, findFirst, count, etc.) is invoked. Only then does the whole pipeline execute, and even then, only as much as necessary to produce the terminal operation's result.Instead of running filter across the entire list, then map across the entire list (like separate loops), Java pushes each individual element through the entire chain of operations before moving to the next element.

Question 10:

- For the processProduct method, Optional type is used as a method parameter, which is problematic. Optional was designed to be used as a return type to signal that the method might not have a result. Using Optional as a method parameter type is bad practice which forces every caller to wrap their argument in an Optional just to call the method, adding the trouble. And Optional itself still can be null which doesn't solve the null-safety problem.
  Fix: just use a plain (possibly null) List<Product> as parameter type and check for null directly:

```
public void processProducts(List<Product> products) {
    if (products != null) {
        for (Product p : products) {
            process(p);
        }
    }
}
```

- For the calculateTotal method, it returns null ("this variable points to nothing") instead of Optional.empty() (a real object, a wrapper which either holds a BigDecimal inside it or explicitly holds nothing) for empty products list.
  Returning null will incur NullPointerException when a caller expect a potentially empty but still a real object of the empty Project list and tries to use `if result.isPresent()` on the null result. To fix it, the return type for an empty Product list should be Optional.empty() instead of null:

```
Optional<BigDecimal> calculateTotal(List<Product> products) {
    if (products == null || products.isEmpty()) {
        return Optinal.empty();
    }
    ...
}
```

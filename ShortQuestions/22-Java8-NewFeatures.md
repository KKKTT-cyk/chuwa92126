Question 1. What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.
A functional interface is an interface that has only one abstract method and is often used with lambda expressions. The @FunctionalInterface annotation can be used to identify it, but it is optional. Two common Java 8 functional interfaces are Predicate<T>, which uses boolean test(T t) to return true or false (Filter products by availability), and Function<T, R>, which uses R apply(T t) to take one type of value and return another (Convert Product to ProductDTO).

Question 2. What will be the output of the following code? Explain why.
The output is Hello from Greeting followed by And hello from Person. Since both interfaces provide the same default sayHello() method, the Person class must override it to resolve the conflict. Inside the overridden method, Greeting.super.sayHello() specifically calls the Greeting version, and then the Person class prints its own message. The Farewell version is not called.

Question 3. Convert the following anonymous class to a lambda expression. Explain each step of the
conversion process.
This can be converted to
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length(); This works because Comparator is a functional interface with one abstract method, compare(). First, the anonymous class and @Override are removed. Next, the method parameters s1 and s2 are kept, and the lambda operator -> is added. Since the method body contains only one return expression, the braces and return keyword can also be removed.

Question 4. Which of the following lambda expressions are valid? For invalid ones, explain the reason.
A, D, E, and F are valid lambda expressions, while B and C are invalid. A is valid because Runnable requires no parameters and returns nothing. B is invalid because return cannot be used without braces; it should be s -> s.isEmpty(). C is invalid because Function must return a value, so the body should include return x * 2. D is valid because Consumer accepts one argument and returns nothing. E is valid because BiFunction takes two arguments and returns their sum. F is also valid because Supplier takes no arguments and returns a String.

Question 5. Match each lambda expression with its corresponding method reference. Explain the type of each
method reference (Static, Bound Instance, Unbound Instance, or Constructor).
The correct matches are 1-B, 2-D, 3-C, 4-A, and 5-E. System.out::println is a bound instance reference because it uses the existing System.out object. String::toUpperCase is an unbound instance reference because the String object is provided when the method is called. Math::abs is a static method reference because abs() is a static method. ArrayList::new is a constructor reference because it creates a new ArrayList. Finally, String::compareTo is an unbound instance reference because the first String becomes the object that calls compareTo() on the second String.

Question 6. What is the difference between Optional.of() and Optional.ofNullable()? What will
happen when executing the following code?
Optional.of() only accepts non-null values and throws a NullPointerException if the value is null. In comparison, Optional.ofNullable() allows null values and creates an empty Optional when null is provided. In this example, the program throws a NullPointerException when Optional.of(value) is executed, so the remaining code does not run. If only Optional.ofNullable(value) were used, isPresent() would return false.

Question 7. What will be the output of the following code? Explain the difference between orElse() and
orElseGet().
The output shows that Creating default value is printed when orElse() is used, even though the Optional already contains "Hello". This happens because orElse() always evaluates its default value. In contrast, orElseGet() only creates the default value when the Optional is empty. Since the value "Hello" is already present, createDefault() is not called with orElseGet(). Both methods return "Hello" in this example.

Question 8. Explain the difference between map() and flatMap() in Stream API. Given the following class
structure, write code to get a list of all product names from all orders.
map() changes each stream element into another value, while flatMap() is used to combine nested collections or streams into one stream. In this example, flatMap()collects the products from all orders into a single product stream, and map() converts each product into its name. The final list can be created with orders.stream().flatMap(order -> order.getProducts().stream()).map(Product::getName).collect(Collectors.toList());.

Question 9. What will be the output of the following code? Explain the concept of lazy evaluation in Stream
API.
The output is tream created, Calling findFirst..., Filtering: 1, Filtering: 2, Mapping: 2, and Result: 20. This demonstrates lazy evaluation because filter() and map() do not execute when the stream is created. They start running only when the terminal operation findFirst() is called. The stream checks 1, which does not pass the filter, then checks 2, which passes and is converted to 20. Since findFirst() only needs the first matching value, the remaining numbers are not processed.

Question 10. Analyze the following code. What is wrong with it? How would you fix it?
The code has two main issues. First, processProducts() checks isPresent() and then calls get(), which works but can be written more cleanly using ifPresent(). Second, calculateTotal() returns null when the product list is null or empty. Since the method returns an Optional, it should return Optional.empty() instead. A better approach is to use Optional methods consistently and avoid returning null.






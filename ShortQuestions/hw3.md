Question 1. What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.
A functional interface is an interface that has exactly one abstract method. It's the foundation of lambda expressions in Java 8. Since there's only one abstract method, the compiler knows exactly which method a lambda is implementing.
The annotation is @FunctionalInterface, and it's not mandatory. Any interface with a single abstract method works with lambdas, with or without it.
Predicate<T>. Its abstract method is boolean test(T t). It takes one argument and returns a boolean, so it represents a condition.
Function<T, R>. Its abstract method is R apply(T t). It takes an input of type T and returns a result of type R, so it represents a transformation.



Question 2.
Hello from Greeting
And hello from Person
THe person implement both interface, so it has to override the same method, in the override method, it called the interface greeting's sayhello first, then output one line by it self.

Question 3. Convert the following anonymous class to a lambda expression. Explain each step of the
conversion process.
step 1: (String s1, String s2) { return s1.length() - s2.length(); } //delete the type, method name
step 2: (String s1, String s2) -> { return s1.length() - s2.length(); } //add the ->
step 3: (s1, s2) -> { return s1.length() - s2.length(); } //ignore the type
final :Comparator<String> comparatpr = (s1, s2) -> s1.length() - s2.length();

Question 4. Which of the following lambda expressions are valid? For invalid ones, explain the reason.
// A
Runnable r = () -> System.out.println("Running");
// B
Predicate<String> p = s -> return s.isEmpty(); // can not return without {}
// C
Function<Integer, Integer> f = x -> { x * 2; }; // has to return in {}
// D
Consumer<String> c = (String s) -> System.out.println(s);
// E
BiFunction<Integer, Integer, Integer> bi = (a, b) -> a + b;
// F
Supplier<String> sup = () -> { return "Hello"; };

Question 5. Match each lambda expression with its corresponding method reference. Explain the type of each method reference (Static, Bound Instance, Unbound Instance, or Constructor)
1 - B Bound Instance, the obejct is known
2 - D Unbound Instance
3 - C Static，abs is a static method, no object. call directly
4 - A Constructor, create a new object
5 - E Unbound Instance, the obejct is unknown until we see the parameter

Question 6. What is the difference between Optional.of() and Optional.ofNullable()? What will
happen when executing the following code?
The second line will throw nullpointerexception until program ends. Optional.of() requires a non-null value. If you pass null, it throws a NullPointerException immediately. Optional.ofNullable() accepts a value that might be null. If the value is null, it returns an empty Optional instead of throwing.

Question 7. What will be the output of the following code? Explain the difference between orElse() and
orElseGet().
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello
orElse takes a value. In opt.orElse(createDefault()), Java has to evaluate the argument before it can call orElse, so createDefault() always executes. Then orElse sees that the Optional contains 'Hello' and returns it, and the default value is just thrown away.

orElseGet takes a Supplier, which is a function. The lambda () -> createDefault() is only defined, not executed. orElseGet calls it only if the Optional is empty. Here it has a value, so the supplier is never invoked.

Question 8. Explain the difference between map() and flatMap() in Stream API. Given the following class structure, write code to get a list of all product names from all orders.
List<String> allProductNames = orders.stream()
        .flatMap(order -> order.getProducts().stream())  // Stream<Order> -> Stream<Product>
        .map(Product::getName)                           // Stream<Product> -> Stream<String>
        .collect(Collectors.toList());                   // Stream<String> -> List<String>
map transfers each single element one to one, and flatmap transfers all of those element into one stream.

Question 9. What will be the output of the following code? Explain the concept of lazy evaluation in Stream
API.
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20

This shows lazy evaluation in the Stream API, which has three important aspects.

First, intermediate operations don't run until a terminal operation is called. filter and map are intermediate operations. They just build up a pipeline and store the lambdas. Nothing executes when the stream is created, which is why 'Stream created' and 'Calling findFirst...' are printed before any filtering happens. Execution only starts when findFirst(), a terminal operation, is called.

Second, elements are processed one at a time through the whole pipeline, not stage by stage. The stream doesn't filter all five numbers first and then map the results. Instead, element 1 goes through the filter and is rejected. Then element 2 passes the filter and immediately goes to map. That's why the filtering and mapping output is interleaved.

Third, findFirst() is a short-circuiting operation. As soon as element 2 produces a result, 20, the stream stops. Elements 3, 4, and 5 are never processed at all.


Question 10. Analyze the following code. What is wrong with it? How would you fix it?
1. public void processProducts(Optional<List<Product>> productsOpt) The optional does not need to apeear in the parameter, if caller passes a null, the ispresent method will checl on it.

2. Optional<List<Product>> is not necessary, the empty list already be expressed by the list, it is already safe.

3.The method calculateTotal return a null but has Optional<BigDecimal> when declare the method, it will cause nullpointerexception, it should return optional method.

4.productsOpt.isPresent() can be replace by productsOpt.ifPresent(...), which is the right way to use optinal method.
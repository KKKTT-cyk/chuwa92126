Conceptual Questions:
1. Definition: A functional interface is an interface that has only one abstract method and can have multiple default 
and static methods, also has @FunctionalInterface annotation for compile-time validation and use lambda expressions 
implements the abstract method.

Use @FunctionalInterface as annotation to mark as a functional interface, which is mandatory.

Two examples:
Consumer<T> represents an operation that takes one argument and returns no result. Its abstract method is void 
accept(T t) where T is the type of the input. 

Predict<T> represents an operation that takes one argument and returns a boolean value. Its abstract method is boolean 
test(T t) where T is the type of the input. 

2. 
Output:
"Hello from Greeting"
"And hello from person"

Reason: Once the instance person p is created and p calls the method sayHello(), and the class Person implements two 
interfaces Greeting and Farewell. In the overridden method sayHello() in class Person, it first calls the sayHello()
method in Greeting interface, and it will first print "Hello from Greeting" and then print "And hello from person".

3. 
Comparator<String> comparator= (s1, s2) -> s1.length-s2.length;
Explanation:
First the Comparator<String> is a functional interface which it has only one abstract method int compare(T o1, T o2),
thus it could use Lambda expression.
Then we need to delete anonymous class and method signature when we use Lambda expression, thus we only keep method 
parameter and method body: (String s1, String s2) -> {return s1.length() - s2.length();}
Then omit the parameter type String, {} and return.

4. 
valid:
A, D, E, F
invalid:
B: Only return without {}, because return and {} should be showed together, or we don't use either of them. 
C: Only {} without return, because return and {} should be showed together, or we don't use either of them. 

5. 
1 - B Bound Instance method reference, because println() is called on the specific object System.out.
2 - D Unbound Instance method reference, because the String object is provided as the lambda expression.
3 - C Static method reference, because abs() is a static method of the Math class.
4 - A Constructor reference, because it refers to the ArrayList constructor.
5 - E Unbound Instance method reference, because the first parameter becomes the object that calls compareTo().

6. 
Difference:
Optional.of(value) is used when value is guaranteed non-null and if value is null, it throws NPE, 
while Optional.ofNullable(value) is used when value might be null and if value is null, it returns empty Optional.

In this code, Optional.of(value) throws a NPE and the remaining statements are not executed and nothing is printed.

7. 
Output：
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello

Difference between orElse() and orElseGet():
The method orElse(default) will return value if the value presents, when the value is absent, it will return default,
while orElseGet(supplier) will also return value if the value presents, when the value is absent, it will call supplier.
And when the default is cheap and simple, we use orElse(), when the default is expensive to compute, we choose orElseGet().

8. 
Difference of map() and flatMap():
Method map() is to transform each stream element into another value and returns a stream of the transformed value.
While flatMap() transforms each element into a stream and then combines all the resulting streams into one stream.

Code:
List<String> allProductsNames = order.stream()
        .flatMap(order -> order.getProducts().stream())
        .map(Product::getName)
        .collect(Collectors.toList())

9. 
Output:
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20

Explanation of lazy evaluation in stream API:
The lazy evaluation in stream API means intermediate operations like filter() will not be executed when they are 
declared, and they are only executed when a terminal operation is called.

In this code, creating stream does not execute filter() and map(). The process begins when findFirst() is called. The 
number 1 is checked but it does not pass the filter. Then number 2 is checked and passes the filter. Then number 2 goes 
into map and mapped to 20. Since findFirst() is a terminal operation, the stream stopped after finding 20.

10. 
Problems:
It is unnecessary to use Optional<List<Product>> as parameter.
Fix: change it to List<Product>
Using get() after isPresent() redundant.
Fix: use product.forEach(this::process)
A method returning Optional<BigDecimal>, thus return null is not correct. It should return Optional.empty().
Fix: return Optional.empty()
reduce(BigDecimal.ZERO, BigDecimal::add) always produce a BigDecimal, so wrapping it as Optional is not necessary.
Fix: use reduce(BigDecimal::add)













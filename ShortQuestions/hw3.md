1. A Functional Interface is an interface with exactly one abstract method. It can be marked with @FunctionalInterface, but the annotation is not required. For example, Predicate<T> has boolean test(T t), and Function<T, R> has R apply(T t)
2. Hello from Greeting
   And hello from Person
3. Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();Comparator has one abstract method, compare, so it can be written as a lambda. Remove the anonymous class and compare declaration, keep its parameters and calculation, and use -> between them.
4. A: Runnable does not return a value. D:The parameter type is explicit, and Consumer returns nothing.
E: The expression returns the sum of the two inputs.F：The block returns a String.
5. 1-B, 2-D, 5-E, 3-C, 4-A 
6. Optional.of() requires a non-null value and throws a NullPointerException for null. Optional.ofNullable() accepts null and creates an empty Optional.
7. --- Using orElse ---
   Creating default value
   Result: Hello
   --- Using orElseGet ---
   Result: Hello
8. map() transforms each stream element into one result. flatMap() transforms each element into a stream, then combines those streams into one.
   List<String> allProductNames = orders.stream()
   .flatMap(order -> order.getProducts().stream())
   .map(Product::getName)
   .collect(Collectors.toList());
9. Stream created
   Calling findFirst...
   Filtering: 1
   Filtering: 2
   Mapping: 2
   Result: 20
   Streams use lazy evaluation: creating the stream does not run filter() or map(). They run when the terminal operation findFirst() is called. It checks 1, then finds the first even number (2), maps it to 20, and stops—so 3, 4, and 5 are never processed.
10. The main error is return null in a method that returns Optional<BigDecimal>. It should return Optional.empty()
    public Optional<BigDecimal> calculateTotal(List<Product> products) {
    if (products == null || products.isEmpty()) {
    return Optional.empty();
    }
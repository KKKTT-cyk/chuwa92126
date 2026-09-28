Question 1:
1.1: A Functional Interface is an interface that contains exactly one abstract method. It can still contain default methods and static methods.
1.2: The annotation used is: @FunctionalInterface
1.3: This annotation is not mandatory. An interface is still a functional interface if it has exactly one abstract method even without the annotation. However, @FunctionalInterface is recommended because the compiler can check that the interface follows the functional-interface rules.
1.4: 
example1: Predicate<T>,  
Its abstract method is:  boolean test(T t);
It accepts one value and returns a boolean.

example 2: Function<T, R>
Its abstract method is: R apply(T t);
It accepts one value of type T and returns a value of type R.

Question 2:
The output is:
Hello from Greeting
And hello from Person

Both Greeting and Farewell provide a default method named:
sayHello()

Since Person implements both interfaces, Java cannot automatically decide which default implementation should be used. Therefore, Person must override sayHello().
Inside the override:
Greeting.super.sayHello();

explicitly calls the default method from the Greeting interface.
Then:
System.out.println("And hello from Person");

prints the second line.
The Farewell implementation is never called.

Question 3:
Because Comparator is a functional interface, it can be replaced by a lambda expression.
First, remove the anonymous class declaration:
(s1, s2) -> {
return s1.length() - s2.length();
}

Because there is only one expression, the braces and return can also be removed:
Comparator<String> comparator =
(s1, s2) -> s1.length() - s2.length();

This is the final lambda expression.

Question 4:
A
Runnable r = () -> System.out.println("Running");
Valid.
Runnable.run() takes no parameters and returns void.

B
Predicate<String> p = s -> return s.isEmpty();
Invalid.
When return is used, braces are required.

C
Function<Integer, Integer> f = x -> { x * 2; };
Invalid.
Function requires a return value, but this lambda does not return anything.

D
Consumer<String> c = (String s) -> System.out.println(s);
Valid.
Consumer.accept() accepts one argument and returns void.

E
BiFunction<Integer, Integer, Integer> bi = (a, b) -> a + b;
Valid.
It takes two Integer values and returns an Integer.

F
Supplier<String> sup = () -> { return "Hello"; };
Valid.
Supplier.get() takes no arguments and returns a value.

Question 5:
| Lambda | Method Reference | Type |
| 1. `x -> System.out.println(x)` | `System.out::println` | Bound instance |
| 2. `s -> s.toUpperCase()` | `String::toUpperCase` | Unbound instance |
| 3. `x -> Math.abs(x)` | `Math::abs` | Static |
| 4. `() -> new ArrayList<>()` | `ArrayList::new` | Constructor |
| 5. `(s1, s2) -> s1.compareTo(s2)` | `String::compareTo` | Unbound instance |

Question 6:
Optional.of() requires a non-null value. Optional.ofNullable() allows either a non-null or null value.
The program actually stops at:
Optional.of(value);

because it throws a NullPointerException.
Therefore, opt2 is never created and neither println() executes.

Question 7:
The output is:
--- Using orElse ---
Creating default value
Result: Hello
--- Using orElseGet ---
Result: Hello

The important difference is how the default value is evaluated.
With:
opt.orElse(createDefault());
createDefault() is executed even though opt already contains "Hello".
Therefore:
Creating default value
is printed.
With:
opt.orElseGet(() -> createDefault());
the supplier executes only if the Optional is empty.
Since opt contains "Hello", createDefault() is not executed the second time.

Question 8:
map() transforms each stream element into another element.flatMap() is useful when each element contains another collection or stream. It transforms the nested streams and flattens them into one stream.
List<String> allProductNames =
orders.stream()
.flatMap(order -> order.getProducts().stream())
.map(Product::getName)
.collect(Collectors.toList());

Question 9:
The output is:
Stream created
Calling findFirst...
Filtering: 1
Filtering: 2
Mapping: 2
Result: 20

Because findFirst() is a short-circuiting terminal operation, Java does not need to process 3, 4, or 5.
This demonstrates lazy evaluation: intermediate Stream operations are delayed until a terminal operation requires their results.

Question 10:
There are two main problems.
The first is this pattern:
if (productsOpt.isPresent()) {
List<Product> products = productsOpt.get();
}

It works, but it does not make good use of Optional. It can be simplified with ifPresent().
The second and more serious problem is:
if (products == null || products.isEmpty()) {
return null;
}

A method returning Optional<BigDecimal> should normally return:
Optional.empty()

instead of null.

A better implementation is:
public class ProductService {

    public void processProducts(
            Optional<List<Product>> productsOpt) {

        productsOpt.ifPresent(
                products ->
                        products.forEach(this::process)
        );
    }

    public Optional<BigDecimal> calculateTotal(
            List<Product> products) {

        if (products == null || products.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal total =
                products.stream()
                        .map(Product::getPrice)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return Optional.of(total);
    }

    private void process(Product product) {
        // process product
    }
}

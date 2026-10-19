## Hw3 Java 8 New Features

### Dengtai Wang

#### Conceptual Questions

1. **What is a Functional Interface? What annotation is used to mark a Functional Interface, and is this annotation mandatory? Give two examples of built-in Functional Interfaces in Java 8 and explain their abstract method signatures.**

   Answer:

   A Functional Interface is an interface that has only one abstract method. It can still have default methods and static methods.

   We can use `@FunctionalInterface` to mark it. This annotation is not mandatory, but it can help compiler check if the interface really has only one abstract method.

   Two examples are:

   Predicate\<T\>, It takes one parameter and returns a boolean value, example using for filter products by availability.

   Function<T, R>, It takes one parameter of type T and returns a result of type R.
   
2. **What will be the output of the following code? Explain why.**

   ```java
   interface Greeting {
     default void sayHello() {
       System.out.println("Hello from Greeting");
     }
   }

   interface Farewell {
     default void sayHello() {
       System.out.println("Hello from Farewell");
     }
   }

   class Person implements Greeting, Farewell {
     @Override
     public void sayHello() {
       Greeting.super.sayHello();
       System.out.println("And hello from Person");
     }
   }

   public class Test {
     public static void main(String[] args) {
       Person p = new Person();
       p.sayHello();
     }
   }
   ```

   Answer:

   Output is:

   ```text
   Hello from Greeting
   And hello from Person
   ```

   Because both interfaces have same default method sayHello(), Person must override it to solve the conflict. Greeting.super.sayHello() specifically calls the default method from Greeting, then Person prints its own message.

3. **Convert the following anonymous class to a lambda expression. Explain each step of the conversion process.**

   ```java
   Comparator<String> comparator = new Comparator<String>() {
     @Override
     public int compare(String s1, String s2) {
       return s1.length() - s2.length();
     }
   };
   ```

   Answer:

   First remove the anonymous class and method name:

   ```java
   (String s1, String s2) -> {
     return s1.length() - s2.length();
   }
   ```
   
   then remove the types:

   ```java
   (s1, s2) -> {
     return s1.length() - s2.length();
   }
   ```
   
   Because there is only one return expression, we can also remove {}, and `return`:

   ```java
   Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();
   ```
   
4. **Which of the following lambda expressions are valid? For invalid ones, explain the reason.**

   ```java
   // A
   Runnable r = () -> System.out.println("Running");

   // B
   Predicate<String> p = s -> return s.isEmpty();

   // C
   Function<Integer, Integer> f = x -> { x * 2; };

   // D
   Consumer<String> c = (String s) -> System.out.println(s);

   // E
   BiFunction<Integer, Integer, Integer> bi = (a, b) -> a + b;

   // F
   Supplier<String> sup = () -> { return "Hello"; };
   ```

   Answer:

   A is valid. Runnable takes no parameter and returns void.

   B is invalid. If we use `return`, we need `{}`:

   ```java
   Predicate<String> p = s -> { return s.isEmpty(); };
   ```

   C is invalid because Function has to return a value, but there is no return statement. It can be fixed as:

   ```java
   Function<Integer, Integer> f = x -> x * 2;
   ```

   D is valid. Consumer takes one parameter and returns void.

   E is valid. BiFunction takes two parameters and returns one result.
   
   F is valid. Supplier takes no parameter and returns a value.

5. **Match each lambda expression with its corresponding method reference. Explain the type of each method reference (Static, Bound Instance, Unbound Instance, or Constructor).**

   ```java
   // Lambda expressions:
   // 1. x -> System.out.println(x)
   // 2. s -> s.toUpperCase()
   // 3. x -> Math.abs(x)
   // 4. () -> new ArrayList<>()
   // 5. (s1, s2) -> s1.compareTo(s2)

   // Method references:
   // A. ArrayList::new
   // B. System.out::println
   // C. Math::abs
   // D. String::toUpperCase
   // E. String::compareTo
   ```

   Answer:

   1 -> B: `System.out::println`, Bound Instance Method Reference.

   2 -> D: `String::toUpperCase`, Unbound Instance Method Reference.

   3 -> C: `Math::abs`, Static Method Reference.

   4 -> A: `ArrayList::new`, Constructor Reference.

   5 -> E: `String::compareTo`, Unbound Instance Method Reference.

6. **What is the difference between Optional.of() and Optional.ofNullable()? What will happen when executing the following code?**

   ```java
   String value = null;
   Optional<String> opt1 = Optional.of(value);
   Optional<String> opt2 = Optional.ofNullable(value);
   
   System.out.println(opt1.isPresent());
   System.out.println(opt2.isPresent());
   ```

   Answer:

   Optional.of() only accepts a non-null value. If the value is null, it throws `NullPointerException`.

   Optional.ofNullable() can accept null. If the value is null, it creates Optional.empty().

   In this code, the program throws NullPointerException at Optional\<String\> opt1 = Optional.of(value);

   So the rest of code will not run.
   
7. **What will be the output of the following code? Explain the difference between orElse() and orElseGet().**

   ```java
   public class Test {
     public static String createDefault() {
       System.out.println("Creating default value");
       return "Default";
     }

     public static void main(String[] args) {
       Optional<String> opt = Optional.of("Hello");

       System.out.println("--- Using orElse ---");
       String result1 = opt.orElse(createDefault());
       System.out.println("Result: " + result1);

       System.out.println("--- Using orElseGet ---");
       String result2 = opt.orElseGet(() -> createDefault());
       System.out.println("Result: " + result2);
     }
   }
   ```

   Answer:

   Output is:

   ```text
   --- Using orElse ---
   Creating default value
   Result: Hello
   --- Using orElseGet ---
   Result: Hello
   ```

   Because orElse() will evaluate its default value even if Optional already has a value, so createDefault() is still called.

   But orElseGet() only calls the Supplier when Optional is empty. Since opt already contains "Hello", createDefault() is not called for orElseGet().

8. **Explain the difference between map() and flatMap() in Stream API. Given the following class structure, write code to get a list of all product names from all orders.**

   ```java
   class Order {
     private List<Product> products;
     public List<Product> getProducts() { return products; }
   }

   class Product {
     private String name;
     public String getName() { return name; }
   }
   ```

   Answer:

   map() changes each element into another value. flatMap() is used when each element contains another collection or stream, and we want to combine them into one stream.

   Here each Order has a list of Product, so we use flatMap() first, then use map() to get product names:

   ```java
   List<String> allProductNames = orders.stream()
       .flatMap(order -> order.getProducts().stream())
       .map(product -> product.getName())
       .collect(Collectors.toList());
   ```

9. **What will be the output of the following code? Explain the concept of lazy evaluation in Stream API.**

   ```java
   List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

   Stream<Integer> stream = numbers.stream()
       .filter(n -> {
         System.out.println("Filtering: " + n);
         return n % 2 == 0;
       })
       .map(n -> {
         System.out.println("Mapping: " + n);
         return n * 10;
       });

   System.out.println("Stream created");
   System.out.println("Calling findFirst...");

   Optional<Integer> result = stream.findFirst();

   System.out.println("Result: " + result.orElse(-1));
   ```

   Answer:

   Output is:

   ```text
   Stream created
   Calling findFirst...
   Filtering: 1
   Filtering: 2
   Mapping: 2
   Result: 20
   ```

   Stream operations like filter() and map() are lazy, so they do not run when the stream is created. They only start when a terminal operation like findFirst() is called.

   findFirst() only needs the first matching result. It checks 1 first, but 1 does not pass filter. Then 2 passes filter and is mapped to 20. After finding 20, the stream stops, so 3, 4 and 5 are not processed.

10. **Analyze the following code. What is wrong with it? How would you fix it?**

    ```java
    public class ProductService {
      public void processProducts(Optional<List<Product>> productsOpt) {
        if (productsOpt.isPresent()) {
          List<Product> products = productsOpt.get();
          for (Product p : products) {
            process(p);
          }
        }
      }
    
      public Optional<BigDecimal> calculateTotal(List<Product> products) {
        if (products == null || products.isEmpty()) {
          return null;
        }
    
        BigDecimal total = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        return Optional.of(total);
      }
    }
    ```

    Answer:

    First problem is using isPresent() and get() manually. It works, but we can use ifPresent() to make Optional cleaner:

    ```java
    productsOpt.ifPresent(products ->
        products.forEach(this::process)
    );
    ```

    Second problem is returning null from a method that returns Optional. If there is no value, it should return Optional.empty() instead:

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

    Optional is used to represent whether a value exists, so returning null from an Optional method should be avoided.

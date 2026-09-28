# Q1.
An functional interface is interface with exactly one abstract method.
@FunctionalInterface annotation is used to mark a Functional Interface. and it's optional.
Consumer<T> with void accept(T t) 
and BiConsumer<T, U> with void accept(T t, U u) are 2 built-in interfaces.

# Q2.
The output is:
Hello from Greeting
And hello from Person

Because Person implements both Greeting and Farewell, they both have default sayHello method,
so Person has to override the method, and Greeting.super.sayHello calls the method from Greeting.

# Q3.
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length()
remove the class declare and override, method name, and remove parameter types, 
if there is only one return code in the code, the curly braces and return can also be omitted.

# Q4.
A is valid.
B is invalid, dont need a `return` here.
C is invalid, dont need `{}` here.
D is valid, we can indicate the parameter type.
E is valid.
F is valid.

# Q5.
1. is a Bound Instance, because System.out is a object.
2. is an Unbound Instance, because parameter calls this method.
3. is static method refer, because calls a static method of Math.
4. is Constructor method refer, because creates a new instance using constructor.
5. is an Unbound Instance, because parameter calls this method.

# Q6.
Optional.of will threw an NullPointerException
while Optional.ofNullable returns a empty Optional.

# Q7. 
thw output is:

--- Using orElse ---  
Creating default value  
Result: Hello  
--- Using orElseGet ---  
Result: Hello  

orElse will return default value when value is absent, and the method will be executed.
orElseGet() will return default value, and the default value is not empty so the supplier will be executed when call get method.

# Q8.
Map is used when a function returns a normal value, and the result will be automatically wrapped. 
FlatMap is used when the function itself returns Optional or Stream, and it expands nested containers into a layer.

`        orders.stream().flatMap(order -> order.getProducts().stream()).map(Product::getName).toList()
`
# Q9.
the output is:  
Stream created  
Calling findFirst...  
Filtering: 1  
Filtering: 2  
Mapping: 2  
Result: 20  

the lazy operations dont execute until a terminal operation is called.


# Q10.
`if (productsOpt.isPresent())     List<Product> products = productsOpt.get();` this code doesnt use the optional feature, should use `ifPresent`.
the return type is `Optional<BigDecimal>`, but actually return a null.
`Product::getPrice` may return a null, and then there will be a `NullPointerException` after calling `reduce`.







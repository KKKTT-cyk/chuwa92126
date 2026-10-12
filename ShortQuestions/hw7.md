1. Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates (a single product vs. a family of matching products).
Feature	|Factory Method	|Abstract Factory
Purpose	|Creates one type of product	|Creates a family of related products
Implementation	|Typically uses inheritance and method overriding |	Typically uses composition with multiple factory methods
Product types	|One product interface per factory method	| Multiple related product interfaces
Example	|CarFactory creates Car	| WindowsFactory creates Windows buttons and checkboxes
Complexity|	Generally simpler	|Generally more complex

2. Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly, and describe how the Builder pattern solves this problem.
2.1
   First, poor readability. When a constructor contains many parameters, it becomes difficult to understand the purpose of each argument, especially when multiple parameters share the same data type.
   Second, increased risk of errors. Developers may accidentally provide arguments in the wrong order. When parameters have compatible types, these mistakes might not produce compilation errors.
   Third, poor maintainability. Supporting additional optional parameters requires creating more overloaded constructors, increasing code complexity and making future modifications difficult.
2.2
   The Builder pattern separates the construction of a complex object from its representation. Instead of passing numerous parameters to a constructor, developers construct an object step by step using clearly named methods.
   For example:
   User user = new User.Builder("John")
   .age(25)
   .email("john@example.com")
   .phone("1234567890")
   .build();


This approach improves readability because each parameter is identified by its corresponding method name. Optional parameters can be omitted, and the build() method can validate the object's configuration before creating it.
The Builder pattern also reduces the need for overloaded constructors and supports the creation of immutable objects.

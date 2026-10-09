# Java Design Patterns Homework

1. Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates
(a single product vs. a family of matching products).
Factory Method is used to create a single type of product, where subclasses decide which specific object to create. Abstract Factory is used to create a family of related products that are designed to work together. For example, a Factory Method can create a Car or Bike, while an Abstract Factory can create matching UI components, such as buttons and checkboxes for Windows or Mac.


2. Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly, and describe how the Builder pattern solves this problem.
Constructors with many parameters are hard to use because it is difficult to remember what each parameter means and easy to pass values in the wrong order. The Builder pattern allows us to create objects step by step using clearly named methods, which makes the code easier to read, more flexible, and less error-prone.
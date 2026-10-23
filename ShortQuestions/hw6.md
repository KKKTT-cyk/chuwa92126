1. Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates  (a single product vs. a family of matching products).
Factory method means we can flexiably create object without knowing the real object, it creates a single product and lets subclasses decide which object to create, factory creates a family of related and matching products without exposing their concrete classes.     





2. Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly,
and describe how the Builder pattern solves this problem.
Becauuse it is easy to forget the order of the arguments when you have too many parameters, the builder pattern solves this by giving each parameter a clear method and then creating the object with build.
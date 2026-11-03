# Homework 2

## Question 1
Answer:
Method overloading means using the same method name with different parameter lists. Java chooses the matching method based on the arguments. It is compile-time polymorphism.

Method overriding means a subclass provides a new implementation of a method from its parent class. It is runtime polymorphism.

## Question 2
Answer:
The output is: Bark! 
Dog override the Animal makeSound method. Although the reference variable a is declared as Animal, it actually refers to a Dog object.

## Question 3
Answer:Java does not support multiple inheritance with classes because it can cause ambiguity.

The Diamond Problem happens when a class inherits the same method from multiple parent classes, and Java would not know which implementation to use.

Java solves this by allowing a class to extend only one class. However, a class can implement multiple interfaces.

## Question 4
Answer:The code will not compile.

Shape is an abstract class, and abstract classes cannot be instantiated directly using new Shape(). A concrete subclass must extend Shape and implement the abstract getArea() method before an object can be created.

## Question 5
Answer:An abstract class is a partially defined parent class. It can have fields, constructors, concrete methods, and abstract methods.

An interface is more like a contract that describes what a class can do. A class can implement multiple interfaces, but it can extend only one class.

I would use an interface when a class needs multiple capabilities. For example, a Duck can implement both Flyable and Swimmable.

## Question 6
Answer:The output is:

Animal eating
Meow!

`Animal a = new Cat();` is upcasting. A Cat object is being referenced by its parent type, Animal.

The call `a.eat()` works because `eat()` is defined in Animal.

The call `a.meow()` would not compile because the reference type of `a` is Animal, and Animal does not have a `meow()` method.

`a instanceof Cat` checks whether the object referenced by `a` is actually a Cat.

`Cat c = (Cat) a;` is downcasting. It converts the Animal reference back to a Cat reference, so `c.meow()` can be called.

## Question 7
Answer:
The equals() method should behave like a proper equality comparison. It should be reflexive, symmetric, transitive, consistent, and return false when compared with null.

If two objects are equal according to equals(), they must also have the same hashCode(). Therefore, when equals() is overridden, hashCode() should also be overridden using the same fields. This is important for hash-based collections such as HashMap and HashSet.

## Question 8
Answer:A shallow copy creates a new outer object, but referenced objects inside it are still shared.

For example, if a Person has an Address field, a shallow copy will make both Person objects refer to the same Address object. Changing the address through one Person may affect the other.

A deep copy creates copies of both the Person object and the Address object. Therefore, each Person has its own separate Address object, and changes to one will not affect the other.

## Question 9
Answer:The output is:

Taking off from Flyable

Both Flyable and Swimmable provide a default takeOff() method, so Duck must override takeOff() to resolve the conflict.

Inside Duck, `Flyable.super.takeOff()` explicitly calls the default takeOff() method from the Flyable interface.


## Question 10
Answer:
Method A is a valid overload because it has a different number of parameters.

Method B is a valid overload because it has different parameter types.

Method C is not a valid overload because it has the same parameter list as the original method. Changing only the return type is not enough for method overloading.

Method D is not a valid overload because parameter names and access modifiers are not part of the method signature. It still has the same signature: calculate(int, int).
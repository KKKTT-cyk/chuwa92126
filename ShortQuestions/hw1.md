## Question1.
Method overloading occurs when multiple methods in the same class have the same method name but different parameter lists.
The compiler determines which overloaded method to call based on the arguments. Therefore, method overloading is considered compile-time polymorphism, also called static polymorphism.

Method overriding occurs when a subclass provides its own implementation of a method inherited from its superclass. The method must have the same name and compatible parameter and return types.
The method that executes is determined by the actual object at runtime. Therefore, method overriding represents runtime polymorphism, also called dynamic polymorphism.

## Question2.
The output is:  Bark!
Although the reference type is Animal, the actual object is a Dog. Java uses dynamic method dispatch for overridden instance methods. Therefore, Java executes the makeSound() implementation belonging to Dog, not Animal.

## Question3.
Java does not support multiple inheritance with classes primarily because it can create ambiguity.
For example, suppose:

       A
      / \
     B   C
      \ /
       D

If both B and C override the same method inherited from A, and D inherits from both B and C, Java would have difficulty determining which implementation D should use.
This situation is known as the Diamond Problem.
Java avoids the Diamond Problem by allowing a class to extend only one class
However, Java allows a class to implement multiple interfaces.If two interfaces contain conflicting default methods, the implementing class must explicitly override the method and resolve the conflict itself.

## Question4.
The program will not compile.
An abstract class cannot be instantiated directly because it may contain abstract methods that do not have implementations.

## Question5.
An abstract class is a class that cannot be instantiated directly and may contain both abstract and concrete methods.
An abstract class can also contain instance variables, constructors, and methods with different access modifiers.
An interface primarily defines a contract describing what a class must be able to do.

A major difference is that a Java class can extend only one class, but it can implement multiple interfaces.

I would prefer an interface when unrelated classes need to share the same capability. For example, an Airplane, Bird, and Drone are very different objects, but all could implement:

interface Flyable {
void fly();
}

Using an interface allows them to share the Flyable behavior without requiring them to inherit from the same superclass.

## Question6.
The output is:

Animal eating
Meow!

First:

Animal a = new Cat();

This is upcasting.

A Cat object is being referenced using its superclass type Animal.

Upcasting is normally automatic:

Animal a = new Cat();

Because Animal does not have a meow() method, this would not compile:

a.meow();

That is why Line 3 is commented out.

The program then checks:

if (a instanceof Cat)

Since the actual object is a Cat, this condition is true.

Next:

Cat c = (Cat) a;

This is downcasting. The superclass reference is converted back to the subclass type.

Now:

c.meow();

is valid, producing: Meow!

## Question7.
When overriding equals(), the implementation should follow: Reflexive, Symmetric, Transitive, Consistent, Non-null.
When equals() is overridden, hashCode() should also be overridden because Java requires:
If two objects are equal according to equals(), they must produce the same hash code.

## Question8.
A shallow copy creates a new outer object but does not create copies of objects referenced by its fields.

Suppose:

class Person {
String name;
Address address;
}

With a shallow copy:

Person1 ──→ Address
↑
Person2 ──────┘

Both Person objects reference the same Address object.

Therefore, changing: person2.address.city = "Seattle";  could also appear when accessing:  person1.address.city

because they share the same Address.

A deep copy creates copies of the nested mutable objects as well.

Person1 ──→ Address1

Person2 ──→ Address2

Now the two Person objects have independent Address objects.

Therefore, modifying person2.address does not modify person1.address.

## Question9.
The output is:

Taking off from Flyable

Because Flyable.super.takeOff() explicitly tells Java to call the default implementation from the Flyable interface.

## Question10.
A valid overload must have a different parameter list, which can be the number of parameters and parameter types. Changing only the return type, parameter names, or access modifier does not create a new method signature.
so method A, B are valid, C,D are not valid.
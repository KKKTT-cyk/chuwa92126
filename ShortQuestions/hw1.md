1. 
Method overloading occurs when multiple methods have the same name but
different parameter lists. It is resolved at compile time, so it is a form
of compile-time polymorphism.

Method overriding occurs when a subclass provides its own implementation
of a method that is already defined in its superclass. The method that is
executed is determined at runtime based on the actual object type, so it
is a form of runtime polymorphism.

2. 
Output:

Bark!

Although the reference variable a has the type Animal, the actual object
is a Dog. Since Dog overrides makeSound(), Java uses dynamic method
dispatch at runtime and calls Dog's implementation of makeSound().
This demonstrates runtime polymorphism.

3. 
Java does not support multiple inheritance with classes because it can
create ambiguity and make the inheritance hierarchy more complicated.

The Diamond Problem occurs when a class inherits the same method from
multiple parent classes and Java cannot determine which implementation
should be used.

Java avoids this problem by allowing a class to extend only one class.
However, a class can implement multiple interfaces. If multiple interfaces
provide conflicting default methods, the implementing class must explicitly
override the method and resolve the conflict.

4. 
The code will not compile.

Shape is an abstract class, and abstract classes cannot be instantiated
directly. Therefore, the statement:

Shape s = new Shape();

causes a compile-time error.

To use Shape, we need to create a concrete subclass that implements
getArea() and instantiate that subclass instead.

5. 
An abstract class can contain instance fields, constructors, abstract
methods, and concrete methods. A class can extend only one abstract class.

An interface mainly defines a contract or capability that classes agree to
implement. A class can implement multiple interfaces.

I would prefer an interface when unrelated classes need to share the same
behavior. For example, Bird, Airplane, and Drone could all implement a
Flyable interface even though they do not share the same superclass.

6. 
Output:

Animal eating
Meow!

Animal a = new Cat();

is an example of upcasting. A Cat object is referenced using its parent
type Animal. Upcasting is safe and normally happens automatically.

Cat c = (Cat) a;

is downcasting. The Animal reference is explicitly converted back to Cat.
The instanceof check is used first to make sure the object is actually a
Cat and to avoid a ClassCastException.

The commented statement a.meow() would not compile because the reference
type Animal does not define a meow() method.

7. 
The equals() method should satisfy the following rules:

1. Reflexive: x.equals(x) must be true.
2. Symmetric: if x.equals(y) is true, y.equals(x) must also be true.
3. Transitive: if x equals y and y equals z, then x must equal z.
4. Consistent: repeated calls should return the same result if the objects
   have not changed.
5. x.equals(null) must return false.

When equals() is overridden, hashCode() should also be overridden because
equal objects must have the same hash code. This is important for
hash-based collections such as HashMap and HashSet.

8. 
A shallow copy creates a new outer object but copies references to nested
objects. Therefore, if Person has an Address field, both Person objects
will refer to the same Address object.

A deep copy creates copies of the nested objects as well. Therefore, each
Person object will have its own independent Address object.

With a shallow copy, modifying the address through one Person may affect
the other Person. With a deep copy, the two addresses are independent.

9. 
A shallow copy creates a new outer object but copies references to nested
objects. Therefore, if Person has an Address field, both Person objects
will refer to the same Address object.

A deep copy creates copies of the nested objects as well. Therefore, each
Person object will have its own independent Address object.

With a shallow copy, modifying the address through one Person may affect
the other Person. With a deep copy, the two addresses are independent.

10. 
Method A is a valid overload because it has a different number of
parameters.

Method B is a valid overload because it has different parameter types.

Method C is not a valid overload because it has the same method name and
the same parameter list as the original method. Changing only the return
type does not create an overload.

Method D is also not a valid overload because parameter names and access
modifiers are not part of the method signature. It still has the same
signature calculate(int, int).

Therefore:

Method A: Valid
Method B: Valid
Method C: Invalid
Method D: Invalid

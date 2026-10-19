Question 1:
Method overloading means multiple methods in the same class with the same name but different parameters. The timing of method overloading is during compile time so it belongs to compile time polymorphism. Method overriding mean as a specific implementation in a subclass of a method defined in its superclass. The timing of method overriding is during runtime so it is called runtime polymorphism.

Question 2:
The output is “Bark!”. Animal is the declared type that the compiler checks at compile time. Dog is the actual object type that is resolved at runtime. Since makeSound() is a overriding method implemented in the subclass Dog, Java looks at the actual object type of Dog at runtime and runs the overridden version of makeSound(), which prints “Bark!” instead of “Some sound”.

Question 3:
Java does not support multiple inheritance with classes because of the “Diamond Problem”: if multiple superclasses have a method with same signature, the subclass cannot figure out which method to inherit. Java solves this problem by supporting multiple inheritance through interfaces.

Question 4:
This will fail to compile with the error: “Shape is abstract, cannot be instantiated”. That is because Shape is declared as an abstract class. Abstract classes might have declarations of abstract methods that have no body nor implementation. Java forbids instantiating any abstract class at compile time to avoid the failure to run any potential abstract method at runtime. The abstract key word itself is what blocks instantiation. To make the code compile and run there needs to be a concrete subclass that implements the abstract method if any.

Question 5:
An abstract class shares common implementation among closely related classes with both abstract and concrete methods. It has constructors, can maintain state yet cannot support multiple inheritance. An interface defines what a class can do without specifying how, so it can only have public static final constants and cannot maintain state, yet it can support multiple inheritance.
A good example for preferring an interface is when unrelated classes need to share a capability rather than a common identity/state, also the classes can have multiple inheritance for multiple capabilities. For example,Duck needs to fly, swim, and walk, while Fish only needs to swim. By using the capability to fly (Flyable), to swim (Swimmable), and to walk (Walkable) as different interfaces, any concrete animal can implement multiple capability interfaces but not forced to implement all of them. If Animal were an abstract class with abstract fly() and swim() methods, Fish would be forced to implement a fly() method that makes no sense (as the material notes, it'd have to throw UnsupportedOperationException). Splitting each capability into its own interface (Flyable, Swimmable, Walkable) means each animal opts into exactly the abilities it actually has, while Animal stays an abstract class purely for the state and behavior every animal actually shares (like name and makeSound()).

Question 6:
The output will be "Animal eating" and "Meow!". Line 1 Animal a = new Cat() is upcasting. A Cat object is being reference through The variable a declared as the parent class Animal. At runtime, a points to an actual Cat object with the declared type Animal. Since Cat doesn't override eat(), so a.eat() inherits Animal's version. Upcasting defines by the direction of the cast from a child type to a parent type.
Downcasting means casting from a parent type to a child type (Animal to Cat). In the code, Cat c = (Cat) a is downcasting as it converts a reference from a parent type declared (Animal) back down to a child type (Cat). Downcasting needs safty check to confirms at runtime that the object a refers to is geniunely a Cat before attempting to avoid ClassCastException. By checking if (a instance of Cat), the downcasting of (Cat) a is safe to use.

Question 7:
The equals() contract requires: 1. Reflexive: x.equals(x) is true; 2. Symmetric: x.equals(y) if y.equals(x); 3. Transitive: x.equals(y) and y.equals(z) => x.equals(z). 4. Consistent: repeated calls return the same result if nothing changed. 5. Null-safe: x.equals(null) is false.
The reason that hashCode() has to be overriden along with equals() is that hashCode has the contract that if two objects are equal, their hash codes must be equal. If equals() is overriden but hashCode is not, then hash-based collections such as HashMap and HashSet which use hashCode() to pick a bucket will put the two equal objects into different buckets because of the different hash codes. This will break lookups of two equal keys inserted.

Question 8:
Shallow copy copies the object's fields as-is. It is fine for primitive / String fields but for a reference field, the copy just holds the same reference as the original one.
Deep copy recursively creates new copies of any referenced mutable objects.
For Person with Address address, since address is an object which is reference based, in a shallow copy, original.address and copy.address point to the same Address object. By modifying the address through one reference, both original.address and copy.address are affected. In a deep copy, the copy gets a brand new Address object with the same values, so changing to one Person's address never affect the other's.

Question 9:
The output will be "Taking off from Flyable".
The reason is although both interfaces Flyable and Swimmable declare a default takeOff() method, Duck explicitly use Flyable.super.takeOff() to choose the Flyable method to override. So the Flyable version is the one that prints.

Question 10:
Method A is valid because there are different number of parameters.
Method B is valid because the types of parameters are different.
Method C is invalid because only the return type is different yet the method's signature remains the same (calculate(int, int)). Therefore it's a duplicate-method compile error since differing only in return type doesn't count as overloading.
Method D is invalid since the method signature is identical to the original. Access modifier is not part of the signature so it also incurs a duplicate-method compile error.

Question 11 and 12 are programming ones. Please see Coding/hw1 directory.

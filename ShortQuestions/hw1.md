1. Method overloading: Same method name, different parameters; happens at compile time → compile-time polymorphism.
   Method overriding: Subclass provides a new implementation of a parent method; resolved at runtime → runtime polymorphism.
2. Bark!
3. Java does not support multiple inheritance with classes because it can create ambiguity. The Diamond Problem happens when both B and C inherit the same method from A, and D inherits from both of them. Java would not know which version D should use. Java solves this by allowing a class to extend only one class and allowing a class to implement multiple interfaces
4. It will not compile. Shape is an abstract class, so you cannot create an object from it directly.
5. Abstract class: can have instance variables, constructors, abstract methods, and normal methods. A class can extend only one abstract class. Interface: mainly defines behaviors/contracts. A class can implement multiple interfaces. Use an interface when different unrelated classes should share the same behavior.
   interface Eatable {
   void eat();
   } both Dog and Cat are different classes but they can both implement Eatable interface.
6. Animal eating
   Meow!
   upcasting: a Cat object is stored in an Animal reference. It happens automatically.
   downcasting: converting the Animal reference back to Cat.
7. equals() rules: reflexive, symmetric, transitive, consistent, and x.equals(null) must be false. If two objects are equal, they must have the same hashCode().Otherwise HashMap / HashSet may treat equal objects incorrectly.
8. Shallow copy: copies the Person object, but both copies share the same Address object. Deep copy: copies both the Person and the Address, so each has its own separate Address

9. Duck implements two interfaces with the same default method, so it must override takeOff() and choose which one to call. In this case, it was Flyable so it is “Taking off from Flyable”.
10. Valid overloads: A and B. A: valid — different number of parameters. B: valid — different parameter types. C: invalid — only return type is different. D: invalid — parameter names/access modifier are different, but parameter types are still (int, int). Rule: Overloading must change the parameter list, not just return type or access modifier.

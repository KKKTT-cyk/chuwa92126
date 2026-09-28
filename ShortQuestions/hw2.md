# Java OOP Homework 2

## Question 1. What is the difference between method overloading and method overriding? In which type of polymorphism does each belong?
We use overloading when multiple methods have the same name but different parameter lists. It is a compile-time polymorphism because the compiler determines which method to call based on the arguments.

While overriding is used when a subclass provides its own implementation of a method inherited from its superclass with the same method. It is runtime polymorphism because the method that is executed depends on the actual object type at runtime.


## Question 2. What will be the output of the following code? Explain why.
Output: 
"Bark!"

Though a is declared as an Animal, the actual object is a Dog. Dog overrides the makeSound() method, so the output of a.makeSound() is "Bark!"

## Question 3. Why does Java NOT support multiple inheritance with classes? What is the "Diamond Problem"? How does Java solve this issue?
Multiple inheritance could cause ambiguity. Diamond Problem is that child inherits conflicting implementations of the same method from multiple parents.
To solve this issue, a class can implement multiple interfaces.


## Question 4. What will happen when you try to compile and run the following code? Explain your answer.
The code does not work because Shape is an abstract class, and abstract classes cannot be instantiated directly. The statement new Shape() will cause compilation error. A subclass of Shape must implement getArea() before an object can be created.


## Question 5. What is the difference between an abstract class and an interface? Give one scenario where you would prefer using an interface over an abstract class.

Abstract class can contain instance fields, constructors, and abstract methods; while an interface is mainly used to define a contract that implementing classes needs to follow. A class can extend only one class, but it can implement multiple interfaces.

One senario I prefer using an interface over an abstract class is when different types of classes need to share the same capability. For example, 'Bird' and 'Airplane' can implement a 'Flyable interface' even though they do not share the same class hierarchy.


## Question 6. What will be the output of the following code? Explain the concept of upcasting and downcasting.
Output:
"Animal eating"
"Meow!"
In line 1, Animal a = new Cat() is upcasting: a Cat object is referenced by an Animal reference. Since the reference type is Animal, a can directly access methods defined in Animal, such as eat(), but not the Cat-specific method meow().
In line 4, Cat c = (Cat) a is downcasting, and it converts the Animal reference back to a Cat reference. The instanceof makes sure that a refers to a Cat before downcasting.


## Question 7. What are the rules for overriding the equals() method? Why must we also override hashCode() when we override equals()?
The rules of equals() method are reflexive, symmetric, transitive, consistent, and null handling. When we override equals(), we should also override hashCode() because objects that are equal according to equals() must have same hash code.


## Question 8. What is the difference between shallow copy and deep copy? Given an object Person with a field Address address, explain what happens to the address field in each type of copy.
Shallow copy: creating a new object but copies the references of nested objects. Thus, the original Person and the copied Person share the same Address object, and changes to the address through one person will affect the other.
Deep copy: creating a new object and also creating copies of its nested objects. Therefore, each Person has its own Address object, and changing one person's address will not affect the other.


## Question 9. What will be the output of the following code? Explain why.
Output: 
"Taking off from Flyable"

Both Flyable and Swimmable provide a default takeOff() method and it makes conflict when Duck implements both interfaces.
Flyable.super.takeOff() calls the default implementation from the Flyable interface, so the output is "Taking off from Flyable".


## Question 10. Consider the following code. Which methods are valid overloads of calculate(int a, int b)? Which are NOT valid and why?
Valid:
Method A because it has a different number of parameters.
Method B because it has different parameter types.

NOT valid:
Method C because changing only return type does not change the method signature.
Method D because changing the access modifier and parameter names does not change the method signature.

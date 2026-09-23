Question 1.What is the difference between method overloading and method overriding? In which type of polymorphism does each belong?
Overloading happens at compile-time while overriding happens at runtime.Overloading happens within the same class while overriding happens at parent-child classes.For parameters overloading must be different while overriding must be the same. Return type for overloading can be different while for overriding it must be the same. For access modifier overloading can be different while for overriding it can’t be more restrictive. Private/final methods can be overloaded but can’t be overridden. In performance perspective overloading is better at early binding while overriding is slightly slower at late binding. The purpose of overloading is convenience and readability while the purpose of overriding is specific implementation. 
Method overriding belongs to runtime polymorphism while method overloading belongs to compile-time polymorphism. 

Question 2. What will be the output of the following code? Explain why.
The output would be Bark! 
This happens because Animal a = new Dog(); creates a Dog object but stores it in an Animal reference. Since Dog overrides the makeSound() method, Java uses runtime polymorphism to determine which method to execute. At runtime, Java sees that the actual object is a Dog, so it calls the Dog version of makeSound() and prints "Bark!".

Question 3.Why does Java NOT support multiple inheritance with classes? What is the "Diamond Problem"?How does Java solve this issue?
Java does not support multiple inheritance with classes mainly to avoid ambiguity and complexity, especially the Diamond Problem. The Diamond Problem is like “ If A and B both have a method with the same signature, which one should C inherit?” Java supports multiple inheritance through interfaces.

Question 4. What will happen when you try to compile and run the following code? Explain your answer.
The code will not compile. This is because Shape is an abstract class, and abstract classes cannot be instantiated directly. Also, getArea() is an abstract method, meaning it has no implementation in Shape. You need to create a concrete subclass that extends Shape and implements getArea(), then create an object of that subclass.

Question 5. What is the difference between an abstract class and an interface? Give one scenario where you would prefer using an interface over an abstract class.
Interface can have abstract/default/static/private methods while abstract allows any methods that are abstract and concrete. For fields interface only allows public/static/final constants while abstract allows anything including instance fields. Interface has no constructor while abstract has constructors. Interface allows multiple inheritance while abstract class can only extend once. For access modifiers the interface could have public methods or private methods after Java 9 while abstract classes could accept any access modifiers. The state interface can’t maintain it while the abstract class can. For use cases interfaces define capabilities/contracts while abstract classes share common implementation. For keywords interfaces use “implements” while abstract classes use “extends”. 
Interface is used when defining a contract/capability (what, not how). 

Question 6. What will be the output of the following code? Explain the concept of upcasting and Downcasting.
The output is “Animal eating” and “Meow!”. 
Upcastring means treating a child object as its parent type while downcasting means converting that parent reference back to the child type. Upcasting is always safe and automatic while downcasting may fail and require explicit cast therefore needs checking before actions. 

Question 7. What are the rules for overriding the equals() method? Why must we also override hashCode() when we override equals()?
For overriding equal() method we need to make it reflexive like “x.equals(x) must return true”, symmetric like “if x.equals(y) then y.equals(x)”,  transitive like “if x.equals(y) and y.equals(z) then x.equals(z)”, consistent like “multiple calls return same result (if objects don't change)” and Null handling like “x.equals(null) must return false”. 
The reason why we must also override hasCode() when we override equal is that we don’t want to break the hashmap. 

Question 8. What is the difference between shallow copy and deep copy? Given an object Person with a field Address address, explain what happens to the address field in each type of copy.
A shallow copy creates a separate outer object, but the inner objects are still shared. For example, both Person objects would point to the same Address. A deep copy creates separate copies of everything, so each Person has its own Address object.

Question 9. What will be the output of the following code? Explain why.
The output is “Taking off from Flyable.”If a class implements two interfaces with the same default method, it needs to override the method to avoid ambiguity. In this case, Duck chooses the Flyable version using Flyable.super.takeOff(), so it prints Taking off from Flyable.

Question 10. Consider the following code. Which methods are valid overloads of calculate(int a, int b)? Which are NOT valid and why?
Methods A and B are valid overloads because they use different parameters. Methods C and D are not valid because their parameter types are the same as the original method. Changing only the return type, access level, or parameter names does not create a valid overload.



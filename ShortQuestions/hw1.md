Question 1. What is the difference between method overloading and method overriding? In which type of
polymorphism does each belong?
overloading means defining mutiple methods with the same name, but has different parameters in the same class, the compiler chooses which to call based on the arguments, so it is static polymorphism.
overriding means child class modify the method inherited from parent class to perform some different or special behavior that not the same as the parent's method, so it is dynamic polymorphism.

Question 2. What will be the output of the following code? Explain why
Bark, becuase the type of the object is casted to dog, and dog override the makesound method.

Question 3. Why does Java NOT support multiple inheritance with classes? What is the "Diamond Problem"?
How does Java solve this issue?
The diamond problem means if a class can inherit more than one class, and those parent classes also inherited from the same class and override the same method, the child class will have a conflict on using those inhireted class. In order to solve this problem, in java, we only can inherit one class.

Question 4. What will happen when you try to compile and run the following code? Explain your answer.
It will execute fail, becuase a abstract class can not be initiated, it is only can be extended by other class. Also there is no implementation of the getarea method.

Question 5. What is the difference between an abstract class and an interface? Give one scenario where you
would prefer using an interface over an abstract class
Abstract class is like a blueprint, it can has both abstract method and regualr method.
and interface is more like a agreement between classes, a class can implment mutiple interfaces, but only can extend one class. So interface can have different capabilities from different sources, but interface does not have fields like abstrace. SO when we want to reuse something, we use absttract class, when we want to give the specific ability to a class, we use interface.

Question 6. What will be the output of the following code? Explain the concept of upcasting and
downcasting.
Animal eating
Meow!
line 1 upcast the object, so it can call eat, but can not call meow, becuase method meow is a method from cat, not animal class. Line 4 downcast the object back to cat, so it can sucessfully call meow. Upcasting means treat the object as an object of its superclass, downcasting means coverting a superclass object to the subclass so can use some specific methods.

Question 7. What are the rules for overriding the equals() method? Why must we also override hashCode()
when we override equals()?
The rules are
Reflexive: x.equals(x) is true.
Symmetric: if x.equals(y) is true, then y.equals(x) is true.
Transitive: if x.equals(y) and y.equals(z) are true, then x.equals(z) is true.
Consistent: repeated calls return the same result while the compared data stays unchanged.
Non-null: x.equals(null) is false.
We also need to override the hashcode method because If equal objects have different hash codes, those collections may fail to find an object or may keep what should be a duplicate.



Question 8. What is the difference between shallow copy and deep copy? Given an object Person with a field
Address address, explain what happens to the address field in each type of copy.
Shallow copy means copy the person object/s address, so they point to the same obejct, if anything person changes, they both get affect.
Deep copy means creating a separate object of the person obejct, they point to different address, so changing one does not affect another.




Question 9. What will be the output of the following code? Explain why
The output is Taking off from Flyable
The problem is that the class duck implements mutiple interfaces and they have some same methods, so it has to override it and define the method, otherwise the compiler will fail to run.

Question 10. Consider the following code. Which methods are valid overloads of calculate(int a, int
b)? Which are NOT valid and why?
Method A: Valid overload. It has three int parameters, while the original method has two.
Method B: Valid overload. Its parameters are double, double rather than int, int.
Method C: Invalid. It has the same parameter types as the original method. Java cannot distinguish methods based only on their return types.
Method D: Invalid. Changing parameter names does not change the parameter list. It also cannot differ from the existing public calculate(int, int) only by access modifier.
Conceptual Questions:
1.
The difference between method overriding and method overloading:

| Aspect                 | Overloading              | Overriding                     |
|------------------------|--------------------------|--------------------------------|
| Time                   | Compile time             | Runtime                        |
| Loaction               | class                    | parent-child class             |
| Method Name            | same                     | same                           |
| Parameters             | must be different        | must be the same               |
| Return type            | can be different         | must be the same               |
| Access modifier        | can be different         | cannot be more restrictive     |
| private / final method | can be overloaded        | cannot be overridden           |
| Performance            | early (early binding)    | slightly slower (late binding) |
| Purpose                | convenience, readability | specific implementation        |


Overloading belongs to compile-time polymorphism while overriding belongs to runtime polimorphism.


2.
Code output:
“Bark!”

Reason:
The reference variable is declared as class Animal, but it refers to the Dog object. Besides the subclass Dog also 
overrides the method makeSound() thus the reference variable a will only call the overridden method not the method 
in the parent class Animal, the output is just: “Bark!”



3.
Because when a subclass extends multiple classes, if there is the same method in different parent classes with 
different method bodies, the subclass can not choose which method it could execute.

The diamond problem is that a subclass extends multiple classes at the same time, and if the method has the 
same signature with different method bodies in the parent classes, the subclass cannot choose which method 
it could execute.

To solve the diamond problem, the subclass could implement multiple interfaces.



4.
The code snipper: Shape s = new Shape(); will not compile, there would be error.

Reason:
Abstract class cannot be instantiated.


5.
The difference between abstract class and interface:

| Feature              | Interface                          | Abstract class                  |
|----------------------|------------------------------------|---------------------------------|
| methods              | abstract, default, static, private | any (abstract+concrete)         |
| fields               | only public static final constant  | any (including instance fields) |
| constructors         | no                                 | yes                             |
| multiple inheritance | yes                                | no                              |
| access modifiers     | public                             | any                             |
| state                | cannot main state                  | can maintain state              |
| use case             | define capabilities/contracts      | share common implements         |
| keyword              | implements                         | extends                         |


scenario:
When unrelated classes need the same behavior and the subclass needs to implement multiple capabilities, in 
this case, we prefer to use an interface.


6.
Code output:
“Animal eating”
“Meow!”

Reason:
upcasting: Converting a child class reference to a parent class reference, always safe, automatically, implicit expression.
downcasting: Converting a parent class reference to a child class reference, need explicit expression and if the actual 
object is not an instance of the child class, it will throw a ClassCastException, better use validation before cast, 
keyword is instanceof before downcasting.


7.
Rules:
must use the same method signature;
reflexive: x.equals(x) must return true;
symmetric: if x.equals(y) then y.equals(x);
transitive: if x.equals(y) and y.equals(z) then x.equals(z);
consistent: multiple calls return same result (if objects don’t change);
null handling: x.equals(null) must return false.

We need to override the HashCode() as well because equal objects must have the same hash code. If we don’t override 
the hashCode() when override the equals(), the HashMap / HashSet will break.


8.
Difference between deep copy and shallow copy:
shallow copy: It creates a new outer object and copies the reference fields,  both original object and the copy one 
shared the same referenced objects.
deep copy: It creates a new outer object and copies the referenced object, original object and the copy one are independent.

In deep copy, the origin field address and copy field address has its own Address copy;
In shallow copy, they share the same Address.


9.
Code output:
"Taking off from Flyable"

Reason:
The class Duck implements two interfaces Flyable and Swimmable, and it must override the takeOff() to solve the
conflict issue. The overridden method calls explicitly Flyable.super.takeOff(), so only the method in Flyable 
interface will be executed.


10.
Valid: A, B,
Invalid:
C: only change the return type not the parameter’s type
D: change the name of parameters ,access modifier and the method body not the parameter’s type


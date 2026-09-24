

# Q1.

**Differences**:

The parameters and return type of overloading must be different while overriding must be same.  
Overloading can be used for private or final methods, but rewriting cannot.  
Overloading can have different access modifier while overriding can't.  
Overloading locates in same class while overriding locates in child classes.

Type of polymorphism
Overriding is runtime Polymorphism while overloading is compile time polymorphism.

# Q2.
The output will be  Bark!
Because Dog is a subclass of Animal, and it overrides the makesound function,  
when create we new Dog, the actual function called is makesound in Dog class.

# Q3.
because there is a Diamond Problem which is: if a class inherit from multiple classes and they have
same signatures, it's hard to know which one should the subclass inherit.

Java use interfaces to solve this multiple inheritance problem.

# Q4.
There will be an error.  
because an abstract cannot be instantiated directly.

# Q5.
Interface supports multiple implement while abstract supports only single inheritance.  
Interface doesn't have constructors while abstract class have.  
Interface can only have constants while abstract class can have any types.  
Interface supports public or private methods while abstract class supports any types.  
Interface cannot maintain state while abstract class can.

I prefer using an interface while I need multiple inheritance, for example when a class should have
ability to Compare,and Serialize at same time, I would implement these 2 interfaces.


# Q6.
Animal eating  
error  
Meow!  
Cat upcast to Animal class so `a` can only call the functions in Animal.  
`Cat c = (Cat) a`; To downcast `a` back to Cat, it must be manually turned forcefully

# Q7.
Two objects that are equal must have the same hashCode.  
If only `equals` is override, two objects with the same content will use the default hashCode of Object, based on memory address, which is basically different.

# Q8.
Shallow copy shares the address, all point to the same address.  
Deep copy both the object itself and the objects it references, with the new and old objects completely independent

For shallow copy when we change the `address` value other objects using the same address will also be changed.
For deep copy they are independent, they won't influence each other.

# Q9.
The output is `Taking off from Flyable`.
Flyable and Swimmable both have a default method with the same name. Duck implements both interfaces simultaneously, and the compiler does not know which one to inherit, so it must override.
So duck overrides `takeOff()` and explicitly specifies the version.

# Q10.
Method C and D are invalid, because overload must has different parameter list including different parameter number, type.

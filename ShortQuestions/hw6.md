1. Difference between factory method and abstract factory:
Factory method creates a single product and usually for one interface, it only has one method which often be overridden 
by subclasses. For example, an interface paymentProcessor can define a method called create(),a payPayProcessor class 
could override the method and return a payPalProcessor, and a debitCardProcessor could also override the create() method
and return a debitCardProcessor.
While abstract factory creates a group of products and usually for one interface, it has multiple 
factory methods. For example, UIFactory can have createButton() and createCheckBoxes(). A windowsUIFactory can create a
matching WindowsButton and WindowsCheckBoxes() and a macUIFactory can also create a matching MacButton() and 
MacCheckBoxes().
2. Because if there are many parameters in a constructor, it would be difficult to read, and hard to figure out which 
parameter is and easy to swap by mistake.
To solve the problem, we use Builder, the main strategy here is to construct complex objects step by step, with 
readable, named steps. 
For example, we create a class pizza, it has 5 fields so there should be 5 parameters in constructor. To make the messy
parameters readable, we use a class Builder, first, we create a temporary object with the required parameter like the 
size=12, and then we set the other not required parameters by their default values, 
like:
.cheese(true)
.pepperoni(true), and return Builder itself, which is return this, this called method chaining. In this way, the Builder
inside will save these setups and the last step is to call build() to create the final object which is immutable and all
fields are in the correct order.


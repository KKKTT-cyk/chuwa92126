1. In the method using instanceof, to get the current area of certain type, we need to first check the type of the shape
using instanceof, then write the related shape area computation logic. So in this method, if we want to add one more 
shape to calculate its area, we need to add another else if block, which is not easy to modification. In this case, this 
method is not closed for modification. Thus, this violates the OCP.
In the interface-based refactor, we have an interface with an abstract method getArea() and other different shapes class
could implement the interface and override the getArea() method and write their own logic. If we want to add another 
shape, we could not modify other shape's computation logic and just add one more class, which means this is open for 
extension and closed for modification. Thus, this follows the OCP.

2. Factory method could only create one single product and usually for one interface, it only has one abstract method 
and should be overridden by its subclasses. For example, an interface named paymentProcessor has one method called
create(), a paypalProcessor class could have override create() method and create a paypalProcessor, and a debitProcessor
class could also override the create() method and creates a debitProcessor.
While for abstract factory could create a group of related products and usually for one interface, it could have 
multiple factory methods. For example, an interface named UIFactory which has two methods createButton() and 
checkedBoxes(). A WindowsUIFactory could create a matching WindowsButton() and WindowsCheckedBoxes() and a MacUIFactory
could also create a matching MacButton() and MacCheckedBoxes().

3. Constructor with many parameters is hard to use because in these parameters, only one parameter is required and the
rest are optional, and they may have the same type, which is easy to get confused. The builder pattern will solve the
issue by construct the objects step by step, with readable and named steps. For example, we have a class named Pizza,
with at least 5 fields. With the help of builder pattern, first use Pizza.Builder(2), here 2 is the required parameter,
here we assume it is size, and then we call the other optional parameters like .cheese() to set their default value, and
every method return this, thus these methods could be called consecutively. Builder will save this setup and the last
step is using build() to create the Pizza object which is complete and immutable in one construction step.

4. In the strategy pattern, the Context class holds the reference for a Strategy and delegate the task for the 
concrete strategy instead of implementing the algorithm. The Strategy interface defines a common method for the concrete
strategies to follow. And the concrete strategy will implement the Strategy interface and override the common method 
with their own algorithms. Because the Context class depends on the Strategy interface rather than a specific 
implementation, the active strategy can be replaced at runtime. For example, a payment system could switch the payment
between debit and PayPal strategies based on the users' choices. This makes the code more flexible, reduce the if-else 
statement and allows the new strategies to be added without changing the Context.

5. One-to-many relationships with the subject and observers means one subject could save many observers, when the 
subject changes, all its registered observers will be notified automatically by iterating all its registered observers 
by iterating them and calls update() method.
The Observer interface defines a common update() method for all observers to implement with specific logic when they get
notification. When there is new observer need to be added, we only need to implement the Observer interface and register
it into Subject. Thus, the subject only depends on Observer interface without knowing the specific type of the Observer,
so there is no need to change the subject code.

6. Decorator pattern solves the problem which is we want to add responsibilities to objects dynamically without 
modifying the class or creating an explosion of subclasses for every combination of features. For example, if we only 
use inheritance to solve this problem like we have a parent class Coffee, we want to add different ingredients to the
coffee, there should be to many subclasses like CoffeeWithMilk and CoffeeWithSugar, which would lead to explosion. 
Decorators wrap the original object and the added behavior while keeping the same interface.
Real-world example: BufferedInputStream, which wrap a FileInputStream and adds buffering while both are used the same 
InputStream type.

7. The core difference between Decorator and Proxy is intent, Decorator pattern is to add behaviors or responsibilities
to the objects while Proxy is intended to control access to objects.

8. The difference between the Adapter, Proxy and Decorator based on 
a) interface relationship: Adapter wraps a class with a different interface, converts it into a compatible one, Proxy 
and Decorator wraps a class with the same interface and converts it into a compatible one.
b) primary intent: Adapter is intended to make incompatible things work together, Proxy is intended to control access 
the objects and Decorator is intended to add behaviors or responsibilities to the objects.

9. Naive lazily-initialized Singleton is not thread safe because multiple thread may call getInstance() at the same time.
They may both observe instance is null and create separate Singleton objects.
The Double-Checked Locking would first check if the instance already exists without acquiring the lock. If it is null, 
the thread enters the synchronized block and checks again before creating the instance. The second check prevents another
waiting thread from creating the second instance. The volatile keywords guarantees visibility across threads and prevents
instructions reordering, so no thread could observe partially initialized Singleton.

10. 
a) Singleton Pattern
b) Strategy Pattern
c) Virtual Proxy Pattern

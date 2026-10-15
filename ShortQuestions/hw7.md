1. instanceof version: one calculator checks Circle, Rectangle, etc. with if/else. Adding a new shape requires editing that calculator.
Refactor: each Shape implements area(). The calculator only calls shape.area(). New shapes are added without modifying existing code, so it follows OCP.
2. Factory Method creates one product object (for example, one Notification). Abstract Factory creates a family of related objects (for example, matching Windows buttons, menus, and checkboxes).
3. Telescoping constructors are easy to misuse because many parameters of the same type/order are confusing. Builder uses named, chainable methods for optional fields, then build() creates the final object.
4. The Context uses a strategy. The Strategy interface defines the algorithm method. Concrete strategies provide different algorithms. Runtime swapping lets the Context change behavior without changing its own code.
5. One Subject can have many Observers. Observers subscribe and are notified when the Subject changes. A new observer only implements Observer; the Subject does not need modification.
6. Decorator avoids “subclass explosion,” where every feature combination needs another subclass. It wraps an object to add behavior dynamically. Java example: BufferedInputStream decorating an InputStream.
7. Decorator’s intent is to add behavior/responsibilities. Proxy’s intent is to control access to an object, such as lazy loading, security, or remote access.
8. Pattern	Wrapped interface	Primary intent
   Adapter	Different interface	Make incompatible classes work together
   Decorator	Same interface	Add behavior dynamically
   Proxy	Same interface	Control access to the real object
9. A naive lazy Singleton can create two instances when two threads both see instance == null. Double-Checked Locking checks twice and synchronizes only during creation. volatile prevents one thread from seeing a partially constructed object.
10. (a) Singleton  
    (b) Strategy  
    (c) Proxy, specifically a virtual proxy
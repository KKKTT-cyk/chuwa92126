### Question 1.

Factory Method creates a single product. This method centralizes the creation of one kind of product, hiding the concrete class choice behind a single method.

Abstract Factory creates a family of related products. Abstract Factory is "a factory of factories" that groups multiple related Factory Methods into a single interface, so that one call produces serveral objects that are guaranteed to match each other. Abstract Factory is essentially a wrapper around several Factory Methods that are meant to be called as a unit.

### Question 2.

Telescoping constructors fail because positional arguments provide no protection against swapping same-typed parameters, and they force an awkward choice between overload explosion or always passing every argument. Builder fixes this by turning construction into named, chainable, order-independent method calls, with required fields enforced through the builder's own constructor and the final object kept immutable.

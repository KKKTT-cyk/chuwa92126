# HW6 - Java Design Patterns

## 1. Factory Method vs. Abstract Factory

Factory Method creates a single product type through a common interface. Subclasses decide which concrete product to create. For example, `CarFactory` creates a `Car`, while `BikeFactory` creates a `Bike`; both products implement `Transport`.

Abstract Factory creates a family of related or matching products. For example, a GUI factory creates both buttons and checkboxes. A Windows factory creates Windows-style versions of both, while a Mac factory creates Mac-style versions.

The main difference is that Factory Method focuses on one product type, while Abstract Factory focuses on a family of related product types.

## 2. Telescoping Constructors and Builder

Telescoping constructors are overloaded constructors with increasing numbers of parameters. When there are many parameters, callers must remember their order and meaning. Parameters of the same type are easy to swap, and optional fields can require confusing placeholder values. More options also lead to more constructor overloads.

The Builder pattern solves this by providing named methods to configure an object step by step, followed by `build()` to create it. For example, `.helmetRequired(true).paymentMethod("CARD").build()` clearly shows what each value means. Required fields can be provided when creating the builder, optional fields can have defaults, and `build()` can validate the configuration. This makes complex objects easier to construct and the calling code easier to read.

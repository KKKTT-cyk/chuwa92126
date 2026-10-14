## Q1. Factory Method vs. Abstract Factory

Factory Method creates one type of product and lets subclasses decide which concrete object to create. For example, EmailNotificationCreator creates email notifications, while SmsNotificationCreator creates SMS notifications.

Abstract Factory creates a family of matching products. For example, WindowsUIFactory creates Windows-style buttons and checkboxes, while MacUIFactory creates Mac-style buttons and checkboxes.


## Q2. Telescoping Constructors and the Builder Pattern

Constructors with many parameters are hard to read because we must remember what each parameter means and its position. It is easy to mix up parameters of the same type, and optional settings may require many overloaded constructors.

The Builder pattern lets us create an object step by step using named methods. This makes the code easier to read and reduces mistakes. Optional settings can be omitted, and build() creates the final object.
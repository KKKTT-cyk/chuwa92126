## Java Design Patterns Homework

### Dengtai Wang

#### Conceptual Questions

1. **Explain the difference between Factory Method and Abstract Factory, in terms of what each one creates**

   **(a single product vs. a family of matching products).**

   Answer:

   ​	Factory method creates a single product through a common interface, with concrete implementations deciding which product to return. For example, CreditcardPayment and ApplePayPayment will both implement PaymentProcessor Interfece. When we need to use payment method, PaymentFactory will return the corresponding Payment class to process the payment.

   ​	Abstract Factory creates a family of related, matching products through multiple product interfaces. For example, if we need both pay and refund function for each payment method, we can use Abstract Facotry Method, an CreditcardFactory creates both payment and refund for credit card, so the components belong to the same provider.

   ​	Single product means one product type, a family of matching products is a group of different product types designed to work together or follow the same provider.

2. **Explain why constructors with many parameters ("telescoping constructors") are hard to use correctly,**

   **and describe how the Builder pattern solves this problem.**

   Answer:

   ​	For Telescoping constructors, they have too many parameters and callers must remember parameter orderes epecially when several parameters share the same type. Like some consequential boolean type:	

   ```java
   new User("A", true, true, false, true)
     // Can be explained as A user Name is A, and has verified email, enabled email notification, disabled text notification, enabled mail notification.
   ```

   ​	Builder pattern replaces a long argument list with named steps

   ```java
   User user = new User.Builder("A") 
     	.verifiedEmail(true)
       .emailNotifications(true)
       .smsNotifications(false)
     	.mailNotifications(true)
       .build();
   ```

   This makes each value’s purpose clear, lets callers omit optional settings and use defaults, and allows build() to validate the configuration before creating the object. The tradeoff is additional builder code, so it is most useful for objects with many optional parameters.
1. Problem: when the OrderService class use new to create dependencies, it becomes tightly coupled with the specific
implementation, for example, it directly created a CreditCardPaymentService, changing to another implementation requires
modifying OrderService. Its dependencies are also hidden, and it is difficult to replace them with mock during
test. Besides, the class becomes responsible for both business logic and dependency creation.
IOC moves the responsibility of creating and managing objects from OrderService to an external container. And dependency
injection will provide the required dependencies through its constructor. As a result, OrderService only focuses on 
business logic and use different implementations or mock dependencies without changing the code.

2. The annotation @SpringBootApplication contains three different annotations:
@SpringBootConfiguration: with this annotation, this class is a configuration class
@EnableAutoConfiguration: with this annotation, it will turn on autoconfiguration
@ComponentScan: with this annotation, it will scan THIS package and all sub-packages for @Component, @Service, @Controller
A @Service class in a sibling package could not be found at startup because the default component could only scan the 
main class's packages and its subpackages. It cannot scan the sibling packages. Therefore, the service is not registered
as a Spring Bean. This could be fixed by moving the main class to a common parent package.

3. The constructor injection provides the dependencies through the class constructor. It makes the required dependency
explicitly, ensure the object is fully initialized, supports final fields and makes unit testing easier. The setter 
injection provides the dependencies through the setter method and is suitable for optional or changeable dependencies,
but it may leave the object in an incomplete state. The field injection provides the dependencies through fields. It is
concise but will hide the dependencies, prevents the fields from being final and makes the unit testing more difficult.
Therefore, the constructor injection is recommended, because it creates clear, immutable, fully initialized and easily
testing objects.

4. Spring performs dependency injection by type. If the application context contains two beans that implement 
NotificationSender, Spring cannot determine which one to inject, the dependency is ambiguous and the application always
fails to start. 
To solve the problem, we could mark one implementation with @Primary, choosing a specific bean as @Qualify, or injecting 
all implementations as a List or a Map and choose appropriate one when needed.

5. Spring @Service beans are singleton by default, in the example ReportService class, one ReportService instance is 
shared by all request threads. If the service stores a mutable list as an instance filed, every request modifies the
same list. Concurrent calls to clear(), add() and join() can interface each other, will cause lost or mixed report data.
Declared the field with final will not solve the problem because the list is still mutable. The list should be created 
as a local variable inside the buildReport(). So each method call could have its own list, preventing interface with 
each other.
Spring's Singleton scope provides one bean instance with per bean definition within each spring container, the container
managed the instances. The class is still normal Java class, so other unmanaged objects could still be created with new,
and its dependencies could still be injected or mocked. In contrast, the GoF Singleton pattern provides one bean instance
per class loader, and it's enforced by class through the private constructor, a static instance and a global assess method.
So it is hard to test.

6. Order and explanation: 
1) constructor: Spring creates the bean instance by calling the constructor. If the constructor injection is used, the
required dependencies are passed into the constructor.
2) setter/field injection: After creating the object, Spring injects the rest dependencies through the setter method or
directly into annotated fields.
3) @PostConstruct: After the dependencies injection is completed, Spring calls the method with annotated @PostConstruct.
This method is used for initialization logic, validation or setup that required all dependencies to be available.
4) BeanPostProcessor.postProcessAfterInitialization(): After initialization is complete, the Spring BeanPostProcessor 
can wrap the bean. Spring AOP proxies are created at this step, so the final object stored and exposed by the container
maybe a proxy.
5) @PreDestroy: When the application context is shutting down, spring calls the method with annotated @PreDestroy, so 
the bean will release resource or perform cleanup.
AOP proxies are created at the step: BeanPostProcessor.postProcessAfterInitialization().

7. Spring boot find the candidate configuration classes from META-INF/spring/org.springframework.boot.autoconfigure.
AutoConfiguration.imports files on the classpath, then evaluate the conditions on those classes and bean methods.
@ConditionalOnClass applies the configurations only when a given class is on the classpath, while @ConditionalOnMissingBean
applies the configurations only when you have not defined the matching bean in the application context.
Therefore, if the application defines its own ObjectWrapper bean, the missing bean condition no longer matches, so Spring
Boots backs off and do not create the default ObjectWrapper.

8. @Value injects configuration values one field at a time, and it is suitable for a few simple properties. It does not 
naturally group related settings, has limited relaxed binding, and provide no configuration validation or IDE 
auto-completion, but it supports SpEL expression. In contrast, @ConfigurationProperties binds a group related settings 
to a structured and type-safe object. It supports full relaxed binding, validation with @Validated, and IDE 
auto-completion with spring-boot-configuration-processor, but it does not support SpEL.
Highest to lowest precedence:
1) command-line arguments
2) Java system properties
3) OS environment variables
4) profile-specific files
5) application.yml
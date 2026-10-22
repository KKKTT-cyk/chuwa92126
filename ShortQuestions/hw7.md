# Spring Boot Homework

## Q1
Using the OrderService example from class, explain the problems caused when a class creates its own dependencies with new, and how Inversion of Control and dependency injection solve them.

Answer:
Using `new` inside `OrderService` ties it to specific tools, such as MySQL and PayPal. This makes them harder to change or replace with fake tools for testing. Configuration is scattered, and object creation and cleanup must be managed manually.

With IoC, Spring takes responsibility for creating and managing these objects. With DI, Spring passes them into `OrderService`, usually through its constructor. This makes the code easier to change, test, and maintain.

## Q2
Name the three annotations that @SpringBootApplication combines and what each one does. Explain why a @Service class placed in a sibling package of the main class is not found at startup.

Answer:
@SpringBootApplication combines three annotations:
1. @SpringBootConfiguration marks the class as a configuration class.
2. @EnableAutoConfiguration enables automatic configuration based on the project's dependencies.
3. @ComponentScan searches for Spring components.
By default, component scanning covers the main class's package and its subpackages. A @Service in a sibling package is outside this area, so it is not found. Move it into the scanning area or explicitly configure Spring to scan its package.


## Q3
Compare constructor injection, setter injection and field injection, and explain why constructor injection is recommended.

Answer:
Constructor injection passes dependencies through the constructor. Setter injection passes them through setter methods after the object is created. Field injection uses @Autowired directly on fields.

Constructor injection is recommended because it makes required dependencies clear, supports final fields, and makes testing easier. Setter injection is useful for optional dependencies, while field injection hides dependencies and makes testing harder.

## Q4
An application context contains two beans that implement NotificationSender. What happens when another bean asks for a NotificationSender, and what are three ways to resolve it?

Answer:
When two beans implement NotificationSender, injecting a single NotificationSender without a selection rule causes a NoUniqueBeanDefinitionException.
Three ways to resolve this are:
1. Use @Primary to mark one bean as the default.
2. Use @Qualifier to specify which bean to inject.
3. Inject a List<NotificationSender> to receive both beans.

## Q5
Explain why singleton beans must be stateless, using the ReportService example with a shared List field, and explain how Spring's singleton scope differs from the GoF Singleton pattern.

Answer:
A singleton bean is shared by multiple users. If ReportService stores report data in a shared list, different users’ data may get mixed together. Creating a new list inside the method keeps each call’s data separate.
Spring creates one instance per bean definition in each Spring container. The GoF Singleton pattern uses the class’s own code, usually a private constructor, to prevent other code from freely creating new instances.


## Q6
Put these lifecycle steps of a bean in order and explain what happens in each: @PreDestroy, constructor, BeanPostProcessor.postProcessAfterInitialization(), @PostConstruct, setter/field injection. In which step are AOP proxies created?

Answer:
The order is:
1. Constructor: creates the bean.
2. Setter/field injection: supplies its dependencies.
3. @PostConstruct: performs initialization after injection.
4. BeanPostProcessor.postProcessAfterInitialization(): processes the initialized bean and typically creates AOP proxies.
5. @PreDestroy: performs cleanup before the bean is destroyed.
The bean is used after initialization and before destruction.


## Q7
Explain how auto-configuration works: where Spring Boot finds the candidate configuration classes, and how @ConditionalOnClass and @ConditionalOnMissingBean decide whether a configuration is applied. Why does defining your own ObjectMapper bean make Spring Boot's one back off?

Answer:
Spring Boot finds auto-configuration candidates in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports.
@ConditionalOnClass checks whether a required class is available. @ConditionalOnMissingBean checks whether a matching bean is missing from the Spring container.
If we define our own ObjectMapper bean, the missing-bean condition is no longer met, so Spring Boot does not create its default one.



## Q8
Compare @Value and @ConfigurationProperties, and list these property sources from highest to lowest precedence: application.yml, OS environment variables, command-line arguments, profile-specific files, Java system properties.

Answer:
@Value reads a single setting, such as a shop name. @ConfigurationProperties reads a group of related settings, such as a shop’s name and address, into one Java object.
If the same setting has different values in multiple places, the higher-priority source wins. From highest to lowest priority:
1. Command-line arguments
2. Java system properties
3. OS environment variables
4. Profile-specific files
5. application.yml


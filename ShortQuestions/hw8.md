# java-spring-boot-homework

1. Using the OrderService example from class, explain the problems caused when a class creates its own dependencies with `new`, and how Inversion of Control and dependency injection solve them.

When OrderService creates its own dependencies using `new`, it becomes tightly coupled to specific implementations, which makes code harder to test and maintain because changing a dependency requires modifying the class itself. It also makes configuration more difficult.

IoC allows Spring container manage object creation and dependencies. DI allows Spring to provide the required objects through constructors, setters, or fields. This makes the code more flexible, easier to test, and easier to maintain.


2. Name the three annotations that @SpringBootApplication combines and what each one does. Explain why a @Service class placed in a sibling package of the main class is not found at startup.

@SpringBootApplication combines the annotations:
- @SpringBootConfiguration: marks the class as a Spring configuration class
- @EnableAutoConfiguration: enables SpringBoot auto-configuration based on available dependencies
- @ComponentScan: scans current package and its subpackages for components

If a @Service class is placed in a sibling package, it is not found because that package is outside the scanning scope. We can move the main application class to a common root package or configure @ComponentScan to fix it.


3. Compare constructor injection, setter injection and field injection, and explain why constructor injection is recommended.

Constructor injection provides dependencies through class constructor, making required dependencies clear, supports `final` fields and easy to test without Spring. It is recommended because it makes dependencies explicit, improves testability and maintainability.

Setter injection provides dependencies through setter methods and is useful for optional dependencies. The object can exist before all dependencies are set.

Field injection uses `@Autowired` directly on fields; it hides dependencies and makes unit testing more difficult.


4. An application context contains two beans that implement NotificationSender. What happens when another bean asks for a NotificationSender, and what are three ways to resolve it?

Spring may fail to inject single bean because it cannot determine which implementation to use. 
Three ways to solve this problem:
- @Primary: marks one bean as the default implementation.
- @Qualifier: specifies which bean should be injected by name.
- Inject all of them: injects all implementations so the application can choose one when needed.


5. Explain why singleton beans must be stateless, using the ReportService example with a shared List field, and explain how Spring's singleton scope differs from the GoF Singleton pattern.
   
Spring singleton beans are shared by multiple threads, and if singleton contains mutable shared data, concurrent requests may modify the same data and cause race conditions. If ReportService stores a shared List as an instance variable, two requests may update the list at the same time, causing incorrect results. A simple solution is to create the list as a local variable, so each request uses its own list.

Spring Singleton manages one bean instance per container and bean definition, while the GoF Singleton pattern controls instance creation within the class itself, usually through a private constructor and a static instance.


6. Put these lifecycle steps of a bean in order and explain what happens in each: @PreDestroy, constructor, BeanPostProcessor.postProcessAfterInitialization(), @PostConstruct, setter/field injection. In which step are AOP proxies created?

    1. constructor: Spring calls its constructor and constructor injection happens here
    2. setter/field injection: Spring injects dependencies through setters or fields
    3. @PostConstruct: After dependency injection is completed, this phase initializes the bean, such as preparing resources, loading initial data, or setting up a cache
    4. BeanPostProcessor.postProcessAfterInitialization(): AOP proxies are created here
    5. @PreDestroy: When application context closes, Spring calls `@PreDestroy` to clean up resources

AOP proxies are created during post-initialization phase. They wrap the original bean to provide additional functionality without modifying the bean's main business logic.


7. Explain how auto-configuration works: where Spring Boot finds the candidate configuration classes, and how `@ConditionalOnClass` and `@ConditionalOnMissingBean` decide whether a configuration is applied. Why does defining your own `ObjectMapper` bean make Spring Boot's one back off?

Spring Boot uses `@EnableAutoConfiguration`, which discovers auto-configuration classes through the `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` files in the jar on the classpath. Then it evaluates conditional annotations to decide which configurations should be applied. `@ConditionalOnClass` checks whether a specific class is available on the classpath, and `@ConditionalOnMissingBean` checks whether a matching bean has been defined.

If we define our own `ObjectMapper` bean, the `@ConditionalOnMissingBean` is no longer satisfied. Spring Boot then backs off and does not create its default `ObjectMapper` bean.


8. Compare `@Value` and `@ConfigurationProperties`, and list these property sources from highest to lowest precedence: `application.yml`, OS environment variables, command-line arguments, profile-specific files, Java system properties.
   
`@Value` is used to inject individual configuration values into a field or parameter, and is useful when only a few properties are needed.

`@ConfigurationProperties` is better for managing a group of related settings because it binds them to a structured object. It also supports type-safe binding and validation, making the configuration easier to maintain.

The property sources from highest to lowest precedence:
   1. command-line arguments
   2. Java system properties
   3. OS environment variables
   4. profile-specific files
   5. `application.yml`

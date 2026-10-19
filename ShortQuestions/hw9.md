## Question1:
In the original OrderService example, the class directly creates its dependencies using the new keyword:
   This approach causes several problems:
1. Tight coupling: OrderService depends directly on concrete implementations instead of relying only on interfaces. Replacing MySqlOrderRepository with another database implementation requires modifying the OrderService code.
2. Difficult unit testing: Because dependencies are created internally, replacing them with mock implementations for testing is difficult.
3. Poor flexibility and maintainability: Changing payment providers, email services, or database implementations requires changes to the service itself.
4. Mixed responsibilities: OrderService is responsible for both managing dependencies and processing business operations, violating the Single Responsibility Principle.

Inversion of Control (IoC) solves this problem by transferring responsibility for object creation and dependency management from the application class to an external container, such as the Spring IoC container.
Dependency Injection (DI) is a technique for implementing IoC. Instead of creating dependencies internally, the class receives them from outside, typically through constructor injection.

## Question2:
2.1: The @SpringBootApplication annotation combines three important Spring annotations:
@SpringBootConfiguration: Identifies the main class as a source of Spring configuration and allows additional bean definitions. It is a specialized form of @Configuration.
@EnableAutoConfiguration	Enables Spring Boot to automatically configure application components based on available dependencies, existing beans, and configuration properties.
@ComponentScan	Automatically discovers Spring-managed components, including classes annotated with @Component, @Service, @Repository, and @Controller.

2.2: The @ComponentScan annotation scans the package containing the main application class and its subpackages by default. It does not automatically scan sibling packages.

## Question3:
Feature	Constructor	Setter	Field
Injection timing	During construction	After construction	After construction
Required dependencies	Best suited	Possible, but less ideal	Possible, but less ideal
Supports final fields	Yes	No	No for normal injection
Unit testing	Easy	Relatively easy	More difficult
Dependency visibility	Explicit	Explicit through setters	Less explicit
Recommended use	Required dependencies	Optional dependencies	Generally discouraged

Why constructor injection is recommended:
Constructor injection ensures required dependencies are available when the object is created, supports immutability, makes dependencies explicit, and simplifies unit testing. It also helps identify design problems when a class requires too many dependencies.

## Question4:
If another class requests a NotificationSender without specifying which implementation to use, Spring cannot automatically determine the intended bean unless another resolution rule applies.
This typically results in a NoUniqueBeanDefinitionException during dependency resolution.

@Primary provides a default implementation, @Qualifier selects a specific implementation, and collection injection allows access to multiple implementations. These techniques make dependency injection more flexible and explicit.

## Question5:
In Spring, the default bean scope is singleton, meaning Spring creates one shared instance of a bean for each bean definition within an application context.
Because Spring applications often handle multiple requests concurrently, the same singleton bean may be accessed by multiple threads simultaneously. Therefore, singleton service beans should generally be stateless or avoid unsynchronized mutable shared state.

Spring Singleton vs. GoF Singleton:
Feature	|Spring Singleton Scope	|GoF Singleton Pattern
Instance management	|Managed by the Spring IoC container	|Managed by the class itself
Number of instances	|One per bean definition per application context	|Typically one per classloader under ordinary class loading
Instance creation	|Spring container	|Private constructor and static access mechanism
Global access	|Retrieved through Spring's container or dependency injection	|Usually through a static method such as getInstance()
Flexibility	|Different application contexts may contain separate instances	|Designed to restrict application-wide instantiation

## Question6:
A Spring bean goes through several lifecycle stages, from object creation to destruction.
The correct order is:
1 Constructor
Spring creates the bean instance.

2 Setter / Field Injection
Spring injects dependencies into the created bean.

3 @PostConstruct
Initialization logic runs after dependency injection.

4 postProcessAfterInitialization()
Post-processors can modify or wrap the initialized bean, including creating AOP proxies.

5 @PreDestroy
Cleanup logic runs before the bean is destroyed.

## Question7:
1. Locating auto-configuration classes
   In modern Spring Boot versions, auto-configuration candidates are discovered from the following file inside Spring Boot or third-party JAR files:
   META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
This file contains the fully qualified names of auto-configuration classes.
During application startup, Spring Boot reads these entries and evaluates their conditions to determine which configurations should be applied.
2. The @ConditionalOnClass annotation enables a configuration only when a specified class is present on the application's classpath.
3. The @ConditionalOnMissingBean annotation allows a bean to be created only when no matching bean has already been registered.
4. if the application explicitly defines its own ObjectMapper, the @ConditionalOnMissingBean condition no longer matches.
   Because an ObjectMapper bean is already available, Spring Boot skips creating its default ObjectMapper.
   This behavior is called auto-configuration backing off.
   It prevents unnecessary duplicate default beans and gives developers greater control over application configuration.

## Question8:
1.The @Value annotation is primarily used to inject individual configuration properties directly into fields, constructors, or method parameters.
The @ConfigurationProperties annotation binds groups of related configuration properties to a Java object.

Priority	Property source	Example
1 — Highest	Command-line arguments	--server.port=9090
2	Java system properties	-Dserver.port=8081
3	OS environment variables	SERVER_PORT=8082
4	Profile-specific configuration files	application-prod.yml
5 — Lowest	General application configuration	application.yml
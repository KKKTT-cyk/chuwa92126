## 1. Using the OrderService example from class, explain the problems caused when a class creates its own dependencies with new, and how Inversion of Control and dependency injection solve them.
The problems are poor flexibility, which means changing the repository will modify the orderservice, and it is hard to test. If we use IoC, object creation is transferred from OrderService to the Spring container. Now OrderService only focuses on order-related business logic. Spring creates the OrderRepository object and injects it into OrderService. This reduces coupling, makes testing easier, supports multiple implementations, and allows dependencies to be configured centrally.



## 2. Name the three annotations that @SpringBootApplication combines and what each one does. Explain why a @Service class placed in a sibling package of the main class is not found at startup.
1. @Configuration indicates that the class can define Spring configuration and @Bean methods.
2. @EnableAutoConfiguration tells Spring Boot to automatically configure components based on the dependencies found on the classpath, such as web servers, databases, and Spring MVC.
3. @ComponentScan tells Spring to scan for classes annotated with @Component, @Service, @Repository, @Controller, and related annotations.
A @Service class in a sibling package is not found because component scanning normally starts from the package containing the main application class and scans only its subpackages.



## 3. Compare constructor injection, setter injection and field injection, and explain why constructor injection is recommended.
Constructor injection, Spring provides the dependency through the constructor.
Setter injection, Spring provides the dependency by calling a setter method after creating the object. It is useful for optional dependencies.
Field injection, Spring directly assigns the dependency to the field.
Constructor injection is recommended because it make a clear dependencies relationship, it is easy to unit test without starting spring, and it It prevents creating an invalid object with missing required dependencies.



## 4. An application context contains two beans that implement NotificationSender. What happens when another bean asks for a NotificationSender, and what are three ways to resolve it?
If two beans implement the same interface, Spring cannot decide which one to inject when another bean requests the interface.
We can use @Primary to define a default implementation or use @Qualifier to select one explicitly. We also can inject all implementations by using a map or list.


## 5. Explain why singleton beans must be stateless, using the ReportService example with a shared List field, and explain how Spring's singleton scope differs from the GoF Singleton pattern. 
Becuase singleton bean is created once per application context, it should generally be stateless. If report is not stateless and singletion, the concurrent requests can modifty the list at the same time, which casing reace conditions.
Spring singleton: one instance per Spring ApplicationContext; Spring manages its creation and lifecycle.
GoF Singleton: the class itself enforces one instance, usually with a private constructor, a static instance, and a static accessor.


## 6. Put these lifecycle steps of a bean in order and explain what happens in each: @PreDestroy, constructor, BeanPostProcessor.postProcessAfterInitialization(), @PostConstruct, setter/field injection. In which step are AOP proxies created?
1. Constructor, call the constructor, create the bean objects
2. Setter / field injection
3. @PostConstruct after all injection done
4. BeanPostProcessor can do some modification to bean, usually AOP creates in this step
5. Bean is ready for use
6. @PreDestroy, in this step, bean clean up everything




## 7. Explain how auto-configuration works: where Spring Boot finds the candidate configuration classes, and how @ConditionalOnClass and @ConditionalOnMissingBean decide whether a configuration is applied. Why does defining your own ObjectMapper bean make Spring Boot's one back off?
Spring Boot auto-configuration first discovers candidate configuration classes from metadata files included in dependency JARs, such as AutoConfiguration.imports. It then evaluates conditions on those classes. 
@ConditionalOnClass applies a configuration only when a required class is available on the classpath, while @ConditionalOnMissingBean applies it only when the application context does not already contain a matching bean. 
For example, Jackson auto-configuration checks whether ObjectMapper is available and creates a default ObjectMapper only if the user has not defined one. If the application defines its own ObjectMapper bean, @ConditionalOnMissingBean no longer matches, so Spring Boot backs off and uses the user-provided configuration instead.






## 8. Compare @Value and @ConfigurationProperties, and list these property sources from highest to lowest precedence: application.yml, OS environment variables, command-line arguments, profile specific files, Java system properties.
It is simple, but it becomes difficult to manage when many related settings are involved. @ConfigurationProperties binds a group of related properties to a typed Java class, supports relaxed naming, validation, and is generally better for structured configuration.

precedence from highest to lowest is:
1. Command-line arguments
2. Java system properties
3. Operating-system environment variables
4. Profile-specific configuration files, such as application-prod.yml
5. The general application.yml file
If the same property appears in multiple sources, Spring Boot uses the value from the source with higher precedence. Thus, a command-line argument overrides a system property, environment variable, or YAML value.
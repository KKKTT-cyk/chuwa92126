## Spring Boot Homework

### Dengtai Wang

#### Conceptual Questions

1. **Using the OrderService example from class, explain the problems caused when a class creates its own**
   **dependencies with new, and how Inversion of Control and dependency injection solve them.**

   ​	When OrderService creates its own dependencies with new, it becomes tightly coupled to concrete classes such as MySqlOrderRepository, PayPalPaymentClient, and SmtpEmailSender. This makes the code harder to test, harder to change, and gives OrderService responsibility for both business logic and object creation. Inversion of Control moves dependency creation outside the class, and Dependency Injection passes those dependencies into the class, usually through the constructor. This reduces coupling, makes implementations easy to replace, and makes unit testing much easier.

2. **Name the three annotations that @SpringBootApplication combines and what each one does. Explain why a @Service class placed in a sibling package of the main class is not found at startup.**

   ​	@SpringBootApplication combines @Configuration, @EnableAutoConfiguration, and @ComponentScan. @Configuration provides bean definitions, @EnableAutoConfiguration lets Spring Boot configure the application automatically, and @ComponentScan finds Spring-managed components. 

   ​	By default, component scanning starts from the package of the main class and only includes its subpackages. Therefore, a @Service in a sibling package is not discovered unless the main class is moved to a common parent package or the scan packages are configured explicitly.

3. **Compare constructor injection, setter injection and field injection, and explain why constructor injection**
   **is recommended.**

   ​	Constructor injection passes dependencies through the constructor, setter injection uses setter methods, and field injection injects dependencies directly into fields. Constructor injection is recommended because it makes required dependencies explicit, allows fields to be final, improves immutability, makes unit testing easier, and ensures the object cannot be created in an incomplete state.

4. **An application context contains two beans that implement NotificationSender. What happens when another bean asks for a NotificationSender, and what are three ways to resolve it?**

   ​	If two beans implement NotificationSender and Spring is asked to inject only one NotificationSender, the injection is ambiguous and Spring throws a NoUniqueBeanDefinitionException. Three common solutions are to mark one bean with @Primary, use @Qualifier to choose a specific bean, or inject all implementations.

5. **Explain why singleton beans must be stateless, using the ReportService example with a shared List field, and explain how Spring's singleton scope differs from the GoF Singleton pattern.**

   ​	Spring singleton beans are shared by many callers and threads, so they should usually avoid mutable request-specific fields. Stateless services avoid shared data race condition by keeping temporary data in local variables or external storage. Spring singleton scope means one bean instance per Spring ApplicationContext, while the GoF Singleton pattern makes the class itself responsible for ensuring only one instance exists and providing global access to it.

6. **Put these lifecycle steps of a bean in order and explain what happens in each: @PreDestroy, constructor, BeanPostProcessor.postProcessAfterInitialization(), @PostConstruct, setter/field injection. In which step are AOP proxies created?**

   ​	The lifecycle order is 

   ​	1. constructor, 

   ​	2. dependency injection,

   ​	3. @PostConstruct, 

   ​	4. BeanPostProcessor.postProcessAfterInitialization(),

   ​	5. and finally @PreDestroy when the application shuts down.

   ​	 The constructor creates the object, setter/field injection provides dependencies, @PostConstruct performs initialization, and post-processors can wrap the bean before it is used. Spring AOP proxies are typically created during postProcessAfterInitialization(). @PreDestroy runs before the bean is destroyed.

7. **Explain how auto-configuration works: where Spring Boot finds the candidate configuration classes, and how @ConditionalOnClass and @ConditionalOnMissingBean decide whether a configuration is applied. Why does defining your own ObjectMapper bean make Spring Boot's one back off?**

   ​	Spring Boot discovers auto-configuration candidates from metadata provided by its auto-configuration modules, such as AutoConfiguration.imports. It then evaluates conditions on those configuration classes. @ConditionalOnClass enables a configuration only when a required class is available on the classpath, while @ConditionalOnMissingBean enables a bean only when the application has not already defined a matching bean.

8. **Compare @Value and @ConfigurationProperties, and list these property sources from highest to lowest precedence: application.yml, OS environment variables, command-line arguments, profilespecific files, Java system properties.**

   ​	@Value is useful for injecting individual configuration values, while @ConfigurationProperties is better for binding a group of related properties into a strongly typed object. @ConfigurationProperties is easier to maintain and validate when many settings belong together. For the listed property sources, the precedence from highest to lowest is: command-line arguments, Java system properties, OS environment variables, profile-specific configuration files, and finally application.yml
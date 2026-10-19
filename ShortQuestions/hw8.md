1.When OrderService creates dependencies with new, it becomes tightly coupled to specific classes, making it difficult to change or test. IoC moves object creation outside OrderService, and dependency injection provides the required objects through its constructor. This makes the code more flexible and easier to test.
2. @SpringBootApplication combines:
- @SpringBootConfiguration: marks the class as the main configuration class.
- @EnableAutoConfiguration: automatically configures Spring based on available dependencies.
- @ComponentScan: searches for Spring components such as @Service and @Controller.
  A @Service in a sibling package is not found because component scanning only searches the main class’s package and its subpackages. Move the main class to a common parent package or specify scanBasePackages.
3.- Constructor injection: Dependencies are passed through the constructor. It makes required dependencies clear and supports immutability and easy testing.
- Setter injection: Dependencies are provided through setter methods. It is useful for optional dependencies but allows incomplete objects.
- Field injection: Dependencies are injected directly into fields using @Autowired. It is concise but hides dependencies and makes testing harder.
  Constructor injection is recommended because it ensures all required dependencies are provided when the object is created, prevents incomplete objects, and makes the class easier to test.
4.Spring throws a NoUniqueBeanDefinitionException because it cannot decide which NotificationSender bean to inject.
  Three solutions are:
- -Mark one implementation with @Primary.
- Select a specific bean using @Qualifier.
- Inject all implementations as a List<NotificationSender> or Map<String, NotificationSender> and choose one in the code.
5.A singleton ReportService is shared by all requests and threads. If it stores report data in a shared List field, different requests may modify the same list, causing mixed data and race conditions. Therefore, singleton beans should keep request-specific data in local variables and remain stateless.
  Spring singleton scope means one bean instance per application context. The GoF Singleton pattern means a class controls its own creation and usually allows only one instance in the JVM through a private constructor and static method.
6.Correct order:
- Constructor – Spring creates the bean object. 
- Setter/field injection – Spring injects its dependencies.
- @PostConstruct – The bean performs initialization after injection. 
- postProcessAfterInitialization() – Spring performs final processing; AOP proxies are usually created here.
- @PreDestroy – Cleanup runs before the bean is destroyed or the application shuts down.
7.Spring Boot’s @EnableAutoConfiguration loads candidate configuration classes from:
  META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

- @ConditionalOnClass applies a configuration only if the required class exists on the classpath.
- @ConditionalOnMissingBean applies it only if the application has not already defined a matching bean.
  Spring Boot’s ObjectMapper is conditional on a missing ObjectMapper bean. Therefore, when you define your own, Spring Boot backs off and uses yours instead of creating another one.
8.- @Value injects individual property values, such as ${server.port}. It is suitable for a few simple properties.
- @ConfigurationProperties binds a group of related properties to a typed Java class. It supports structured configuration, relaxed binding, and validation.
  Property precedence from highest to lowest:
- Command-line arguments 
- Java system properties 
- OS environment variables 
- Profile-specific files, such as application-dev.yml 
- application.yml
   A higher-precedence value overrides the same property from a lower-precedence source.

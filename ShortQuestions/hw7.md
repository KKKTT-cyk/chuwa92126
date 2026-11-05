# Homework 7 - Spring Boot

## 1. IoC and Dependency Injection

When OrderService creates a dependency with `new EmailSender()`, it is tied to that implementation. Changing the sender requires editing the service, and testing with a fake sender is harder.

IoC lets Spring manage object creation and wiring. DI supplies dependencies to OrderService, for example through its constructor. Depending on a NotificationSender interface makes implementations easier to replace and test.

## 2. @SpringBootApplication

It combines:

- `@SpringBootConfiguration`: marks a configuration class that can define beans; it is based on `@Configuration`.
- `@EnableAutoConfiguration`: enables configuration based on the classpath, existing beans, and other conditions.
- `@ComponentScan`: finds components in the main class's package and its subpackages.

A `@Service` in a sibling package is outside the default scan. Move the main class to a common parent package or set `scanBasePackages` to include the service package.

## 3. Injection Types

- **Constructor injection**: passes dependencies through the constructor. Best for required dependencies; supports final fields and easy unit testing.
- **Setter injection**: supplies dependencies after construction. Useful for optional dependencies when optional behavior is configured.
- **Field injection**: injects directly into annotated fields. Short, but hides dependencies and makes testing without Spring harder.

Constructor injection is recommended because required dependencies are explicit and available when the object is created. A single constructor does not need `@Autowired`.

## 4. Multiple NotificationSender Beans

If Spring needs one NotificationSender but cannot choose between two beans, it throws `NoUniqueBeanDefinitionException`, often wrapped in a startup dependency error.

Three solutions are:

1. Use `@Primary` to choose a default bean.
2. Use `@Qualifier("emailSender")` to select a specific bean.
3. Inject `List<NotificationSender>` or `Map<String, NotificationSender>` when multiple senders are needed, then choose or use them in the service.

## 5. Singleton Beans and State

A singleton ReportService is shared by requests. A shared mutable List field can mix different users' report data and cause concurrency problems. Keep each report's data in a local variable instead.

Singleton services should normally avoid request-specific state. Immutable configuration or properly managed shared state can still be valid; Spring does not require every singleton to have no state.

Spring singleton scope means one instance per bean definition per container. The GoF Singleton enforces one instance of a class through its own design, usually a private constructor and static access, within a class loader.

## 6. Bean Lifecycle

The normal order of the listed steps is:

1. **Constructor**: creates the object and receives constructor dependencies.
2. **Setter/field injection**: supplies the remaining dependencies.
3. **@PostConstruct**: runs initialization after injection.
4. **BeanPostProcessor.postProcessAfterInitialization()**: processes the initialized bean and may wrap it. Standard AOP proxies are normally created here for eligible beans.
5. **@PreDestroy**: runs cleanup when Spring destroys the bean, normally during graceful shutdown.

## 7. Auto-configuration

Modern Spring Boot finds candidate auto-configuration classes in dependency JARs at:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Boot checks their conditions and applies matching configurations. Older versions used `META-INF/spring.factories`.

- `@ConditionalOnClass`: requires the specified classes on the classpath.
- `@ConditionalOnMissingBean`: applies only when no matching bean exists.

In Spring Boot 3 with Jackson 2, the default ObjectMapper bean is conditional on a missing ObjectMapper. Defining our own bean makes Boot skip its default mapper. Other Jackson configuration can still apply.

## 8. Properties and Precedence

`@Value` injects individual values and supports defaults and SpEL expressions. `@ConfigurationProperties` binds related settings to a typed object and supports relaxed binding, nested properties, and validation when configured. It is better for groups of settings.

Highest to lowest precedence among the listed sources:

1. Command-line arguments.
2. Java system properties.
3. OS environment variables.
4. Active profile-specific files, such as `application-dev.yml`.
5. General `application.yml`.

Higher-precedence values override lower ones. The file ordering assumes the same location or location group.

## 1. Using the OrderService example from class, explain the problems caused when a class creates its own dependencies with new, and how Inversion of Control and dependency injection solve them.

When `OrderService` creates a dependency itself, such as `private final PaymentService paymentService = new PaymentService();`, it becomes tightly coupled to a specific implementation. Replacing that dependency requires changing `OrderService`, and unit tests cannot easily substitute a fake or mock. Inversion of control (IoC) transfers responsibility for creating and wiring objects to a container such as Spring. Dependency injection (DI) is how Spring supplies those objects, for example through a constructor:

```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring creates and manages the dependency and passes it to `OrderService`. This reduces coupling and makes testing and changing implementations easier.

## 2. Name the three annotations that @SpringBootApplication combines and what each one does.Explain why a @Service class placed in a sibling package of the main class is not found at startup.

`@SpringBootApplication` combines three annotations:

- `@SpringBootConfiguration` (a specialized `@Configuration`): marks the class as a source of Spring bean definitions.
- `@EnableAutoConfiguration`: asks Spring Boot to configure beans based on available libraries, properties, and existing beans.
- `@ComponentScan`: scans for Spring components such as `@Component`, `@Service`, `@Repository`, and `@Controller`.

By default, component scanning starts at the package containing the main application class and includes its subpackages, not sibling packages. A `@Service` in a sibling package therefore is not registered automatically. Move the main class to a common parent package, relocate the service under the scanned package, or explicitly configure scanning with `@ComponentScan(basePackages = {"com.example.app", "com.example.services"})`.

## 3. Compare constructor injection, setter injection and field injection, and explain why constructor injection is recommended.

Constructor injection supplies dependencies when an object is created. Required dependencies can be declared `final`, and missing dependencies fail fast during bean creation. Setter injection supplies dependencies after construction; it can be useful for optional or reconfigurable dependencies, but the object can temporarily exist without them. Field injection uses `@Autowired` directly on fields, which hides dependencies and makes plain unit tests and immutable design harder.

Constructor injection is generally recommended because it makes dependencies explicit, supports immutability, and allows easy unit testing without starting Spring. A Spring bean with one constructor does not need `@Autowired` on that constructor.

## 4. An application context contains two beans that implement NotificationSender. What happens when another bean asks for a NotificationSender, and what are three ways to resolve it?

If two beans implement `NotificationSender` and Spring is asked to inject one by type, resolution is ambiguous and startup normally fails with `NoUniqueBeanDefinitionException` (often wrapped in an `UnsatisfiedDependencyException`). Three ways to resolve this are:

1. Mark one implementation `@Primary` to make it the default candidate.
2. Use `@Qualifier("emailNotificationSender")` at the injection point to select a specific bean.
3. Inject `List<NotificationSender>` or `Map<String, NotificationSender>` and choose or use the implementations explicitly.

Spring can sometimes resolve a single dependency by matching the injection-point name to a bean name, but relying on this is less explicit than a qualifier.

## 5. Explain why singleton beans must be stateless, using the ReportService example with a shared List field, and explain how Spring's singleton scope differs from the GoF Singleton pattern.

Spring singleton beans have one instance per bean definition per application context, so concurrent requests can access the same object. If `ReportService` stores request-specific results in a shared mutable `List` field, one user's data may mix with another's, and concurrent modifications can produce races or corrupted results. Prefer method-local lists and keep request-specific state out of singleton fields:

```java
@Service
public class ReportService {
    public List<String> generateReport() {
        List<String> lines = new ArrayList<>();
        // Build this request's report using local state.
        return lines;
    }
}
```

Strictly speaking, singleton beans **can** contain safely managed state (such as immutable configuration or properly synchronized caches); being stateless is a strong default for ordinary services, not an absolute rule. Spring's singleton scope is managed per container and bean definition, while the GoF Singleton pattern typically enforces one instance through the class's own construction/access mechanism, often using a static field or method.

## 6. Put these lifecycle steps of a bean in order and explain what happens in each: @PreDestroy, constructor, BeanPostProcessor.postProcessAfterInitialization(), @PostConstruct, setter/field injection. In which step are AOP proxies created?

The listed steps occur in this order:

1. Constructor: Spring instantiates the bean.
2. Setter/field injection:** Spring populates dependencies and configured properties.
3. `@PostConstruct`: initialization callback runs after dependencies are injected; it is typically invoked through a bean post-processor during the before-initialization phase.
4. `BeanPostProcessor.postProcessAfterInitialization()`: post-processors can modify or wrap the initialized bean; **AOP proxies are commonly created or returned at this stage.
5. `@PreDestroy`:cleanup callback runs when the context closes and destroys the managed bean.

This is a simplified lifecycle: before-initialization post-processors and other initialization callbacks also exist. Certain proxying mechanisms may expose proxies earlier for circular-reference handling.

## 7. Explain how auto-configuration works: where Spring Boot finds the candidate configuration classes, and how @ConditionalOnClass and @ConditionalOnMissingBean decide whether a configuration is applied. Why does defining your own ObjectMapper bean make Spring Boot's one back off?

Spring Boot discovers auto-configuration candidates from `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in dependency JARs (Spring Boot 3.x and current versions). Older Boot versions used `META-INF/spring.factories` for this registration.

`@ConditionalOnClass` enables a configuration only if specified classes are on the classpath, indicating a relevant library is present. `@ConditionalOnMissingBean` allows default beans to be created only when an appropriate user-defined bean is not already registered. For example, when an application defines its own `ObjectMapper` bean, the matching conditional default `ObjectMapper` bean backs off so the application can control its configuration. This is the convention-over-configuration approach: Boot supplies sensible defaults while allowing explicit overrides.

## 8. Compare @Value and @ConfigurationProperties, and list these property sources from highest to lowest precedence: application.yml, OS environment variables, command-line arguments, profile- specific files, Java system properties.

`@Value("${app.timeout}")` injects an individual property and is convenient for a few simple settings; it also supports SpEL expressions. `@ConfigurationProperties(prefix = "app")` binds a group of related properties to a typed Java class or record. It is preferable for larger configurations because it supports structured/nested values, relaxed binding, validation, and easier maintenance.

For the listed sources, highest to lowest precedence in a typical Spring Boot application is:

1. Command-line arguments (`--server.port=8081`)
2. Java system properties (`-Dserver.port=8082`)
3. OS environment variables (`SERVER_PORT=8083`)
4. Profile-specific files (`application-dev.yml`, when that profile is active)
5. General `application.yml`

This ordering assumes the standard Spring Boot externalized-configuration setup. Other property sources (such as test overrides, `SPRING_APPLICATION_JSON`, and configuration files in different locations) can affect the full precedence order.

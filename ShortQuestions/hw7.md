# HW7 - Java Design Patterns and Spring Boot

The question numbers below follow each assignment separately. The Spring answers use Spring Boot 3.x, Spring Framework 6.x, and Jackson 2 for the `ObjectMapper` example. Examples illustrate the concepts in the questions; they do not reproduce unavailable classroom source code.

## Part A - Java Design Patterns

### 1. Shape area, instanceof, and the Open/Closed Principle

An `instanceof` implementation puts every shape's area formula in one calculator. It checks whether an object is a Circle or Rectangle, casts it, and calculates `Math.PI * radius * radius` or `width * height`. Adding Triangle requires editing that calculator to add another branch. The calculator depends on concrete shape classes and must change whenever a new shape is introduced.

The refactor introduces a `Shape` interface with `double area()`. Circle and Rectangle implement their own formulas. The calculator simply calls `shape.area()`, and Java selects the implementation through polymorphism. Triangle can implement `Shape` without changing the calculator or existing shapes. This follows OCP: the calculator is open to extension through new implementations and closed to modification for each new shape type.

### 2. Factory Method vs. Abstract Factory

Factory Method defines a creation method for one product type and lets subclasses choose the concrete implementation. For example, subclasses of a `NotificationCreator` can override `createNotification()` to return EmailNotification or SmsNotification.

Abstract Factory creates a family of related products. A GUI factory might create both Button and Checkbox; Windows and Mac factories each supply matching versions of both. The difference is one product type versus a family of product types, not the number of object instances created. Abstract Factory can use Factory Methods internally.

### 3. Telescoping constructors and Builder

Telescoping constructors are overloaded constructors with increasing numbers of parameters. Long argument lists are difficult to read, especially when multiple arguments have the same type. Callers can swap values accidentally, must remember positional meanings, and may need placeholders for omitted options. More options also lead to more overloads.

A Builder takes required fields in its constructor and exposes named, chainable methods for optional fields. It supplies defaults and returns the completed object through `build()`. For example, `new Pizza.Builder("large").cheese(true).crust("thin").build()` communicates each option clearly. A builder can validate input and construct an immutable product with private final fields and no setters.

### 4. Strategy

The Context holds a reference to a Strategy and delegates an algorithm to it. The Strategy interface declares the operation, and concrete strategies implement alternative algorithms. For example, a checkout Context might use a PaymentStrategy implemented by CardPayment and PaypalPayment.

The Context can change its active strategy through a setter or another configuration method. Switching strategies at runtime changes behavior without rewriting the Context or adding algorithm-selection branches inside it. Each algorithm can also be maintained and tested independently.

### 5. Observer

A Subject keeps a collection of observers, so one subject can notify many observers when an event or state change occurs. The Observer interface defines a callback, such as `update()`. Observers subscribe and unsubscribe, and the Subject calls the callback on each registered observer.

The Subject depends on the Observer interface rather than concrete observer classes. A new observer type only needs to implement the interface and register with the Subject. Therefore, its addition does not require changes to the Subject's notification code.

### 6. Decorator and subclass explosion

If every combination of optional features requires a subclass, the number of subclasses grows rapidly. For example, coffee with milk, sugar, whipped cream, or combinations of those additions would require many subclasses.

Decorator solves this through composition. Each decorator implements the component interface, wraps another component, delegates to it, and adds behavior. Features can be combined by stacking wrappers at runtime.

A real Java example is `new BufferedInputStream(new FileInputStream("data.txt"))`. Both are InputStreams, and BufferedInputStream adds buffering while preserving the stream interface.

### 7. Proxy vs. Decorator

Both typically implement the same interface as the wrapped object and delegate operations to it. Their primary intent differs: Proxy controls access to the object, while Decorator adds responsibilities or behavior.

A virtual proxy can delay creating an expensive object until its first use; a protection proxy can check permission. A decorator can add compression or buffering to an existing stream. Similar structure does not make the patterns interchangeable because they solve different problems.

### 8. Adapter vs. Decorator vs. Proxy

| Pattern | Interface relationship | Primary intent |
| --- | --- | --- |
| Adapter | Exposes the interface the client expects while wrapping an object with a different interface. | Makes incompatible APIs work together. |
| Decorator | Preserves the component interface. | Adds behavior through composable wrappers. |
| Proxy | Preserves the subject interface. | Controls access, creation, or communication with the subject. |

For example, adapting a legacy payment API changes its interface; adding logging with a decorator extends behavior; a remote proxy hides communication with a service.

### 9. Lazy Singleton and Double-Checked Locking

In a naive lazy Singleton, two threads can both observe `instance == null`, then each construct and return a different instance. The check and assignment are not atomic. Without safe publication, another thread may also observe an incompletely initialized object.

Double-Checked Locking uses an outer null check, synchronization, and another null check inside the lock. Only one thread initializes the instance; later calls normally avoid the lock. The `volatile` field provides visibility and prevents publication of the reference before initialization is complete. It is required even though creation is synchronized, because the fast path reads outside the lock. This works with the Java 5+ memory model.

```java
public final class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### 10. Choose the pattern

| Situation | Pattern | Reason |
| --- | --- | --- |
| (a) Exactly one shared instance, accessible globally | Singleton | Controls instance creation and provides a shared access point. |
| (b) Interchangeable algorithms selected at runtime | Strategy | Delegates to a replaceable algorithm implementation. |
| (c) Delay an expensive object's creation without changing calling code | Proxy, specifically Virtual Proxy | Implements the same interface and creates the real object when needed. |

### 11. Notification factory implementation

Complete, compilable source is in [Coding/hw7](../Coding/hw7/): `Notification.java`, `EmailNotification.java`, `SmsNotification.java`, and `NotificationFactory.java`. From that directory, compile the programming answers with `javac *.java`.

`Notification` declares `void send(String message)`. Both implementations print a simulated notification. `NotificationFactory.create("email")` returns an EmailNotification, and `create("sms")` returns an SmsNotification. Null or unsupported types throw IllegalArgumentException.

Terminology note: the question calls this Factory Method, but its required static `create(String type)` implementation is usually called a Simple Factory. GoF Factory Method uses an overridable creation method in a creator hierarchy. This solution follows the requested static API exactly.

### 12. Immutable Pizza Builder implementation

Complete, compilable source is in [Pizza.java](../Coding/hw7/Pizza.java). Example usage: `new Pizza.Builder("large").cheese(true).crust("thin").build()`.

`Pizza.Builder` is a static inner class. Its constructor requires `size`; cheese, pepperoni, and mushrooms default to false; crust defaults to `"regular"`. Each option method returns `this` to support chaining, and `build()` constructs a Pizza.

Pizza is final, has only private final fields, exposes getters, and has no setters. Its constructor copies the builder's values. Because those values are primitives and immutable Strings, changing or reusing a builder cannot change an existing Pizza. This implementation uses a nonblank String for size and rejects null or blank size/crust values.

## Part B - Spring Boot

### 1. OrderService, IoC, and dependency injection

If OrderService constructs its dependencies with statements such as `new EmailNotificationSender()`, it chooses a concrete implementation and takes responsibility for creating it. Replacing that implementation requires editing the service. Tests cannot easily substitute a fake, and directly created objects do not automatically receive Spring's lifecycle management or AOP support.

With Inversion of Control, the container manages object creation and wiring. Dependency injection supplies the service's collaborators from outside. OrderService can depend on a NotificationSender interface and receive it through its constructor:

```java
@Service
public class OrderService {
    private final NotificationSender sender;

    public OrderService(NotificationSender sender) {
        this.sender = sender;
    }
}
```

This illustrative snippet assumes an existing NotificationSender interface and a registered implementation. Configuration chooses the implementation; tests can pass a fake directly. IoC is the broader principle, and DI is the mechanism used here. [Spring DI documentation](https://docs.spring.io/spring-framework/reference/6.2/core/beans/dependencies/factory-collaborators.html).

### 2. SpringBootApplication and component scanning

The three combined annotations are:

- `@SpringBootConfiguration`: identifies the application configuration class; it is itself meta-annotated with `@Configuration`, which supports `@Bean` definitions.
- `@EnableAutoConfiguration`: enables Boot's conditional auto-configuration.
- `@ComponentScan`: discovers components such as `@Component`, `@Service`, `@Repository`, and `@Controller`.

This is often summarized as Configuration + EnableAutoConfiguration + ComponentScan. By default, scanning starts in the main class's package and includes its descendants. If the main class is in `com.example.app` and a Service is in sibling package `com.example.service`, the Service is outside that scan tree. Its annotation alone does not register it. Move the main class to `com.example`, or set `@SpringBootApplication(scanBasePackages = "com.example")`. [Spring Boot annotation documentation](https://docs.spring.io/spring-boot/3.5/reference/using/using-the-springbootapplication-annotation.html).

### 3. Constructor, setter, and field injection

| Style | How dependencies arrive | Main tradeoff |
| --- | --- | --- |
| Constructor | Arguments supplied when the bean is constructed. | Makes required dependencies explicit and allows final fields. |
| Setter | Setter methods called after construction. | Useful for optional collaborators with defaults, but permits later changes. |
| Field | Spring writes directly to annotated fields after construction. | Concise, but hides dependencies and makes plain-Java testing harder. |

Constructor injection is recommended for mandatory collaborators because an object is constructed with its required dependencies, references can be final, and tests can call its constructor without Spring or reflection. Setter and field dependencies are unavailable during construction. An autowired setter can still be required; setter injection is not automatically optional. For a Spring bean with a single constructor, `@Autowired` on that constructor is unnecessary. [Spring autowiring documentation](https://docs.spring.io/spring-framework/reference/6.2/core/beans/annotation-config/autowired.html).

### 4. Two NotificationSender beans

For a single-valued injection point, if neither bean is preferred or otherwise uniquely matched, Spring cannot select one and reports NoUniqueBeanDefinitionException, usually wrapped in an UnsatisfiedDependencyException during startup.

Three ways to resolve it are:

1. Use `@Qualifier("emailSender")` at the injection point, for example on a constructor parameter, to narrow the candidates to the intended bean.
2. Mark exactly one implementation `@Primary` to make it the default for single-valued type-based injection.
3. Use explicit name-based injection, for example `@Resource(name = "emailSender")` on a field or setter. In Spring 6, Resource is from `jakarta.annotation`.

If the consumer intentionally needs both, it can instead inject `List<NotificationSender>`; that changes the dependency to a collection rather than selecting one sender. [Qualifier documentation](https://docs.spring.io/spring-framework/reference/6.2/core/beans/annotation-config/autowired-qualifiers.html), [Resource documentation](https://docs.spring.io/spring-framework/reference/6.2/core/beans/annotation-config/resource.html).

### 5. Stateless singleton services and Spring vs. GoF Singleton

A singleton ReportService is shared by callers. If it stores a mutable List field and clears/adds report rows per request, concurrent requests can interleave operations, overwrite each other's results, or mix users' data. Spring's singleton scope does not make that List thread-safe. Keep request-specific rows in a method-local List instead.

Statelessness is the usual safe design for singleton services, not a container-enforced rule. Immutable configuration is fine; deliberately shared mutable state requires a correct concurrency design. Synchronizing a collection alone also does not separate one user's report from another's.

Spring singleton means one instance per bean definition per container. Multiple definitions of the same class, or multiple containers, can have separate instances. Spring manages the scope without requiring a private constructor or static accessor. GoF Singleton controls creation in the class itself, usually with a private constructor and global static access, conventionally yielding one instance per defining class loader. [Spring bean scopes](https://docs.spring.io/spring-framework/reference/6.2/core/beans/factory-scopes.html).

### 6. Bean lifecycle and AOP proxies

For an ordinary managed singleton, the requested order is:

1. **Constructor:** instantiate the bean; constructor dependencies are supplied at this point.
2. **Setter/field injection:** populate dependencies configured for those injection styles.
3. **`@PostConstruct`:** run initialization after dependency injection. It is invoked through a BeanPostProcessor before the normal after-initialization phase.
4. **`BeanPostProcessor.postProcessAfterInitialization()`:** process the initialized bean and potentially return a wrapper. Standard Spring AOP auto-proxying normally creates/returns the proxy here.
5. **`@PreDestroy`:** release resources when the container destroys the bean, typically at context shutdown, after its useful lifetime.

The listed steps omit other callbacks, such as Aware methods, before-initialization processing, and custom init methods. `@PostConstruct` normally runs on the target before its AOP proxy is exposed; calls there should not rely on transactional advice on that bean. Prototype destruction is not automatically managed by Spring. [Spring lifecycle documentation](https://docs.spring.io/spring-framework/reference/6.2/core/beans/factory-nature.html).

### 7. Auto-configuration and ObjectMapper

`@EnableAutoConfiguration` triggers selection of candidate configuration classes from dependency JAR resources named `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`. Boot evaluates their conditions and registers applicable configurations/beans. These candidates are imported rather than found by ordinary application component scanning. Older Boot versions used the EnableAutoConfiguration entry in `META-INF/spring.factories`; the imports mechanism is used by Boot 3.x.

`@ConditionalOnClass` matches when the specified class is available on the classpath. `@ConditionalOnMissingBean` matches when no qualifying bean of the specified type is already registered. Conditions can appear on a configuration class or on individual bean methods. This lets defaults adapt to available libraries and user configuration. [Auto-configuration documentation](https://docs.spring.io/spring-boot/3.5/reference/features/developing-auto-configuration.html).

For Jackson 2, Boot's default ObjectMapper bean method has `@ConditionalOnMissingBean`. Defining a discoverable `@Bean` whose declared return type is ObjectMapper makes that condition fail, so Boot skips creating its default mapper. Other Jackson configuration can still apply; defining a mapper does not disable all auto-configuration. [Boot Jackson configuration source](https://github.com/spring-projects/spring-boot/blob/v3.5.7/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jackson/JacksonAutoConfiguration.java).

### 8. Value, ConfigurationProperties, and property precedence

`@Value` injects individual values with placeholders, for example `@Value("${app.timeout:30}")`, and supports SpEL expressions. `@ConfigurationProperties(prefix = "app")` binds a group of properties to a typed object, including nested values and collections. It supports relaxed binding and validation when configured with `@Validated` and a Bean Validation provider. Register such classes through ConfigurationPropertiesScan, EnableConfigurationProperties, or another supported bean-registration mechanism. Use Value for isolated values and ConfigurationProperties for related settings; the latter does not evaluate SpEL.

For the five sources in the question, from highest to lowest precedence:

1. Command-line arguments, such as `--app.timeout=10`.
2. Java system properties, such as `-Dapp.timeout=20`.
3. OS environment variables, such as `APP_TIMEOUT=30`.
4. Active profile-specific files, such as `application-dev.yml`.
5. The base `application.yml`.

The file comparison assumes the same location/location group and an active profile. File locations matter too: an external base file can override a packaged profile-specific file. [Spring Boot external configuration documentation](https://docs.spring.io/spring-boot/3.5/reference/features/external-config.html).

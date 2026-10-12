# Java Spring Boot Homework

## 1. Inversion of Control (IoC) and Dependency Injection (DI)

When a class creates its own dependencies using `new`, it becomes tightly coupled to specific implementations.

For example:

```java
public class OrderService {
    private PaymentService paymentService =
        new PaymentService();
}
```

This causes several problems:

- **Tight coupling:** `OrderService` directly depends on a concrete `PaymentService` implementation.
- **Difficult testing:** Replacing the real dependency with a mock during unit testing becomes difficult.
- **Poor flexibility:** Changing the implementation requires modifying `OrderService`.
- **Responsibility for object creation:** The class must manage its own dependencies.

**Inversion of Control (IoC)** solves this by transferring responsibility for creating and managing objects to the Spring container.

**Dependency Injection (DI)** is a way to implement IoC. Spring creates the required dependencies and injects them into the class.

For example:

```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

This reduces coupling, improves testability, and makes the application easier to maintain.

## 2. @SpringBootApplication and Component Scanning

The `@SpringBootApplication` annotation combines three annotations:

| Annotation | Purpose |
|---|---|
| `@SpringBootConfiguration` | Marks the class as a Spring Boot configuration class. It is a specialized form of `@Configuration`. |
| `@EnableAutoConfiguration` | Enables Spring Boot to configure beans automatically based on dependencies and application settings. |
| `@ComponentScan` | Automatically discovers Spring components such as `@Component`, `@Service`, `@Repository`, and `@Controller`. |

By default, component scanning starts from the package containing the main application class and includes its subpackages.

For example:

```text
com.example.app
    Application.java
    service/
        OrderService.java

com.example.other
    PaymentService.java
```

If `Application.java` is in `com.example.app`, Spring scans `com.example.app` and its subpackages but not the sibling package `com.example.other`.

Therefore, a `@Service` class in the sibling package is not discovered automatically.

This can be fixed by moving the main class to a common parent package or explicitly configuring component scanning:

```java
@SpringBootApplication(scanBasePackages = "com.example")
public class Application {
}
```

## 3. Constructor Injection vs. Setter Injection vs. Field Injection

Spring supports three common types of dependency injection.

| Type | How it works | Main characteristic |
|---|---|---|
| Constructor injection | Dependencies are passed through a constructor | Required dependencies are provided at creation |
| Setter injection | Dependencies are assigned through setter methods | Supports optional or replaceable dependencies |
| Field injection | Dependencies are injected directly into fields | Concise but harder to test |

**Constructor injection:**

```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

**Setter injection:**

```java
@Autowired
public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

**Field injection:**

```java
@Autowired
private PaymentService paymentService;
```

Constructor injection is recommended because it makes required dependencies explicit, supports immutable fields using `final`, makes unit testing easier, and prevents objects from being created without their required dependencies.

For a class with only one constructor, Spring does not require `@Autowired` on that constructor.

## 4. Multiple Beans Implementing the Same Interface

Suppose two Spring beans implement the same interface:

```java
public interface NotificationSender {
    void send(String message);
}
```

```java
@Service
public class EmailSender implements NotificationSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

```java
@Service
public class SmsSender implements NotificationSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
```

If another bean requests a `NotificationSender`, Spring finds two matching beans.

Without a way to determine which one to use, Spring typically throws a `NoUniqueBeanDefinitionException`.

Three common solutions are:

### 1. @Primary

Marks one implementation as the default choice.

```java
@Primary
@Service
public class EmailSender implements NotificationSender {
    public void send(String message) {
        System.out.println(message);
    }
}
```

### 2. @Qualifier

Specifies which bean should be injected.

```java
@Service
public class OrderService {
    private final NotificationSender sender;

    public OrderService(
        @Qualifier("smsSender") NotificationSender sender
    ) {
        this.sender = sender;
    }
}
```

### 3. Inject a collection

Inject all matching implementations and choose the appropriate one when needed.

```java
@Service
public class NotificationService {
    private final List<NotificationSender> senders;

    public NotificationService(List<NotificationSender> senders) {
        this.senders = senders;
    }
}
```

These approaches allow Spring to resolve dependency ambiguity.

## 5. Singleton Beans and Statelessness

By default, Spring beans use the singleton scope, meaning Spring creates one shared instance of a bean per bean definition within an application context.

Because the instance is shared by multiple requests and threads, storing mutable request-specific data in instance fields can cause concurrency problems.

For example:

```java
@Service
public class ReportService {
    private List<String> reports = new ArrayList<>();

    public void addReport(String report) {
        reports.add(report);
    }
}
```

Here, all requests share the same `reports` list. This can cause data leakage between requests, race conditions, and inconsistent results.

A safer approach is to keep request-specific data in local variables:

```java
@Service
public class ReportService {
    public List<String> generateReport() {
        List<String> reports = new ArrayList<>();
        reports.add("New Report");
        return reports;
    }
}
```

Singleton beans should generally be stateless or avoid unsynchronized mutable shared state.

**Spring Singleton vs. GoF Singleton:**

| Spring Singleton | GoF Singleton |
|---|---|
| Managed by the Spring container | Managed by the class itself |
| One instance per bean definition per application context | Typically one instance per class within a class loader |
| Created and managed through Spring configuration | Usually uses a private constructor and static access method |
| Multiple application contexts can have separate instances | Intended to provide one globally accessible instance |

Spring's singleton scope is container-managed, while the GoF Singleton pattern controls instance creation within the class.

## 6. Spring Bean Lifecycle

The correct order is:

1. Constructor
2. Setter/field injection
3. `@PostConstruct`
4. `BeanPostProcessor.postProcessAfterInitialization()`
5. `@PreDestroy`

**Step 1: Constructor**

Spring creates the bean instance by calling its constructor. Constructor injection also occurs during this step.

**Step 2: Setter/field injection**

Spring injects dependencies into fields or setter methods.

**Step 3: @PostConstruct**

Spring invokes the initialization method after dependency injection is complete. This is commonly used to perform initialization tasks.

**Step 4: BeanPostProcessor.postProcessAfterInitialization()**

Spring invokes bean post-processors after initialization. This allows additional processing, including wrapping beans with proxies.

**Step 5: @PreDestroy**

When the application context shuts down normally, Spring invokes the destruction callback to release resources such as connections or threads.

**When are AOP proxies created?**

AOP proxies are typically created during the `postProcessAfterInitialization()` phase by Spring's auto-proxy infrastructure.

This allows Spring to wrap the original bean with additional behavior such as transaction management or method interception.

## 7. Spring Boot Auto-Configuration

Spring Boot auto-configuration automatically configures application components based on dependencies available on the classpath, existing beans, and configuration properties.

In modern Spring Boot versions, candidate auto-configuration classes are listed in:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Spring Boot loads these candidates and evaluates their conditions to decide which configurations should be applied.

### @ConditionalOnClass

This annotation activates a configuration only when specified classes are available on the classpath.

For example:

```java
@ConditionalOnClass(DataSource.class)
```

This condition matches when `DataSource` is available.

### @ConditionalOnMissingBean

This annotation activates a configuration only when a matching bean has not already been registered.

For example:

```java
@Bean
@ConditionalOnMissingBean(ObjectMapper.class)
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

**Why does a custom ObjectMapper make Spring Boot's default back off?**

Spring Boot's default `ObjectMapper` bean configuration uses a missing-bean condition.

If the application defines its own `ObjectMapper` bean, the condition no longer matches, so Spring Boot does not create its default `ObjectMapper` bean.

This allows developers to override default auto-configuration without modifying Spring Boot's internal code.

## 8. @Value vs. @ConfigurationProperties and Property Precedence

Both `@Value` and `@ConfigurationProperties` are used to access external configuration values in Spring Boot.

### @Value

Injects individual configuration values directly into fields, constructors, or methods.

```java
@Value("${app.name}")
private String appName;
```

It is useful when only a few configuration values are needed.

### @ConfigurationProperties

Binds a group of related configuration properties to a Java object.

For example:

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
```

This approach provides structured configuration, type-safe binding, and support for validation.

| @Value | @ConfigurationProperties |
|---|---|
| Best for individual values | Best for groups of related values |
| Supports SpEL expressions | Supports structured, type-safe binding |
| Less convenient for large configurations | Easier to maintain for complex configurations |

### Property Source Precedence

From highest to lowest precedence among the five sources listed:

1. **Command-line arguments**
2. **Java system properties**
3. **OS environment variables**
4. **Profile-specific configuration files**
5. **application.yml**

For example, if `server.port` is defined in multiple sources, the command-line argument takes precedence:

```bash
java -jar app.jar --server.port=9090
```

The application uses port `9090`, overriding lower-precedence configuration values.

This precedence system allows applications to use default configuration files while supporting environment-specific settings and runtime overrides.
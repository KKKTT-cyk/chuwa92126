## Q1. Problems with `new` and How IoC/DI Solve Them

**Problem:** `OrderService` creates its own dependencies:

```java
private final OrderRepository repository = new MySqlOrderRepository();
private final PaymentClient paymentClient = new PayPalPaymentClient("api-key123");
```

- **Hard to test:** the real `PayPalPaymentClient` can't be replaced with a fake.
- **Hard to change:** switching to PostgreSQL means editing every class that calls `new MySqlOrderRepository()`.
- **Scattered configuration:** API keys and hosts are buried in classes.
- **No lifecycle management:** nothing creates, shares, or closes these objects.

**Solution:** with **IoC**, the Spring container creates and wires objects instead of the class itself. **DI** is how it does this: dependencies are passed in, usually through the constructor.

```java
public OrderService(OrderRepository repository, PaymentClient paymentClient) {
    this.repository = repository;
    this.paymentClient = paymentClient;
}
```

`OrderService` now depends only on interfaces, so tests can pass fakes, implementations are chosen by configuration, and Spring manages the beans' lifecycle.



## Q2. @SpringBootApplication and Component Scanning

`@SpringBootApplication` combines:

- **`@SpringBootConfiguration`:** marks the class as a configuration class that can define beans.
- **`@EnableAutoConfiguration`:** turns on auto-configuration based on the classpath.
- **`@ComponentScan`:** scans the main class's package and all sub-packages for `@Component`, `@Service`, `@Controller`, etc.

**Why a sibling package is not found:** scanning starts at the main class's package and only goes **down**.

```
com.example.orders                        <- main class
com.example.orders.service.OrderService   FOUND (sub-package)
com.example.payment.PaymentClient         NOT FOUND (sibling package)
```

A `@Service` in a sibling package is never scanned, so its bean is not created and dependent beans fail at startup with "could not be found." Fix: put the main class in the root package.



## Q3. Constructor vs. Setter vs. Field Injection

```java
// Constructor injection
public OrderService(OrderRepository repository) {
    this.repository = repository;
}

// Setter injection
@Autowired(required = false)
public void setAuditLogger(AuditLogger auditLogger) {
    this.auditLogger = auditLogger;
}

// Field injection
@Autowired
private OrderRepository repository;
```

- **Constructor injection:** dependencies are passed through the constructor. Fields can be `final`, required dependencies are explicit, and the class can be tested by calling the constructor with mocks.
- **Setter injection:** dependencies are set through setter methods after the object is created. Fields can't be `final`; useful for optional dependencies.
- **Field injection:** `@Autowired` is placed directly on a field. It is the shortest, but the dependency is hidden, can't be `final`, and testing without Spring requires reflection.

**Why constructor injection is recommended:** the object can't be created without its required dependencies, they can't change afterwards, it is easy to unit test, and circular dependencies fail fast at startup.



## Q4. Multiple Beans of the Same Type

If `EmailSender` and `SmsSender` both implement `NotificationSender`, injecting a `NotificationSender` fails at startup with `NoUniqueBeanDefinitionException`, because Spring finds two matching beans and cannot decide which one to use.

**Three ways to resolve it:**

1. **`@Primary`:** mark one implementation as the default, used whenever no qualifier is given.
2. **`@Qualifier("smsSender")`:** choose a specific bean by name at the injection point.
3. **Inject all of them:** use `List<NotificationSender>` or `Map<String, NotificationSender>` (bean name → bean) and select one at runtime.


## Q5. Stateless Singletons and Spring vs. GoF Singleton

**Why stateless:** a singleton bean has one instance shared by all request threads. In `ReportService`, the shared `List` field can be cleared and filled by several requests at once, mixing their data (a race condition). The fix is to use a local variable inside the method, so each call gets its own list. Singleton fields should only hold dependencies and immutable configuration.

**Spring vs. GoF Singleton:** Spring guarantees one instance per container, enforced by the container; the class is still a normal class that can be created with `new`, so it is easy to test. The GoF Singleton guarantees one instance per class loader, enforced by the class itself through a private constructor, which makes it global state and hard to test.


## Q6. Bean Lifecycle Order

1. **Constructor:** Spring creates the bean instance; constructor injection happens here.
2. **Setter / field injection:** Spring injects the remaining dependencies through setters or fields.
3. **`@PostConstruct`:** runs once after all dependencies are injected, for initialization logic (e.g. loading a cache).
4. **`BeanPostProcessor.postProcessAfterInitialization()`:** Spring gets a final chance to wrap or replace the bean before it is put into use.
5. **`@PreDestroy`:** runs when the application context closes, for cleanup (e.g. releasing resources).

**AOP proxies are created in step 4**, `postProcessAfterInitialization()`. Spring wraps the fully initialized bean in a proxy and other beans receive the proxy instead of the original object.



## Q7. How Auto-Configuration Works

**Where candidates come from:** `@EnableAutoConfiguration` reads a list of candidate configuration classes from every jar on the classpath, in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (e.g. `JacksonAutoConfiguration`).

**How conditions decide:** each candidate is applied only if all its `@Conditional...` annotations match; otherwise it is skipped.
- **`@ConditionalOnClass`:** applies only if a certain class is on the classpath, i.e. the library was added as a dependency.
- **`@ConditionalOnMissingBean`:** applies only if you haven't already defined a bean of that type.

```java
@ConditionalOnClass(ObjectMapper.class)
public class JacksonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}
```

**Why your own `ObjectMapper` makes Boot back off:** Boot's `objectMapper()` is marked `@ConditionalOnMissingBean`. Once you define your own `ObjectMapper` bean, that condition fails, so Boot skips creating its default one and uses yours.



## Q8. @Value vs. @ConfigurationProperties, and Property Precedence

**`@Value`** is written directly on a field in the class that uses it and injects a single property, e.g. `@Value("${payment.base-url}")`. It is simple and fine for one or two values.

**`@ConfigurationProperties`** is a separate class that binds a whole group of properties with a common prefix into one typed object, which is then injected where needed:

```java
@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(String baseUrl, Duration timeout) { }
```

It keeps related settings together, converts types automatically, and supports validation, so it is recommended for groups of settings.

**Property source precedence (highest to lowest):**
1. Command-line arguments
2. Java system properties
3. OS environment variables
4. Profile-specific files (`application-prod.yml`)
5. `application.yml`
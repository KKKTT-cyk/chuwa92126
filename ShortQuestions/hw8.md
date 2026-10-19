# HW8 – Spring Boot

## Question 1.

When `OrderService` creates its own dependencies with `new` (`new MySqlOrderRepository()`, `new PayPalPaymentClient(...)`,
`new SmtpEmailSender(...)`), it decides which implementations it uses, so it is **tightly coupled** to concrete classes.
What goes wrong:

| Problem | Explanation |
|---|---|
| Hard to test | A unit test cannot replace `PayPalPaymentClient` with a fake, because the real one is hardcoded |
| Hard to change | Switching from MySQL to PostgreSQL means editing every class that calls `new MySqlOrderRepository()` |
| Scattered configuration | API keys, hosts and ports are buried inside classes |
| No lifecycle management | Who creates these objects, shares them, and closes their connections on shutdown? |

**Inversion of Control (IoC)** means that instead of your code creating and connecting objects, a container does it, and
your class only declares what it needs ("Don't call us, we'll call you"). **Dependency injection (DI)** is how Spring
implements IoC: the container creates the dependencies and passes them in, for example through the constructor of
`OrderService(OrderRepository, PaymentClient, EmailSender)`. A test can then pass a fake directly, switching an
implementation is done in one place, and the container manages the lifecycle of the objects.

---

## Question 2.

`@SpringBootApplication` combines three annotations:

- `@SpringBootConfiguration`: marks this class as a configuration class (a specialized `@Configuration`).
- `@EnableAutoConfiguration`: turns on auto-configuration.
- `@ComponentScan`: scans **this package and all its sub-packages** for `@Component`, `@Service`, `@Controller`, etc.

`@ComponentScan` starts from the package of the main class and **only goes down**. If the main class is in
`com.example.orders`, then `com.example.orders.service.OrderService` is found (sub-package), but a `@Service` in
`com.example.payment` (a sibling package) is **not found**, so startup fails with an error like "required a bean of type
'com.example.payment.PaymentClient' that could not be found". The fix is to keep the main class in the root package, so
every other class sits underneath it.

---

## Question 3.

| Aspect | Constructor injection | Setter injection | Field injection |
|---|---|---|---|
| Fields can be `final` (immutable) | Yes | No | No |
| Required dependencies are obvious | Yes | No | No |
| Unit testable without Spring | Yes, just call the constructor | Yes | No, needs reflection |
| Circular dependency possible at all | No, always fails at startup | Only with `allow-circular-references=true` | Only with `allow-circular-references=true` |
| Recommended for | Required dependencies | Optional dependencies | Nothing (avoid) |

Constructor injection is recommended because the dependencies are **explicit** in the constructor, the fields can be
`final`, the class can be unit tested by simply calling the constructor, and a circular dependency fails at startup
instead of hiding. Field injection hides the dependency, cannot be `final`, and needs reflection to test.

---

## Question 4.

When another bean asks for a `NotificationSender` and two beans (`emailSender` and `smsSender`) implement it, injection
**fails at startup** with `NoUniqueBeanDefinitionException: expected single matching bean but found 2: emailSender,
smsSender`. Three ways to resolve it:

1. **`@Primary`**: mark one bean as the default, used whenever no qualifier is given.
2. **`@Qualifier`**: choose explicitly at the injection point, e.g. `@Qualifier("smsSender") NotificationSender sender`.
3. **Inject all of them**: inject a `Map<String, NotificationSender>` (bean name to bean) or a
   `List<NotificationSender>`, and pick the right one at runtime.

---

## Question 5.

Tomcat handles every HTTP request on its own thread, but there is **only one instance** of each singleton bean. Any
mutable field in a singleton is therefore **shared state**, which is exactly the race condition from the multithreading
lesson. In `ReportService`, the field `private final List<String> lines = new ArrayList<>()` is shared by all threads:
`buildReport()` clears and fills it, so two concurrent requests can mix their lines. The fix is to make it a **local
variable** inside `buildReport()` so each thread has its own. Rule: keep singleton beans **stateless**. Only hold
dependencies and immutable configuration in fields, and if shared mutable state is truly required, use thread-safe types
such as `AtomicLong` or `ConcurrentHashMap`.

| Aspect | Spring singleton scope | GoF Singleton pattern |
|---|---|---|
| Guarantee | One instance **per container** per bean definition | One instance **per class loader** |
| Enforced by | The container | The class itself (private constructor) |
| Can you call `new`? | Yes, it is a normal class | No |
| Testability | Easy, inject a mock | Hard, global state |

---

## Question 6.

The order is:

1. **Constructor**: the object is instantiated (constructor injection happens here).
2. **Setter/field injection**: the properties are populated.
3. **`@PostConstruct`**: initialization, runs once after all dependencies have been injected.
4. **`BeanPostProcessor.postProcessAfterInitialization()`**: post-processors can wrap the bean.
5. **`@PreDestroy`**: runs at shutdown, when the application context is closing.

(Between steps 2 and 3, the Aware callbacks and `postProcessBeforeInitialization()` also run.) **AOP proxies are created
in `postProcessAfterInitialization()`**, so the object that gets injected may be a proxy wrapping your bean, which
matters for `@Transactional`.

---

## Question 7.

`@EnableAutoConfiguration` (part of `@SpringBootApplication`) makes Spring Boot read the **candidate list** from the file
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in every jar on the classpath. For each
candidate (e.g. `JacksonAutoConfiguration`), it evaluates the `@Conditional...` annotations: if all conditions match,
its beans are registered, and if any condition fails, the whole class is skipped.

- **`@ConditionalOnClass`**: apply the configuration only when a given class is on the classpath (e.g. only if Jackson's
  `ObjectMapper` is present).
- **`@ConditionalOnMissingBean`**: apply it only when **you have not defined a bean of that type yourself**.

Boot's `objectMapper()` bean is declared with `@ConditionalOnMissingBean`, so when you define your own `ObjectMapper`
bean, that condition fails and Boot **backs off**, using your bean instead.

---

## Question 8.

- **`@Value`** injects a **single property** into a field, e.g. `@Value("${payment.base-url}")`, with an optional
  default (`${payment.retry.max-attempts:3}`). It is fine for one or two values, supports SpEL expressions, but has
  limited relaxed binding, no validation, and does not group related settings.
- **`@ConfigurationProperties(prefix = "payment")`** (recommended) binds a **whole group of related settings** into one
  type-safe object (e.g. `PaymentProperties`), with full relaxed binding (`base-url` = `baseUrl` = `BASEURL`), validation
  with `@Validated`, IDE auto-completion, and it is injected like any other bean.

Property source precedence, from highest to lowest:

1. Command-line arguments (`--server.port=9090`)
2. Java system properties (`-Dserver.port=9090`)
3. OS environment variables (`SERVER_PORT=9090`)
4. Profile-specific files (`application-prod.yml`)
5. `application.yml`

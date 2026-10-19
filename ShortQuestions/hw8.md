### Question 1.

When a class creates its dependencies with `new`, it is tightly coupled to concrete implementations such as the OrderService coupled with implementations of OrderRepository, PaymentClient and EmailSender. That makes it hard to unit test because you can't substitute a fake, hard to change because swapping MySQL for PostgresSQL means editing every class, and it scatters configuration and leave lifecycle unmanaged. IoC moves object creation and wiring out of the class into the Spring container, and dependency injection is how to implement IoC: the container passes dependencies in, usually via the constructor. The class depends only on interfaces, and configuration decides the implementation.

### Question 2.

`@SpringBootApplication` combines `@SpringBootConfiguration`, which marks a configuration class; `@EnableAutoConfiguration`, which enables classpath-based auto-configuration; and `@ComponentScan`, which scans for stereotype-annotated classes. Component scanning starts at the main class's package and only goes downward. A `@Service` in a sibling package is never scanned, so no bean exists and startup fails with "Required a bean of type ... that could not be found". The fix is to put the main class in the root package.

### Question 3.

Constructor injection passes dependencies through the constructor, setter injection through setter methods, and field injection directly into annotated fields. Constructor injection is recommended for required dependencies because fields can be `final`, dependencies are explicit, the class is unit-testable without Spring, and circular dependencies fail fast at startup. Setter injection suits optional dependencies. Field injection should be avoided because it hides dependencies, can't be `final`, and needs reflection to test.

### Question 4.

With two `NotificationSender` beans, Spring can't pick one and throws `NoUniqueBeanDefinitionException` at startup. There are 3 fixes: mark one bean `@Primary` as the default; use `@Qualifier("smsSender")` at the injection point to choose explicitly; or inject a `Map<String, NotificationSender>` or `List<NotificationSender>` to get all implementations and select at runtime.

### Question 5.

Spring's singleton scope is one instance per container per bean definition, enforced by the container, and the class is still a normal, mockable class. A singleton bean is one instance shared by every request thread, so any mutable field is shared state and creates race conditions. In `ReportService`, the shared `List` lets concurrent requests clear and mix each other's lines. That is why singleton beans must be stateless: fields should hold only dependencies and immutable configuration, per-request data should live in local variables, and any genuinely shared mutable state should use thread-safe types like `AtomicLong` or `ConcurrentHashMap`. The GoF Singleton is one instance per class loader, enforced by the class with a private constructor, and it is global state that is hard to test.

### Question 6.

The order is: constructor, then setter/field injection, then `@PostConstruct`, then `BeanPostProcessor.postProcessAfterInitialization()`, then the bean is in use, and finally `@PreDestroy` at shutdown. The constructor instantiates the bean, injection wires its dependencies, `@PostConstruct` runs intialization logic, and the post-processor's after-initialization hook is where AOP proxies are created. `@PreDestroy` handles cleanup when the context closes. So the injected object may be a proxy rather than the raw bean.

### Question 7.

Both reads candidate auto-configuration classes from `META-INF/spring/...AutoConfiguration.imports` in every jar on the classpath, then applies each one only if all its `@Conditional` annotations match. `@ConditionalOnClass` applies the configuration only if a given class is on the classpath, and `@ConditionalOnMissingBean` only if you haven't defined that bean yourself. If you define your own `ObjectMapper`, the `@ConditionalOnMissingBean` check on Boot's default fails, so Boot backs off and uses yours. Note this also drops Boot's Jackson customization, wo properties are preferred for tweaking it.

### Question 8.

`@Value` injects individual values and supports SpEL, but it isn't grouped, validated or well supported by IDEs. `@ConfigurationProperties` binds a group of related properties into a type-safe, validatable object with relaxed binding and auto-completion, so it's the recommended approach. Precedence from highest to lowest is: command-line arguments, Java system properties, OS environment variables, profile-specific files such as `application-prod.yml`, and finally `application.yml`.

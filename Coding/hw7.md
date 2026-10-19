# Q1.
This way is tightly coupled,and it's hard to test,a unit test cannot replace the client with a fake, and it's hard to change, and keys hosts and posts are buried inside classes, hard to change.
Normally a class controls the creation of what it needs, Ioc flips that control, the class no longer create it dependencies, something outside it, the caller, a configuraation class or a framework container, decides which
implementations to use and supplies them.
Dependency injection is the most common way to implement Ioc, dependencies are passed in.

# Q2.
`@springbootconfiguration` marks the class as a source of bean definitions.
`@enableautoconfiguration` tells springboot to configure beans automatically based on what's on the classpath and in properties.
`@componentscan` scans the package of the annotated class and all its sub package for classes marked with component annotation.

# Q3.
Constructor injection is recommended because the object is always valid, we cannot obtain a service without a repository. with setter or field injection, an object can exist when null dependencies, and the failure shows up later as NPE.
Dependencies can be final, so they cannt be reassigned accidentally, and the object is safe to share across threads.
Dependencies are explicit, the constructor signature is documentation. and easy to test.
Circular dependencies fail faster,

# Q4.
Failing at startup, springboot doesnt know should use which one, then throw a NoUniqueBeanDefinitionException,  Use @primary to mark a compenent as a default one, when springboot doesnt know which one should be used will use this default one.
Use @qualifier to declare a explicit component. Or inject all of them.

# Q5.
In spring, a singleton shared by all request threads, so if we use a singleton to store data there will be a thread safe problem and also the data will be modified by different users.
Springboot singleton is one instance per container, and Gof singleton is one instance per JVM.
Springboot singleton forced by springboot while Gof forced by class itself.
Springboot singleton cannot by created by new while Gof cannt because the constructor usually declared as private.
Springboot singleton is easy to test while GOf is hard, because it's global state.

# Q6.
The order
1. Constructor
2. Setter / field injection
3. @PostConstruct
4. BeanPostProcessor.postProcessAfterInitialization()
5. bean is in use
6. @PreDestroy
In step4, a BeanPostProcessor checks whether the bean needs advice or pointcut, if so it wraps the bean in a JDK dynamic proxy or CGLib proxy and returns the proxy instead of the original.

# Q7.
Springboot looks at what is on the classpath and which beans already defined.
`@ConditionalOnClass` makes the whole class skipped while the condition is not found, with `@ConditionalOnMissingBean` , springboot checks the bean factory if there is already a same type bean,is yes the method is skipped,if not the default is created,This check only looks at bean definitions registered so far. That’s why ordering matters.
A hand-built ObjectMapper loses Boot's own Jackson customizations

# Q8.
They both read values from config files, `@Value` is one value at a time while `ConfigurationProperties` is a whole group at once.
`@value` must match name closely while `@ConfigurationProperties` is more flexible.
`@value` can't work with `@Validated` while `@ConfigurationProperties` can.
`@ConfigurationProperties` enables IDE autocomplete and error checking in application.yml through generated configuration metadata, whereas `@Value` keys are plain strings that tools cannot discover.

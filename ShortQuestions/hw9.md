# java-spring-filter-interceptor-homework

1. Explain the difference between a Servlet `Filter` and a Spring MVC `HandlerInterceptor`: which specification or framework defines each one, where each runs relative to `DispatcherServlet`, and what information each one has access to.

A Servlet Filter runs before and after the `DispatcherServlet`. It can access HTTP requests and responses, but does not know which controller method will handle the request.

A `HandlerInterceptor` is part of Spring MVC and runs inside the `DispatcherServlet` and it can access the specific controller method and its annotations through HandlerMethod.


2. In a filter's `doFilter` method, explain what runs before and after `chain.doFilter()`, what happens when a filter does not call `chain.doFilter()`, and which two design patterns the filter chain is based on.

When the request enters the filter, code runs before `chain.doFilter()` and when the response comes back, code runs after it. If `chain.doFilter()` is not called, the request stops and cannot reaches the controller.
The filter chain uses Chain of Responsibility, where each filter decides whether to pass the request to the next filter, and Decorator, where filters can wrap request or response objects to add functionality.


3. Describe two ways to register a filter in Spring Boot, how the order of filters is controlled, where Spring Security's filter chain sits by default, and how the same filter can accidentally end up registered twice.
   
To register filter in Spring Boot, we can use `@Component` or `FilterRegistrationBean`. Filter order can be controlled using `@Order` or `setOrder()`, and lower order values run first.

Spring Security's filter chain runs at order -100 by default. A filter may be registered twice if it is annotated with `@Component` and also registered using `FilterRegistrationBean`.


4. Describe when `preHandle`, `postHandle` and `afterCompletion` are called, including what happens when the controller throws an exception, and explain why `postHandle` cannot change the body of a `@RestController` response.

`preHandle()` runs before controller and can stop the request by returning false. `postHandle()` runs after controller finishes successfully but before view rendering. `afterCompletion()` runs after request processing is complete, including when controller throws an exception, as long as `preHandle()` returned true.

If the controller throws an exception, `postHandle()` would be skipped, and `afterCompletion()` still runs. `postHandle()` cannot modify `@RestController` response because the response has already been written before `postHandle()` executes.


5. Interceptor A is registered with order 1 and interceptor B with order 2. List the order in which all of their methods run for a successful request. Then list which methods run when `B.preHandle()` returns false.
   
   1. A.preHandle()
   2. B.preHandle()
   3. Controller method
   4. B.postHandle()
   5. A.postHandle()
   6. B.afterCompletion()
   7. A.afterCompletion()

`A.preHandle()` runs and returns true, and `B.preHandle()` runs and returns false. Then `A.afterCompletion()` runs.


6. Explain why an exception thrown inside a filter is not handled by `@RestControllerAdvice` while an exception thrown in preHandle is, and describe two ways to return a consistent JSON error response from a filter.

Filters run outside the `DispatcherServlet`, so exceptions in filters are not handled by `@RestControllerAdvice`. Exceptions thrown in preHandle happen inside the `DispatcherServlet`, so MVC's exception resolvers can handle them.

To return a consistent JSON error response from a filter:
   1. catch the exception and write a JSON response directly using `ObjectMapper`
   2. delegate the exception to `HandlerExceptionResolver`, which reuses the existing `@RestControllerAdvice` exception handlers.


7. Explain the difference between the pattern `/api/*` used in a servlet filter registration and the same pattern used in an interceptor's `addPathPatterns()`, and explain why an authentication interceptor should exclude `/error`.
   
In a servlet filter, `/api/*` matches `/api` and all paths under it, including nested paths. 
In an interceptor's `addPathPatterns()`, `/api/*` matches only one path segment. To match nested paths, we should use `/api/**`.

An authentication interceptor should exclude `/error` because Spring Boot may forward failed requests to this endpoint. 
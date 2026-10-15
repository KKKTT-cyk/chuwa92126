# Homework 8 - Spring Filter and Interceptor

## 1. Servlet Filter vs. Spring MVC HandlerInterceptor

A Servlet Filter is defined by the Servlet specification. It runs around the servlet, so it can process a request before DispatcherServlet and a response after downstream processing returns. It can access the request and response, including the URL, headers, and parameters, but normally does not know the selected Controller method before dispatch.

HandlerInterceptor is defined by Spring MVC. It runs inside DispatcherServlet's processing flow, after a handler has been selected. It receives the request, response, and selected handler. For an annotated Controller, the handler is usually a HandlerMethod, so the interceptor can inspect the method and its annotations.

For example, preHandle() can check for a @LogAction annotation and run logging code. The annotation itself does not perform the logging. Returning true allows processing to continue; returning false stops the handler execution chain. Without @LogAction, the Controller can still run if the interceptor returns true.

## 2. Filter Execution and Design Patterns

Code before `chain.doFilter()` runs before downstream processing. The call continues to the next matching filter, or to the servlet if no filters remain. Code after it runs when downstream processing returns normally. Use `finally` for cleanup that must also run if an exception occurs.

For filters A then B, the normal order is:

```text
A before → B before → Controller → B after → A after
```

The Controller runs once. Returning from B resumes A after its existing chain call; it does not repeat the call. URL patterns decide which filters participate. A nonmatching filter is skipped, not a reason to reject the request.

If a filter does not call `chain.doFilter()`, downstream filters and the servlet are not invoked. It can write an error response directly. If B stops the chain and returns normally, A's after-code still runs.

- **Chain of Responsibility:** Each filter can process the request and either forward it or stop the chain.
- **Decorator:** Filters can wrap downstream processing with before/after behavior. Request and response wrappers are direct examples of adding behavior around an existing object.

## 3. Filter Registration and Ordering

Two ways to register a filter in Spring Boot are:

- Use `@Component` on the filter class. Boot automatically registers the bean with the embedded servlet container, normally mapped to `/*`. Use `@Order` on the filter class to control its order.
- Define a `FilterRegistrationBean` in a `@Bean` method. Use `setFilter()`, `addUrlPatterns()`, and `setOrder()` to specify the filter, matching URLs, and order.

Lower order values run first. Spring Security's servlet filter registration defaults to order `-100`, so it runs before filters ordered `1` or `2`. A filter ordered `-200` runs before it. This servlet registration order is separate from `@Order` used to select between multiple SecurityFilterChain beans.

Avoid accidentally registering two filter instances: using `@Component` and also `new FilterA()` in a registration bean can register two independent instances. Either create the filter only in the registration bean, or inject the existing component into it. Boot recognizes that same instance as explicitly registered.

## 4. Interceptor Lifecycle

For a normal synchronous request:

- `preHandle()` runs before the Controller. Returning `true` continues processing; returning `false` stops handler execution.
- `postHandle()` runs after successful handler execution, before view rendering in traditional MVC. It is skipped if handler execution throws an exception.
- `afterCompletion()` runs after request processing finishes, including view rendering, and is useful for cleanup even when an exception occurs. It runs only for interceptors whose `preHandle()` successfully returned `true`. Its exception argument does not include exceptions already handled by an exception resolver.

For `@RestController`, the return value is converted and written to the response before `postHandle()` runs, so `postHandle()` is too late to reliably change the response body. Use `ResponseBodyAdvice` to modify the body before the message converter writes it.

## 5. Execution Order of Two Interceptors

For a normal synchronous request, A has order 1 and B has order 2. If both `preHandle()` methods return `true` and the Controller completes normally:

```text
A.preHandle → B.preHandle → Controller
→ B.postHandle → A.postHandle
→ B.afterCompletion → A.afterCompletion
```

If A returns `true` but B returns `false`:

```text
A.preHandle → B.preHandle → A.afterCompletion
```

The Controller and both `postHandle()` methods are skipped. B's `afterCompletion()` does not run because its `preHandle()` did not return `true`. If A returns `false`, only A's `preHandle()` runs.

Interceptors do not each invoke the Controller. DispatcherServlet coordinates handler selection and invocation through a HandlerAdapter; all applicable interceptors must allow processing to continue before the Controller runs.

## 6. Exception Handling in Filters and Interceptors

A Filter runs outside DispatcherServlet's exception-handling scope, so exceptions thrown by the Filter are not automatically handled by `@RestControllerAdvice`. In contrast, `preHandle()` runs within DispatcherServlet's processing flow. DispatcherServlet catches its exceptions and delegates to `HandlerExceptionResolver`, which can invoke a matching `@ExceptionHandler` in an applicable advice class.

A `try-catch` alone does not invoke advice; the exception must reach the MVC exception-resolution mechanism.

Two ways to return consistent JSON errors from a Filter are:

1. Handle the error directly: set the HTTP status, JSON content type, and encoding, then write an error body using the application's shared error format.
2. Catch the exception and delegate to the injected MVC `HandlerExceptionResolver` using `resolveException(request, response, null, ex)`. This can reuse matching global advice handlers that do not require a selected Controller. If the resolver returns `null`, provide fallback handling or rethrow the exception.

After handling the error, stop processing instead of continuing the chain. Write an error response only while the response is not already committed.

## 7. URL Patterns and Excluding /error

In Servlet filter registration, `/api/*` is a path-prefix mapping and matches nested paths such as `/api/orders/123`. In an interceptor's `addPathPatterns()`, `/api/*` matches only one path segment after `/api/`, such as `/api/orders`. Use `/api/**` to match multiple levels. `api` is just a literal path name, and `addPathPatterns()` is a method, not an annotation.

| Request path | Filter `/api/*` | Interceptor `/api/*` | Interceptor `/api/**` |
|---|---|---|---|
| `/api/orders` | Yes | Yes | Yes |
| `/api/orders/123` | Yes | No | Yes |

Spring Boot uses `/error` for default error handling. An authentication interceptor mapped broadly should exclude `/error` so it does not block error responses, replace the original error with an authentication error, or cause repeated error handling. This does not bypass authentication for the original protected endpoint.

```java
registry.addInterceptor(authInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/error");
```

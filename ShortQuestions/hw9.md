### Question 1.

A Servlet Filter is defined by the Jakarta Servlet specification and runs at the servlet container level, before and after `DispatcherServlet`. It has access to raw HTTP requests and responses, including headers, URLs, and bodies through appropriate APIs or wrappers, but does not know which controller method will execute.

A `HandlerInterceptor` is defined by Spring MVC and runs inside `DispatcherServlet`, around the selected handler. It has access to the HTTP request and response, as well as the `HandlerMethod`, which provides information about the controller method and its annotations.

### Question 2.

Code before `chain.doFilter()` executes during incoming request processing. Code after it executes when downstream processing returns, on the way back out. If a filter does not call `chain.doFilter()`, the request stops at that filter and never reaches subsequent filters or the controller. The filter chain uses two design patterns: Chain of Responsibility, where each filter decides whether to continue processing, and Decorator, where filters can wrap request or response objects to add behavior.

### Question 3.

Two common approaches are annotating a filter with `@Component`, which automatically registers it, and using `FilterRegistrationBean`, which provides explicit URL mapping and order configuration. Filter order is controlled by `@Order` or `setOrder()`, with lower numbers running earlier. Spring Security's filter chain has a default order of `-100`, before custom filters ordered `1`. A filter can accidentally be registered twice if it it annotated with `@Component` and another instance is registered through `FilterRegistrationBean`.

### Question 4.

`preHandle()` runs before the controller, and returning `false` stops further processing.

`postHandle()` runs after the controller returns normally but before view rendering. It is skipped if the controller throws an exception.

`afterCompletion()` runs after request processing finishes, incluing when an exception occurs, provided that interceptor's `preHandle()` returned `true`.

`postHandle()` cannot change a `@RestController` response body because the body has already been written by the `HandlerAdapter` before `postHandle()` runs. Use `ResponseBodyAdvice` to modify the response body instead.

### Question 5.

For a successful request:

```
1. A.preHandle()
2. B.preHandle()
3. Controller executes
4. B.postHandle()
5. A.postHandle()
6. View rendering (if applicable)
7. B.afterCompletion()
8. A.afterCompletion()
```

If `B.preHandle() returns `false`after A returned`true`:

```
1. A.preHandle()
2. B.preHandle()
3. A.afterCompletion()
```

The controller, both `postHandle()` methods and `B.afterCompletion()` are skipped.

### Question 6.

Filters run outside `DispatcherServlet`, so exceptions thrown inside them are not automatically handled by Spring MVC's `@RestControllerAdvice`.

Interceptors run inside `DispatcherServlet`, allowing exceptions from `preHandle()` to be processed by Spring MVS's `HandlerExceptionResolver`.

Two solutions for consistent filter error responses are:

1. Catch the exception in the filter and directly write a JSON response using `ObjectMapper`.

2. Inject `HandlerExceptionResolver` and call `resolveException()` to delegate to the existing Spring MVC exception handlers.

### Question 7.

In servlet filter registration, `/api/*` matches `/api` and all nested paths, including `/api/orders/123`.

In an interceptor's `addPathPatterns()`, `/api/*` matches only one path segment, such as `/api/orders`. To match nested paths, use `/api/**`.

An authentication interceptor should exclude `/error` when using broad URL matching because Spring Boot may dispatch failed requests to `/error`. If the interceptor blocks that dispatch, it can replace the original error response with a misleading `401 Unauthorized`.

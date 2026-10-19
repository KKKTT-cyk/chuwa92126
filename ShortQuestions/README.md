

## 1.Explain the difference between a Servlet Filter and a Spring MVC HandlerInterceptor: which specification or framework defines each one, where each runs relative to DispatcherServlet, and what information each one has access to.

Answer:A Servlet Filter is defined by the Jakarta Servlet specification (`jakarta.servlet.Filter`) and runs in the servlet container's filter chain, generally before a request reaches Spring MVC's `DispatcherServlet` and again as the response returns. It has access to `ServletRequest`, `ServletResponse`, and `FilterChain` (often cast to HTTP request/response types), so it can inspect or wrap raw requests and responses without knowing which Spring MVC handler will execute. A `HandlerInterceptor` is defined by Spring MVC (`org.springframework.web.servlet.HandlerInterceptor`) and runs inside`DispatcherServlet` processing, after a handler has been mapped. It receives `HttpServletRequest`, `HttpServletResponse`, and the selected handler (often a `HandlerMethod`), so it can inspect controller and method metadata; `postHandle` also receives a `ModelAndView` when applicable. Filters are useful for cross-cutting HTTP concerns, while interceptors are useful for MVC handler-specific concerns.

## 2. In a filter's doFilter method, explain what runs before and after chain.doFilter(), what happens when a filter does not call chain.doFilter(), and which two design patterns the filter chain is based on.

Answer: Code before `chain.doFilter(request, response)` runs on the inbound request; the call delegates to the next filter or ultimately the target servlet. Code after it runs on the outbound path as the nested calls unwind (assuming the downstream call returns normally; use `finally` for cleanup even if an exception occurs). If a filter does not call `chain.doFilter()`, processing stops at that point: later filters and the servlet/controller are not invoked for that dispatch, and the filter can write its own response, such as a 401. The chain combines chain of responsibility (each filter may handle or forward a request) and decorator (filters wrap additional behavior around downstream processing).

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    long start = System.nanoTime(); // Before downstream processing
    try {
        chain.doFilter(request, response);
    } finally {
        System.out.println("Elapsed ns: " + (System.nanoTime() - start));
    }
}
```

## 3. Describe two ways to register a filter in Spring Boot, how the order of filters is controlled, where Spring Security's filter chain sits by default, and how the same filter can accidentally end up registered twice.

Answer: Two common registration methods are (1) make a `Filter` a Spring bean, such as with `@Component`, allowing Spring Boot to auto-register it with the servlet container, and (2) define a `FilterRegistrationBean` to configure URL patterns, dispatcher types, and order explicitly. You can set order using `FilterRegistrationBean#setOrder(...)` or `@Order` on an auto-registered filter; lower order values run earlier on the incoming request. Spring Security normally installs a `DelegatingFilterProxy` named `springSecurityFilterChain`, which delegates to Security's internal `FilterChainProxy`. In Spring Boot, the security servlet filter registration is normally ordered at `SecurityProperties.DEFAULT_FILTER_ORDER` (commonly `-100`), though configuration and versions can change ordering. A filter may execute twice if it is auto-registered as a servlet filter bean and separately added to Spring Security via `http.addFilterBefore/After/At(...)`, or if duplicate servlet registrations are created. For a Security-only filter, disable servlet auto-registration using a `FilterRegistrationBean` with `setEnabled(false)`.

## 4.Describe when preHandle, postHandle and afterCompletion are called, including what happens when the controller throws an exception, and explain why postHandle cannot change the body of a @RestController response.

Answer: `preHandle` runs before the controller and can prevent handler execution by returning `false`. `postHandle` runs after the controller completes successfully but before MVC finishes rendering a view. `afterCompletion` runs after request completion for interceptors whose `preHandle` returned `true`, including cleanup after errors. If a controller throws an exception, `postHandle` is ordinarily skipped; Spring MVC attempts exception resolution (for example, via `@ExceptionHandler` / `@RestControllerAdvice`), and `afterCompletion` still runs. `postHandle` cannot reliably modify a `@RestController` response body because `@ResponseBody` return values are serialized and written by an `HttpMessageConverter` during handler execution, before `postHandle` runs. Use `ResponseBodyAdvice` to modify the response body instead.

## 5.Interceptor A is registered with order 1 and interceptor B with order 2. List the order in which all of their methods run for a successful request. Then list which methods run when B.preHandle() returns false.

Answer: For a successful request, the order is:

```text
A.preHandle()
B.preHandle()
Controller executes
B.postHandle()
A.postHandle()
B.afterCompletion()
A.afterCompletion()
```

If `B.preHandle()` returns `false`, the sequence is:

```text
A.preHandle()
B.preHandle()  // returns false
A.afterCompletion()
```

The controller does not run, neither `postHandle` runs, and `B.afterCompletion()` is not called because B did not successfully complete `preHandle`. B should prepare the response itself if it blocks the request.

## 6.Explain why an exception thrown inside a filter is not handled by @RestControllerAdvice while an exception thrown in preHandle is, and describe two ways to return a consistent JSON error response from a filter.

Answer: `@RestControllerAdvice` is integrated with Spring MVC's `HandlerExceptionResolver` machinery inside `DispatcherServlet`. A filter executes outside that MVC exception-handling boundary, so an exception thrown by the filter before it delegates into `DispatcherServlet` is not automatically processed by controller advice. An exception thrown from an interceptor's `preHandle` happens inside the dispatcher's handler execution flow and can be handled by MVC exception resolvers (subject to a matching handler and exception handler).

Two ways to return a consistent JSON error from a filter are:

1. Write the response directly: set an appropriate status (for example, 401), `Content-Type: application/json`, and serialize a standard error DTO with Jackson's `ObjectMapper` to `response.getWriter()`. Avoid calling `chain.doFilter()` afterward.
2. Delegate to Spring MVC's `HandlerExceptionResolver`:** inject the resolver (often qualified as `handlerExceptionResolver`) and call `resolveException(request, response, null, exception)`. This can reuse MVC exception-handling conventions, but verify that the relevant advice supports exceptions with a `null` handler and that the resolver actually handles the exception; otherwise provide a fallback response.

## 7.Explain the difference between the pattern /api/* used in a servlet filter registration and the same pattern used in an interceptor's addPathPatterns(), and explain why an authentication interceptor should exclude /error.

Answer: A servlet filter registration uses servlet URL-pattern matching**: `/api/*` is a path-prefix mapping and matches `/api`, `/api/`, `/api/users`, and deeper paths such as `/api/users/42`. An interceptor's `addPathPatterns("/api/*")` uses spring MVC path-pattern matching, where `*` matches within a single path segment; it typically matches `/api/users` but not `/api/users/42`. For all nested API paths, use `/api/**` in an interceptor. An authentication interceptor should exclude `/error` because Spring Boot may dispatch failed requests to its error endpoint; intercepting or rejecting that dispatch can mask the original failure, replace its status/body, or create confusing repeated error handling. Configure exclusions as appropriate for your application's error dispatch and security policy.

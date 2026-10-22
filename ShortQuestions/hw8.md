# HW8 - Spring Filters and Interceptors

These answers use the Servlet stack with Spring Boot 3.x, Spring Framework 6.x, and `jakarta.servlet` APIs. The callback sequences assume synchronous request processing.

## 1. Servlet Filter vs. Spring MVC HandlerInterceptor

| Aspect | Servlet Filter | HandlerInterceptor |
| --- | --- | --- |
| Defined by | The Jakarta Servlet specification (`jakarta.servlet.Filter`); older versions use `javax.servlet`. | Spring MVC (`org.springframework.web.servlet.HandlerInterceptor`). |
| Position | Runs in the servlet container's filter chain, outside `DispatcherServlet`. Its before logic runs before the servlet; its after logic runs when the downstream chain returns. | Runs inside `DispatcherServlet`, after a handler is selected and around that handler's execution and MVC completion. |
| Information available | Servlet request/response, headers, parameters, cookies, session, attributes, and raw body streams. It does not normally have the selected Spring controller method before delegation. | HTTP request/response plus the selected `handler`, often a `HandlerMethod`, allowing inspection of controller type, method, and annotations. `postHandle` also receives a nullable `ModelAndView`. |
| Typical uses | Encoding, CORS, authentication, logging, and request/response wrapping. Can apply to non-MVC resources as well, according to its mappings. | Handler-specific checks, timing, and model preparation for matching MVC handlers. |

A filter can pass wrapped request/response objects down the chain. An interceptor does not have that replacement mechanism, and its handler argument is not the controller's bound method arguments or parsed request DTO.

References: [Servlet Filter API](https://jakarta.ee/specifications/platform/10/apidocs/jakarta/servlet/Filter.html), [HandlerInterceptor API](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/servlet/HandlerInterceptor.html).

## 2. What happens around chain.doFilter() and the design patterns

Code before `chain.doFilter(request, response)` performs preprocessing, such as checking credentials or setting encoding. Calling it delegates to the next filter, or to the target servlet when no filters remain. Code after it performs postprocessing when that downstream call returns normally.

For two filters, the normal order is:

```text
F1 before -> F2 before -> servlet -> F2 after -> F1 after
```

If downstream code throws an exception that escapes the call, ordinary statements after `chain.doFilter()` are skipped. Put required cleanup in a `finally` block. Also, after delegation the response may already be committed; changing its headers or body is not always possible without suitable buffering/wrapping.

If a filter does not call `chain.doFilter()`, it short-circuits processing: later filters and the target servlet are not invoked for that dispatch. It should complete the response itself, for example by returning a 401 JSON response. Earlier filters still resume as the call stack unwinds.

The two patterns are **Chain of Responsibility**, because each filter can delegate or stop processing, and **Decorator**, because nested filters add behavior around downstream processing. Request/response wrapper classes also use decoration to add behavior while preserving the servlet interfaces.

Reference: [Jakarta Servlet specification, sections 6.2.1-6.2.2](https://jakarta.ee/specifications/servlet/6.0/jakarta-servlet-spec-6.0.pdf).

## 3. Filter registration, ordering, Spring Security, and duplicate registration

Two common registration approaches in an embedded Spring Boot application are:

1. **Automatic registration of a Filter bean:** implement `Filter` and annotate the class with `@Component`, or expose the filter through an `@Bean` method. Boot registers it with the servlet container, normally for `/*`.
2. **Explicit registration:** declare an `@Bean` returning `FilterRegistrationBean<MyFilter>`. Supply the filter and configure its URL patterns, dispatcher types, and order, for example `registration.addUrlPatterns("/api/*")` and `registration.setOrder(1)`.

Another supported option is `@WebFilter` together with `@ServletComponentScan` for embedded-container scanning.

Lower order values run earlier on the incoming request and later on the return path. For automatically registered filter beans, use `@Order` on the filter class or implement `Ordered`; putting `@Order` only on its `@Bean` method does not set the servlet filter's order. For an explicit registration, use `FilterRegistrationBean.setOrder(...)`. An unordered registration normally has `Ordered.LOWEST_PRECEDENCE`.

Spring Security participates in the container chain through a `DelegatingFilterProxy` delegating to `springSecurityFilterChain` (`FilterChainProxy`). In Boot 3.x its default container order is **-100**, before ordinary filters ordered 0, 1, or left unordered, and before `DispatcherServlet`. It is not necessarily the first filter: a filter ordered -101 runs earlier. Container order is separate from ordering filters inside a `SecurityFilterChain` or choosing among multiple security chains.

Duplicate registration can happen when a `@Component` filter is automatically registered and an explicit registration creates a second instance with `new MyFilter()`. It can also happen when a filter bean is added to Spring Security while Boot separately registers it with the container. Choose one registration path; for a security-only filter bean, disable its container registration with `FilterRegistrationBean.setEnabled(false)`. An explicit registration wrapping the same bean instance normally makes Boot back off from automatic registration, so `@Component` plus a registration bean is not inherently duplication.

References: [Boot filter registration](https://docs.spring.io/spring-boot/3.5/how-to/webserver.html#howto.webserver.add-servlet-filter-listener), [Boot filter ordering](https://docs.spring.io/spring-boot/3.5/reference/web/servlet.html#web.servlet.embedded-container.servlets-filters-listeners.beans), [default security order](https://docs.spring.io/spring-boot/3.5/api/java/constant-values.html#org.springframework.boot.autoconfigure.security.SecurityProperties.DEFAULT_FILTER_ORDER), [Spring Security architecture and filter beans](https://docs.spring.io/spring-security/reference/servlet/architecture.html), [Boot registration deduplication source](https://github.com/spring-projects/spring-boot/blob/v3.5.7/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/servlet/ServletContextInitializerBeans.java).

## 4. Interceptor callbacks, exceptions, and REST response bodies

- **`preHandle`:** called after handler mapping and before the controller executes. Returning `true` continues to the next interceptor/controller. Returning `false` stops that execution chain; the interceptor is responsible for the response.
- **`postHandle`:** called after successful handler execution, before view rendering. It runs in reverse interceptor order and can modify the model for a view. It is skipped if the controller throws an exception out to `DispatcherServlet`, even if an exception resolver subsequently handles that exception.
- **`afterCompletion`:** called after MVC processing, including view rendering, finishes, for cleanup. It runs in reverse order, but only for interceptors whose `preHandle` completed and returned `true`. It also runs on controller failure. An unresolved exception is passed as `ex`; an exception successfully handled by an MVC exception resolver is not passed, so `ex` is normally `null` in that case.

For `@RestController` / `@ResponseBody`, the handler adapter uses an `HttpMessageConverter` to serialize and write the return value before `postHandle` runs. The response is already written and normally committed, and there is no response object in `ModelAndView` to edit. Use `ResponseBodyAdvice.beforeBodyWrite(...)` to transform the return body before serialization.

References: [Spring MVC interception](https://docs.spring.io/spring-framework/reference/6.2/web/webmvc/mvc-servlet/handlermapping-interceptor.html), [HandlerInterceptor callback contract](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/servlet/HandlerInterceptor.html).

## 5. Callback order for interceptors A and B

Assuming both interceptors match the request, A has order 1, B has order 2, and both return `true`:

```text
1. A.preHandle()
2. B.preHandle()
3. Controller executes
4. B.postHandle()
5. A.postHandle()
6. View rendering, if applicable
7. B.afterCompletion(..., null)
8. A.afterCompletion(..., null)
```

For a REST response, body serialization/writing occurs in step 3 rather than step 6.

If A returns `true` but B returns `false`:

```text
1. A.preHandle()
2. B.preHandle() returns false
3. A.afterCompletion(..., null)
```

The controller and both `postHandle` methods are skipped. `B.afterCompletion` is also skipped because B's `preHandle` did not return `true`. B must handle its own rejection response and any cleanup it needs.

Reference: [Spring HandlerExecutionChain implementation](https://github.com/spring-projects/spring-framework/blob/v6.2.12/spring-webmvc/src/main/java/org/springframework/web/servlet/HandlerExecutionChain.java).

## 6. Exceptions in filters vs. preHandle, and consistent JSON errors

`@RestControllerAdvice` participates in Spring MVC exception resolution. An exception thrown by a filter's own logic occurs outside `DispatcherServlet`'s dispatch handling, so MVC advice does not automatically catch it. A servlet container error dispatch to `/error` is a separate error-handling path.

`preHandle` runs inside `DispatcherServlet`'s dispatch try/catch. If it throws, the servlet invokes its `HandlerExceptionResolver` chain. A matching `@ExceptionHandler` in applicable `@RestControllerAdvice` can therefore handle it. Advice still needs a matching exception handler; it does not handle every exception just because it exists.

Two ways to return the same JSON error format from a filter are:

1. **Write the response directly.** Catch the filter's expected exception, choose the appropriate HTTP status, set `application/json` and UTF-8, and serialize a shared error DTO with `ObjectMapper`. Reuse the DTO/error-writing helper in MVC advice to keep the schema consistent, for example `{"code":"UNAUTHORIZED","message":"Authentication required"}` with status 401. Return without continuing the chain. Use status 500 with a generic message for unexpected server failures; do not expose internal exception details. `sendError(...)` alone does not guarantee this JSON format.
2. **Delegate explicitly to MVC's resolver.** Inject `HandlerExceptionResolver` qualified with `"handlerExceptionResolver"`, catch the filter exception, and call `resolver.resolveException(request, response, null, ex)`. A global, matching `@RestControllerAdvice` handler can write the shared JSON response. The handler is `null` because no MVC controller may have been selected; advice restricted to particular controller types/packages may not apply. A non-null, empty `ModelAndView` can mean the exception was handled by writing the body. A null result means it was not handled, so use a JSON fallback or rethrow rather than silently returning an empty success response.

Both approaches must act before the response is committed. Once it is committed, the filter cannot reliably replace its status/body. For Spring Security authentication/authorization failures, its configured `AuthenticationEntryPoint` and `AccessDeniedHandler` can use the same shared JSON writer.

References: [DispatcherServlet dispatch and exception handling](https://github.com/spring-projects/spring-framework/blob/v6.2.12/spring-webmvc/src/main/java/org/springframework/web/servlet/DispatcherServlet.java), [HandlerExceptionResolver API](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/servlet/HandlerExceptionResolver.html), [Spring Security exception handling](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-exceptiontranslationfilter).

## 7. /api/* matching and excluding /error

A servlet filter registration uses **Servlet URL mapping rules**. `/api/*` is a path-prefix mapping, so it covers `/api` and paths below it, including multiple nested segments. Servlet mappings do not use Spring's `/**` syntax.

An interceptor's `addPathPatterns("/api/*")` uses **Spring MVC path matching**. With `PathPattern` (or the corresponding Ant-style pattern), `*` matches within one path segment; it does not cross `/`. To include all nested API paths, use `/api/**`.

Assuming the application context root and the default `DispatcherServlet` mapping:

| Request path | Filter `/api/*` | Interceptor `/api/*` | Interceptor `/api/**` |
| --- | --- | --- | --- |
| `/api/users` | Matches | Matches | Matches |
| `/api/users/42` | Matches | Does not match | Matches |
| `/apix/users` | Does not match | Does not match | Does not match |

Spring Boot routes unhandled errors to `/error`. A broadly mapped authentication interceptor, such as one matching `/**`, can run again for that MVC error handler. If it rejects the error request, it can replace the original status/body with an authentication error or trigger repeated error handling/redirects. Exclude `/error` so the error handler can render the original failure, for example:

```java
registry.addInterceptor(authInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/login", "/error");
```

This is an illustrative fragment inside `WebMvcConfigurer.addInterceptors`. If the interceptor only includes `/api/**`, `/error` is already outside that scope. For production authentication, Spring recommends Spring Security or a suitable servlet filter, since interceptor and controller path matching can differ.

References: [Servlet specification, sections 6.2.4 and 12.2](https://jakarta.ee/specifications/servlet/6.0/jakarta-servlet-spec-6.0.pdf), [Spring PathPattern syntax](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/util/pattern/PathPattern.html), [Boot error handling](https://docs.spring.io/spring-boot/3.5/reference/web/servlet.html#web.servlet.spring-mvc.error-handling), [interceptors and security](https://docs.spring.io/spring-framework/reference/6.2/web/webmvc/mvc-servlet/handlermapping-interceptor.html).

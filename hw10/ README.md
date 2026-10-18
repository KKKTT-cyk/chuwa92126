## Q1. Filter vs. HandlerInterceptor

- **Defined by:** a `Filter` is part of the Jakarta Servlet specification and is run by the servlet container (Tomcat). A `HandlerInterceptor` is part of Spring MVC.
- **Where it runs:** a filter runs **outside** `DispatcherServlet`, before the request reaches it and after it returns. An interceptor runs **inside** `DispatcherServlet`, around the chosen controller method.
- **What it can access:** a filter sees every request the container receives and the raw request/response objects, which it can wrap or replace, but it doesn't know which controller method will handle the request. An interceptor only sees requests mapped to a handler, but it knows exactly which controller method runs (`HandlerMethod`) and can read its annotations.



## Q2. chain.doFilter() and the Filter Chain

- **Before `chain.doFilter()`:** runs on the way in, before the next filters and the controller.
- **After `chain.doFilter()`:** runs on the way out, after the controller has finished. With several filters, the first one in is the last one out.
- **If `chain.doFilter()` is not called:** the request stops at that filter, and later filters and the controller never run. This is used on purpose to block requests (e.g. return 403), but forgetting it in one branch causes an empty 200 OK with no error logged.
- **Design patterns:** 
**Chain of Responsibility**, because each filter does its part and decides whether to pass the request on; and **Decorator**, because a filter can wrap the request or response (e.g. `ContentCachingRequestWrapper`) and pass the wrapper down the chain.


## Q3. Registering and Ordering Filters

**Two ways to register a filter:**
- **`@Component`:** the filter becomes a bean and Spring Boot registers it automatically for all URLs. Order is set with `@Order`.
- **`FilterRegistrationBean`:** a `@Bean` that registers the filter with specific URL patterns (e.g. `/api/*`) and an order via `setOrder()`.

**Order:** a lower number runs earlier (further outside). `Ordered.HIGHEST_PRECEDENCE` runs first; filters without an order run last in no guaranteed order.

**Spring Security:** its filter chain has order **-100** by default, so it runs before your own filters (e.g. order 1, 10).

**Registered twice:** if the filter class has `@Component` and is also registered with a `FilterRegistrationBean` using `new ApiKeyFilter()`, there are two instances. The filter runs twice per request, and the URL pattern only limits one of them. Fix: use only one style, and remove `@Component` when using `FilterRegistrationBean`.



## Q4. preHandle, postHandle, and afterCompletion

- **`preHandle`:** called before the controller. Returning false stops the request, so the controller is never called.
- **`postHandle`:** called after the controller returns normally, before any view is rendered. It is **not** called if the controller throws an exception.
- **`afterCompletion`:** called after the request is complete, **even if an exception was thrown**, as long as this interceptor's preHandle returned true. Its ex parameter holds the exception, or null if `@RestControllerAdvice` already handled it. It is the right place for cleanup.

**Why `postHandle` can't change a `@RestController` body:** for `@ResponseBody` / `ResponseEntity` methods, the `HandlerAdapter` writes the JSON body and commits the response **before** `postHandle` runs. Changes made in `postHandle` therefore have no effect. Use `ResponseBodyAdvice` to modify the body, or `preHandle` / a filter to add headers.



## Q5. Interceptor Execution Order

**Successful request** (A has order 1, B has order 2):

1. `A.preHandle()`
2. `B.preHandle()`
3. Controller method
4. `B.postHandle()`
5. `A.postHandle()`
6. `B.afterCompletion()`
7. `A.afterCompletion()`

`preHandle` runs in registration order; `postHandle` and `afterCompletion` run in reverse order.

**When `B.preHandle()` returns `false`:**

1. `A.preHandle()` → returns `true`
2. `B.preHandle()` → returns `false`, the chain stops
3. `A.afterCompletion()`

The controller and all `postHandle` calls are skipped. `B.afterCompletion()` is not called because B's `preHandle` returned `false`, but `A.afterCompletion()` still runs, since `afterCompletion` is guaranteed for every interceptor whose `preHandle` returned `true`.



## Q6. Exceptions in Filters vs. Interceptors

`@RestControllerAdvice` is called by the `HandlerExceptionResolver` inside `DispatcherServlet`. An exception in `preHandle` happens inside `DispatcherServlet`, so it reaches the advice. A filter runs before `DispatcherServlet`, so its exceptions never reach the advice, and Spring Boot returns its default error body instead.

**Two ways to return consistent JSON from a filter:**
1. **Write the error in the filter:** catch the exception and write an `ApiError` JSON response with `ObjectMapper`. Simple, but the error format is duplicated.
2. **Delegate to `HandlerExceptionResolver`:** call `resolver.resolveException(request, response, null, ex)` so the existing `@RestControllerAdvice` builds the response. All error formats stay in one place.



## Q7. Path Patterns and Excluding /error

**`/api/*` in a filter vs. an interceptor:** the pattern decides which requests the filter or interceptor runs on, but the two use different matching rules.
- **Servlet filter (`addUrlPatterns`):** `/api/*` matches `/api` and everything below it, e.g. `/api/orders` and `/api/orders/42`.
- **Interceptor (`addPathPatterns`):** `/api/*` matches exactly one segment, e.g. `/api/orders` but not `/api/orders/42`. To match everything below `/api`, use `/api/**`.

**Why an auth interceptor should exclude `/error`:** when a request fails, Spring Boot forwards it to `/error`, which passes through `DispatcherServlet` and the interceptors again. If the auth interceptor rejects `/error`, the client gets a 401 that hides the real error (e.g. 404 or 500).
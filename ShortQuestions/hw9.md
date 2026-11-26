# HW9 – Spring Filter and Interceptor

## Question 1.

A **Servlet Filter** is defined by the **Jakarta Servlet specification** (not by Spring). The servlet container (Tomcat) runs
filters **before and after `DispatcherServlet`**, so a filter sees every request the container receives, even ones that
never reach Spring MVC. A **`HandlerInterceptor`** is defined by **Spring MVC** and runs **inside `DispatcherServlet`,
around the chosen controller method**, so it only sees requests that `DispatcherServlet` maps to a handler.

| Aspect | Filter | Interceptor |
|---|---|---|
| Defined by | Jakarta Servlet specification | Spring MVC |
| Runs | Before and after `DispatcherServlet` | Inside `DispatcherServlet`, around the handler |
| Knows the controller method | No (Spring has not chosen it yet) | Yes (`HandlerMethod` and its annotations) |
| Can replace the request/response objects | Yes, by passing wrappers to `chain.doFilter()` | No |

---

## Question 2.

Code **before** `chain.doFilter()` runs on the way **in** (before the next filter or the controller). Code **after**
`chain.doFilter()` runs on the way **out**, after the controller has finished, so the first filter in is the last filter
out, like layers of an onion.

If a filter **does not call `chain.doFilter()`**, the request stops there and **never reaches the controller** (for
example, a filter that blocks an IP with `sendError(403)` and returns). The classic bug is forgetting `chain.doFilter()`
in one branch of an `if`: every request taking that branch gets an empty `200 OK` and no error is logged.

The filter chain is based on the **Chain of Responsibility** pattern (each filter does its part and decides whether to pass
the request on) and the **Decorator** pattern (a filter can pass wrapped request/response objects down the chain).

---

## Question 3.

Two ways to register a filter:

1. **`@Component` on the filter class**: Boot registers it automatically for every URL; the order is set with `@Order`.
2. **`FilterRegistrationBean`**: gives URL patterns (`addUrlPatterns("/api/*")`) and the order (`setOrder(1)`); recommended
   when you need URL patterns.

(A third way, `@WebFilter` with `@ServletComponentScan`, is rarely used with Boot and has no reliable ordering.)

**Order:** a **lower number runs earlier** (outer). `@Order(Ordered.HIGHEST_PRECEDENCE)` runs first in and last out. A filter
with no order gets `LOWEST_PRECEDENCE`, so it runs last and in no guaranteed order among such filters.

**Spring Security's filter chain** is registered with order **-100** by default, so it sits before your own filters
(order 1, 10, ...).

**Registered twice:** if the filter class is annotated with `@Component` (Boot registers every `Filter` bean) **and** is also
registered with a `FilterRegistrationBean` using `new ApiKeyFilter()`, there are two registrations. The filter then runs
twice per request, and the URL pattern only limits one of the two copies. The fix is to choose one style.

---

## Question 4.

- **`preHandle`**: called **before the controller**. It runs even if the controller later throws (it runs first), and
  returning `false` stops the request.
- **`postHandle`**: called **after the controller returned normally**, before the view is rendered. It is **not called if the
  controller threw an exception**.
- **`afterCompletion`**: called **after the request completed, also when an exception was thrown** (`ex` is `null` if
  `@RestControllerAdvice` already handled it). It is only called if this interceptor's `preHandle()` returned `true`.

`postHandle` cannot change the body of a `@RestController` response because for `@ResponseBody`/`ResponseEntity` methods the
JSON body is **written and the response is committed inside the `HandlerAdapter`, before `postHandle` runs**. Changing the
body or headers there has no effect. To modify the body use `ResponseBodyAdvice`; to add a header use `preHandle` or a
filter.

---

## Question 5.

With A (order 1) and B (order 2), for a **successful request**:

1. `A.preHandle()`
2. `B.preHandle()`
3. Controller method
4. `B.postHandle()`
5. `A.postHandle()`
6. `B.afterCompletion()`
7. `A.afterCompletion()`

`preHandle` runs in registration order; `postHandle` and `afterCompletion` run in **reverse** order.

When **`B.preHandle()` returns `false`**: `A.preHandle()` runs (returns `true`), `B.preHandle()` runs and returns `false`, so
the chain stops. The controller is not called, **no `postHandle`** is called for anyone, `B.afterCompletion()` is **not**
called (B never "entered"), and **`A.afterCompletion()` is called** (A entered successfully and gets to clean up).
`afterCompletion` is guaranteed for every interceptor whose `preHandle` returned `true`.

---

## Question 6.

An exception thrown inside a **filter** happens **outside `DispatcherServlet`**, so `@RestControllerAdvice` (which works
through Spring MVC's exception resolvers) is not involved. The container forwards the request to `/error` and Spring Boot's
`BasicErrorController` writes its default error JSON. An exception thrown in **`preHandle`** is inside `DispatcherServlet`, so
`@RestControllerAdvice` handles it.

Two ways to return a consistent JSON error from a filter:

1. **Write the error response inside the filter:** catch the exception, set the status (e.g. 401) and the JSON content type,
   write an `ApiError` with `ObjectMapper`, and `return` to stop the chain. Keep `chain.doFilter()` outside the `try`, or
   downstream errors would be swallowed. It is simple, but the error format is duplicated.
2. **Delegate to Spring's `HandlerExceptionResolver`:** inject it with `@Qualifier("handlerExceptionResolver")` and call
   `resolver.resolveException(request, response, null, ex)`, which runs your existing `@ExceptionHandler` in the
   `@RestControllerAdvice`. One place defines all error formats, but the filter is tied to Spring MVC internals.

---

## Question 7.

In a **servlet filter**, `/api/*` matches `/api` **and everything below it**. In an **interceptor's `addPathPatterns()`**,
`/api/*` matches **exactly one segment**: `/api/orders`, but not `/api/orders/42`. To match everything below in an
interceptor, use `/api/**` (which is not a valid servlet pattern).

An authentication interceptor should **exclude `/error`** because when a request fails, Spring Boot forwards it to `/error`,
which goes through `DispatcherServlet` and therefore through the interceptors again. If the auth interceptor rejects
`/error`, it hides the real error behind a confusing 401.

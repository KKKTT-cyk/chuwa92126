## 1. Explain the difference between a Servlet Filter and a Spring MVC HandlerInterceptor: which specification or framework defines each one, where each runs relative to DispatcherServlet, and what information each one has access to.
Servlet filter runs in the servlet container's filter chain, which is outside of the dispatcherservlet, it can access request and response informations, like url, headers, but not controller.
 Spring MVC HandlerInterceptor runs inside dispatcherservlet and can access selected handler, including controller method, and annotations



## 2. In a filter's doFilter method, explain what runs before and after chain.doFilter(), what happens when a filter does not call chain.doFilter(), and which two design patterns the filter chain is based on.
Before chain.doFilter(), it do something request preprocessing like authentication or starting a timer.The call invokes the next filter and ultimately the servlet; code afterward runs when downstream processing returns, so filters exit in reverse order.

The two design patterns are Chain of Responsibility, where each filter can forward or stop processing, and Decorator, which adds behavior around downstream processing, including through request and response wrappers.





## 3. Describe two ways to register a filter in Spring Boot, how the order of filters is controlled, where Spring Security's filter chain sits by default, and how the same filter can accidentally end up registered twice.
First, we can use @Component, which automatically registers the Filter bean with the embedded servlet container. Second, we can use FilterRegistrationBean, which allows explicit configuration of URL patterns and other registration settings. Filter order is controlled using @Order on the Filter class or setOrder() on the registration bean.
The execute order is from lower number to higher number. If accidentally use the two way to register the same filter, it will registered twice.




## 4. Describe when preHandle, postHandle and afterCompletion are called, including what happens when the controller throws an exception, and explain why postHandle cannot change the body of a @RestController response.
preHandle() runs after Spring MVC selects a handler but before executing it; returning true allows processing to continue, PostHandle() runs after the handler completes successfully and before view rendering. AfterCompletion() runs after MVC request processing finishes.
For a @RestController, the return value is serialized and the response body written and committed inside the HandlerAdapter, before postHandle() executes. Consequently, postHandle() is too late to modify the JSON body.


## 5. Interceptor A is registered with order 1 and interceptor B with order 2. List the order in which all of their methods run for a successful request. Then list which methods run when B.preHandle() returns false.
prehandle a
prehandle b
controller
posthandle b
posthandle a
after completion b
after completion a




## 6. Explain why an exception thrown inside a filter is not handled by @RestControllerAdvice while an exception thrown in preHandle is, and describe two ways to return a consistent JSON error response from a filter.
Because filter is outside of dispatcherservlet, so it does not automatically reach Spring MVC’s exception resolvers or @RestControllerAdvice.
A filter can return consistent JSON errors in two ways: first, write the response directly by catching the relevant exception, or injecting the bean handlerExceptionResolver and calling resolveException(). This can reuse an applicable global Advice’s error format.



## 7. Explain the difference between the pattern /api/* used in a servlet filter registration and the same pattern used in an interceptor's addPathPatterns(), and explain why an authentication interceptor should exclude /error.
In a servlet filter registration, /api/* is a Servlet prefix mapping, matching paths beneath /api at any depth, including /api/users and /api/users/42. In an interceptor’s addPathPatterns(), * matches within a single path segment, so /api/* matches /api/users but not /api/users/42; use /api/** to include nested paths.
/error because Spring Boot uses that endpoint during error processing. Otherwise, the interceptor may reject the error dispatch and replace the original error response with a 401, 403, or login redirect. Excluding /error allows the error controller to report the original failure.

## Spring Filter & Interceptor Homework

### Dengtai Wang

#### Conceptual Questions

1. **Explain the difference between a Servlet Filter and a Spring MVC HandlerInterceptor: which specification or framework defines each one, where each runs relative to DispatcherServlet, and what information each one has access to.**

   A Servlet Filter is defined by the Servlet specification, while a HandlerInterceptor is part of the Spring MVC framework. A filter runs before and after DispatcherServlet, so it works at a lower level and mainly has access to HttpServletRequest and HttpServletResponse. An interceptor runs inside Spring MVC after the request reaches DispatcherServlet, so it can also access Spring-specific information such as the controller handler or HandlerMethod.

2. **In a filter's doFilter method, explain what runs before and after chain.doFilter(), what happens when a filter does not call chain.doFilter(), and which two design patterns the filter chain is based on.**

   Code before chain.doFilter() runs before the request continues to the next filter or servlet, while code after chain.doFilter() runs after downstream processing finishes. If a filter does not call chain.doFilter(), the request stops at that filter and does not continue to the controller. The filter chain mainly follows the Chain of Responsibility pattern, because each filter can decide whether to continue, and the Decorator pattern, because each filter adds behavior around the next step in the chain.

3. **Describe two ways to register a filter in Spring Boot, how the order of filters is controlled, where Spring Security's filter chain sits by default, and how the same filter can accidentally end up registered twice.**

   A filter can be registered by annotating it with @Component, or by creating a FilterRegistrationBean. Filter order can be controlled with @Order or setOrder() on the registration bean, where a smaller order value runs earlier. Spring Security also works in the servlet filter layer before requests reach Spring MVC controllers and interceptors. A filter can accidentally be registered twice if it is both marked as @Component and also manually registered with a FilterRegistrationBean.

4. **Describe when preHandle, postHandle and afterCompletion are called, including what happens when the controller throws an exception, and explain why postHandle cannot change the body of a @RestController response.**

   preHandle() runs before the controller method. If it returns true, the request continues, and if it returns false, the controller is not called. postHandle() runs after the controller finishes successfully, while afterCompletion() runs after the whole request is completed and can also run when an exception occurs. If the controller throws an exception, postHandle() is normally skipped, but afterCompletion() can still run. postHandle() usually cannot change the body of a @RestController response because the response body may already have been written by Spring's HttpMessageConverter processing.

5. **Interceptor A is registered with order 1 and interceptor B with order 2. List the order in which all of their methods run for a successful request. Then list which methods run when B.preHandle() returns false.**

   For a successful request, A.preHandle() runs first, followed by B.preHandle(), and then the controller executes. After the controller finishes, B.postHandle() runs before A.postHandle(), and finally B.afterCompletion() runs before A.afterCompletion(). If B.preHandle() returns false, the controller does not execute. In that case, A.preHandle() and B.preHandle() run, then only A.afterCompletion() runs because A successfully completed preHandle() before the chain was stopped.

6. **Explain why an exception thrown inside a filter is not handled by @RestControllerAdvice while an exception thrown in preHandle is, and describe two ways to return a consistent JSON error response from a filter.**

   An exception thrown inside a filter happens before the request reaches DispatcherServlet, so it is outside Spring MVC's exception handling flow. Because @RestControllerAdvice belongs to Spring MVC, it normally cannot handle exceptions thrown directly in filters. An exception thrown in preHandle() happens inside the DispatcherServlet processing flow, so Spring MVC can handle it through @RestControllerAdvice.

   One way to return a consistent JSON error from a filter is to catch the exception inside the filter and write the JSON response directly using HttpServletResponse. Another way is to delegate the exception to a HandlerExceptionResolver, so Spring's exception handling logic can generate the response.

7. **Explain the difference between the pattern /api/* used in a servlet filter registration and the same pattern used in an interceptor's addPathPatterns(), and explain why an authentication interceptor should exclude /error.**

   In servlet filter registration, /api/* uses Servlet URL-pattern matching. In a Spring MVC interceptor, path patterns use Spring path matching rules, where * normally matches one path level and ** matches multiple path levels. For example, /api/ can match /api/users, while /api/** can also match deeper paths such as /api/users/123.

   An authentication interceptor should exclude /error because Spring Boot may forward failed requests to the /error endpoint. If the authentication interceptor also intercepts /error, it can cause another authentication failure during error handling and may lead to repeated or incorrect error processing.

1. A Servlet Filter is defined by the Java Servlet specification. It runs outside and before/after the DispatcherServlet, so it can process all HTTP requests and responses, including static resources. It has access mainly to ServletRequest, ServletResponse, and FilterChain, but usually does not know which controller method will run.
A HandlerInterceptor is defined by Spring MVC. It runs inside the DispatcherServlet, before and after a controller method. It has access to HttpServletRequest, HttpServletResponse, the selected handler or HandlerMethod, and sometimes ModelAndView and exception information. Therefore, it can perform controller-specific checks, such as checking method annotations or permission
2. Code before chain.doFilter() processes the incoming request. Code after it processes the outgoing response.
   If the filter does not call chain.doFilter(), the chain stops, so later filters and the DispatcherServlet/controller are not executed. The filter should then create its own response, such as 401 Unauthorized.
   The filter chain mainly uses: Chain of Responsibility — each filter passes the request to the next filter； Decorator — filters can wrap and enhance the request or response.
3. Two common ways to register a filter in Spring Boot are:
Make the filter a Spring bean using @Component or @Bean; Spring Boot automatically registers it with the servlet container.
Create a FilterRegistrationBean, which allows explicit control over URL patterns, order, and whether the filter is enabled.
   Filter order is controlled with @Order, the Ordered interface, or FilterRegistrationBean.setOrder(). Lower numbers run earlier.
   Spring Security is registered in the servlet filter chain through DelegatingFilterProxy. By default, its filter chain runs early, at order -100, and delegates to Spring Security’s FilterChainProxy.
   A filter can run twice if it is registered as a normal servlet filter and also added to Spring Security’s filter chain. It can also be duplicated by making it a @Component while separately registering it with a FilterRegistrationBean.
4. - preHandle() is called before the controller method. If it returns true, processing continues; if it returns false, the controller is not called.
- postHandle() is called after the controller finishes successfully but before view rendering. If the controller throws an exception, postHandle() is skipped.
- afterCompletion() is called after the entire request is completed, including view rendering and exception handling. It is useful for cleanup and logging. If an exception remains unresolved, it is passed through the ex parameter.
  For a @RestController, the return value is serialized and written to the response body by an HttpMessageConverter while the controller is being handled. By the time postHandle() runs, the body has usually already been written, and there is normally no ModelAndView. Therefore, postHandle() cannot directly change the JSON body. Use ResponseBodyAdvice instead.
5. A.preHandle()
  B.preHandle()
  Controller
  B.postHandle()
  A.postHandle()
  B.afterCompletion()
  A.afterCompletion()
When B.preHandle() returns false
  A.preHandle() → true
  B.preHandle() → false
  A.afterCompletion()
  The controller does not run, neither postHandle() runs, and B.afterCompletion() does not run because B did not successfully pass its own preHandle(). Only A’s afterCompletion() runs.
6. A filter runs outside the DispatcherServlet, so its exceptions do not reach Spring MVC’s @RestControllerAdvice. An exception from preHandle() occurs inside the DispatcherServlet and is processed by HandlerExceptionResolver.
  A filter can return consistent JSON by either:
Catching the exception and writing JSON directly with ObjectMapper.
Delegating the exception to Spring’s HandlerExceptionResolver.
7. In a servlet filter registration, /api/* follows Servlet URL-pattern rules and matches everything under the /api prefix, including nested paths.
   In addPathPatterns(), Spring MVC path rules are used. /api/* normally matches only one path segment, while /api/** matches all nested paths.
   An authentication interceptor should exclude /error because Spring Boot dispatches failed requests there. Intercepting /error can block error handling or replace the original error with another authentication error.
1. A Servlet filter belongs to Jakarta Servlet specification, the servlet container runs filter before a request reach
any servlet including DispatcherServlet. At this level, servlet filter can assess HTTP request and response information
without knowing which controller method could handle the request. The spring MVC HandlerInterceptor belongs to spring 
MVC, and it runs inside the DispatcherServlet. At this level, because it runs after handler mapping, so it can also 
access the selected method and its annotation.
2. In a doFilter() method, before calling the chain.doFilter(), the filter converts ServletRequest to a
HTTPServletRequest and record the start time. The chain.doFilter() call passes the request to the remaining filter chain
and eventually to the controller. When the downstream processing finishes and control returns to this Filter, it 
calculates the elapsed time and writes the request information to the log. If the filter does not call the 
chain.doFilter(), the request stops and never reaches  the controller. The filter chain based on Chain of Responsibility
pattern and Decorator pattern.
3. Two ways to register filter in Spring Boot: @Component on the filter and FilterRegistrationBean 
With the annotation @Component to implement Filter, which makes the Spring Boot automatically register all URLs, its
order can be controlled by @Order. 
A FilterRegistrationBean can be declared as a @Bean, which provides more controls to URL patterns and order with 
@addUrlPatterns and @setOrder. Lower order value runs earlier when request enters and when requests returns. Spring 
Security's filter chain is registered in the filter chain with a default order of -100. A filter can accidentally run
twice if it is registered both as a servlet filter, for example, with @Component and FilterRegistrationBean and also 
added to SpringFilterChain. And @Component creates and register an instance while FilterRegistrationBean registers 
another instance with new ApiKeyFilter().
4. preHandle is called before the controller and when the controller throws an exception, it will run first; postHandle
is called after the controller and before view rendering and when the controller throws an exception, it will not run;
afterCompletion is called after everything is called, and when the controller throws an exception, it will run and if 
@RestControllerAdvice already handled the exception ex is null. 
Because for @ResponseBody/@ResponseEntity methods, the JSON body is written and the response committed inside the 
HandlerAdapter, before the postHandle runs, thus postHandle could not change the body of @ResponseController response.
5. For a successful request, the order is: A.preHandle(), B.preHandle(), controller, B.postHandle(), A.postHandle(), 
B.afterCompletion(), A.afterCompletion().
If B.preHandle returns false, the order is: A.preHandle(), B.preHandle(), A.afterCompletion(), the controller and other all 
postHandle() methods are skipped, and B.afterCompletion is not called.
6. Exceptions thrown inside the Filter are not resolved by @RestControllerAdvice because Filter runs outside the 
DispatcherServlet. @RestControllerAdvice is part of spring MVC's exception-resolution mechanism, which is invoked by 
DispatcherServlet. Thus, an unhandled Filter exception returns to the Servlet container, usually performs an error 
dispatch to /error, where Spring Boot's BasicErrorController generates the default error response.
7. In the Servlet Filter pattern, /api/* means /api and everything below it, while in the addPathPatterns() of Interceptor
/api/* means exactly one segment: for example, should be /api/orders, but not /api/orders/42. An authentic interceptor 
should exclude the /error so that error handling is not blocked by another authentication check, which should mask the 
original error or cause repeated error handling.



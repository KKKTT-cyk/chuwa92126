# Q1.
Filter is defined by Jakarta Servlet, while HandlerInterceptor is defined by SpringMVC.  
Filter runs before and after DispatcherServlet, Interceptor runs inside DispatcherServlet around the handler.  
Filter can access to every request the container receives, Interceptor can only access requests DispatcherServlet maps to a handler.

# Q2.

Code before chain.doFilter() runs on the way in, code after chain.doFilter() runs on the way out, after the controller has finished. if a filter doesnt call dofilter method, the request will stop here and will not reach to controller.

# Q3.
we can use FilterRegistrationBean with setOrder() and @Component with @order to register and control the order of filters. Spring Security's filter chain sits -100 by default, when a filter use both these two methods to register they can be registered twice， and will execute twice.

# Q4.
preHandle executes before controller, postHandle executes after the controller and before view rendering, afterCompletion executes after request completed. if controller throws an error, postHandle doesnt execute.
The response commited inside the HandlerAdapter before postHandle runs, so changing body or header in postHandle has no effect.

# Q5.
A.preHandle()  
B.preHandle()  
controller  
B.postHandle()  
A.postHandle()  
view render
B.afterCompletion()  
A.afterCompletion()  
When B.preHandle return false A.preHandle return True, B.preHandle return false, A.afterCompletion() nothing else run, the controller isn't called.

# Q6.
prehand runs inside the try block, so its exceptions are caught and passed to the resolvers. Filters run before dispatcherservlet is ever called and after it returns, an exception thrown there never passes through that try block.
way1. catch it in filter with highest priority. way2. delegate to spring's hanlerexceptionresolver.

# Q7.
Servlet filter registration uses the servlet specifications url patterns, here api/* means everything under /api  
Interceptor uses spring's path pattern, here api/* matches exactly one level below api/   
In Spring Boot, when a request fails, for example with an uncaught exception, a missing handler, or a call to response.sendError(), the container performs a second, internal ERROR dispatch to /error. That dispatch goes through DispatcherServlet again, so interceptors run again, and BasicErrorController is supposed to build the error response.



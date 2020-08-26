package com.developcollect.dcinfra.utils.spring;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author zak
 * @since 1.0.0
 */
class SpringMvcUtilClearUpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        SpringMvcUtil.mappingThreadLocal.remove();
    }

    @Override
    public void destroy() {

    }
}

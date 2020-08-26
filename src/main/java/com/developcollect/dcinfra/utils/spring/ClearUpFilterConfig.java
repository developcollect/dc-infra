package com.developcollect.dcinfra.utils.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author zak
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(SpringMvcUtil.class)
class ClearUpFilterConfig {

    @Bean
    public FilterRegistrationBean<SpringMvcUtilClearUpFilter> registerLoginCheckFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SpringMvcUtilClearUpFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("springMvcUtilClearUpFilter");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}

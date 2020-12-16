package com.developcollect.dcinfra.utils.spring;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author zak
 * @since 1.0.0
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext applicationContext;
    private static String applicationName;

    @Autowired
    private void init(@Value("${spring.application.name}") String applicationName) {
        SpringUtil.applicationName = applicationName.toLowerCase();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext.getParent() == SpringUtil.applicationContext
                || SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getApplicationContext().getBean(requiredType);
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        return getApplicationContext().getBeansOfType(type);
    }

    public static Object getBean(String name) throws BeansException {
        return getApplicationContext().getBean(name);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Object> beansWithAnnotation = getApplicationContext().getBeansWithAnnotation(annotationType);
        return beansWithAnnotation;
    }

    /**
     * 获取当前运行项目的路径
     *
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2019/11/18 9:20
     */
    public static String appHome() {
        ApplicationHome home = new ApplicationHome();
        return home.getDir().getAbsolutePath() + File.separator;
    }

    public static void publishEvent(ApplicationEvent event) {
        getApplicationContext().publishEvent(event);
    }


    public static String getApplicationName() {
        return applicationName;
    }


    /**
     * 获取一个有负债均衡的RestTemplate，这个RestTemplate能够通过服务名（而不是ip和端口）调接口
     *
     * @return org.springframework.web.client.RestTemplate
     * @author Zhu Kaixiao
     * @date 2020/11/20 9:39
     */
    public static RestTemplate getLoadBalancedRestTemplate() {
        return (RestTemplate) getBean("loadBalancedRestTemplate");
    }
}

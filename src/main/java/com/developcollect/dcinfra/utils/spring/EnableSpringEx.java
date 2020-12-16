package com.developcollect.dcinfra.utils.spring;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用Spring相关工具
 *
 * @author zak
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(RestTemplateConfig.class)
@Import({SpringUtil.class, WebUtil.class})
public @interface EnableSpringEx {
}

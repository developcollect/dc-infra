package com.developcollect.dcinfra.utils.spring;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/11/20 9:08
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
public class RestTemplateConfig {


    /**
     * 配置一个能够通过服务名调用接口的 RestTemplate
     * 注意：
     * 方法名不能变动，否则其他地方无法通过bean name获取bean
     */
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //单位为ms
        factory.setReadTimeout(600000);
        //单位为ms
        factory.setConnectTimeout(30000);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }


}

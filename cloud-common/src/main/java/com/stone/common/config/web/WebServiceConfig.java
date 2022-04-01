package com.stone.common.config.web;

import com.stone.common.interceptor.CloudWebLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebServiceConfig implements WebMvcConfigurer {

    @Autowired
    private CloudWebLogInterceptor cloudWebLogInterceptor;

    /**
     * 拦截器策略添加自定义拦截器
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cloudWebLogInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

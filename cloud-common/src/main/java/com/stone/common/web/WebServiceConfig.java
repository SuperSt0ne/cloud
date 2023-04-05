package com.stone.common.web;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebServiceConfig implements WebMvcConfigurer {

    private final List<HandlerInterceptor> handlerInterceptors;

    public WebServiceConfig(List<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }

    /**
     * 拦截器策略添加自定义拦截器
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

package com.stone.config;

import com.stone.common.exception.GlobalExceptionHandler;
import com.stone.common.http.converter.ApiResultHttpMessageConverter;
import com.stone.common.interceptor.CloudWebLogInterceptor;
import com.stone.common.web.WebServiceConfig;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public WebServiceConfig webServiceConfig() {
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        interceptors.add(cloudWebLogInterceptor());
        return new WebServiceConfig(interceptors);
    }

    @Bean
    public CloudWebLogInterceptor cloudWebLogInterceptor() {
        return new CloudWebLogInterceptor();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

//    @Bean
//    public HttpMessageConverters customConverters() {
//        return new HttpMessageConverters(true, Collections.singleton(new ApiResultHttpMessageConverter()));
//    }
}

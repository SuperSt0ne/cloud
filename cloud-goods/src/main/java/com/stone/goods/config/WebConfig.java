package com.stone.goods.config;

import com.stone.common.interceptor.CloudWebLogInterceptor;
import com.stone.common.web.WebServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
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
}

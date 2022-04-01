package com.stone.user.config;

import com.stone.common.interceptor.CloudWebLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {

    @Bean
    public CloudWebLogInterceptor cloudWebLogInterceptor() {
        return new CloudWebLogInterceptor();
    }
}

package com.stone.user.config;

import com.stone.common.config.web.WebServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public WebServiceConfig webServiceConfig() {
        return new WebServiceConfig();
    }

}

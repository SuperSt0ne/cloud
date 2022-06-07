package com.stone.search;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.stone.search")
@EnableDubbo(scanBasePackages = "com.stone.search.service")
@EnableCaching
public class CloudSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSearchApplication.class, args);
    }

}

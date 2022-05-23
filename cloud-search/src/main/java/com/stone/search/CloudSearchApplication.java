package com.stone.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.stone.search")
@EnableCaching
public class CloudSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSearchApplication.class, args);
    }

}

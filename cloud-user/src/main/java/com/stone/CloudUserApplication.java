package com.stone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.stone.user.mapper")
@SpringBootApplication
public class CloudUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudUserApplication.class, args);
    }

}
package com.stone.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stone.goods")
@EnableDubbo
public class CloudGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGoodsApplication.class, args);
    }

}

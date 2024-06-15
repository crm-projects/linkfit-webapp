package com.server.storefront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "com.server.storefront")
public class CreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatorApplication.class, args);
    }
}

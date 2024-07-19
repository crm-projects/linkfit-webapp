package com.server.storefront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@ComponentScan(basePackages = "com.server.storefront")
@SpringBootApplication
public class StorefrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorefrontApplication.class, args);
    }
}

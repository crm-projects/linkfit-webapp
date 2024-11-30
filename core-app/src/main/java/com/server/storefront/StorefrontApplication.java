package com.server.storefront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.server.storefront")
public class StorefrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorefrontApplication.class, args);
    }
}

package com.server.storefront.configs;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Linkfit API Documentation")
                        .version("v1")
                        .description("API for Linkfit services"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("https://api.linkfit.app"));  // Ensure Swagger uses HTTPS
    }
}


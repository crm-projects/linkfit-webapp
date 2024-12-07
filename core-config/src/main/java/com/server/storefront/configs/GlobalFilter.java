package com.server.storefront.configs;

import com.server.storefront.filters.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class GlobalFilter implements WebMvcConfigurer {


    private final JWTAuthFilter jwtAuthFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthFilter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://linkfit-waitlist.netlify.app/")
                .allowedMethods("POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

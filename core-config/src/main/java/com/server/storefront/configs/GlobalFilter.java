package com.server.storefront.configs;

import com.server.storefront.filters.DomainFilter;
import com.server.storefront.filters.HttpRequestFilter;
import com.server.storefront.filters.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class GlobalFilter implements WebMvcConfigurer {


    private final JWTAuthFilter jwtAuthFilter;

    private final HttpRequestFilter httpRequestFilter;

    private final DomainFilter domainFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthFilter);
        registry.addInterceptor(httpRequestFilter);
        registry.addInterceptor(domainFilter);
    }
}

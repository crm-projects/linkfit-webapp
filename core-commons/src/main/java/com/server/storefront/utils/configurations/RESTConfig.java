package com.server.storefront.utils.configurations;

import com.server.storefront.utils.interceptors.RESTValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to register all the Interceptors to {@link InterceptorRegistry}
 *
 * @author Vas
 * @version 1.0
 */
@Configuration
public class RESTConfig implements WebMvcConfigurer {

    @Autowired
    private RESTValidationInterceptor restValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restValidationInterceptor);
    }
}

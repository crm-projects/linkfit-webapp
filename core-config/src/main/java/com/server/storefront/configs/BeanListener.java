package com.server.storefront.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
@Configuration
public class BeanListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Application Context Initialized!");

        String[] beanNames = event.getApplicationContext().getBeanDefinitionNames();
        log.info("Bean creation order:");

        for (String beanName : beanNames) {
            log.info("Bean: {}", beanName);
        }
    }
}

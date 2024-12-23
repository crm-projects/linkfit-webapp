package com.server.storefront.configs;

import com.server.storefront.model.Config;
import com.server.storefront.repository.ConfigRepository;
import com.server.storefront.utils.ApplicationConstant;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class InitLoader {

    private final ConfigRepository config;

    private static Boolean jwtVerification;

    @PostConstruct
    private void init() {
        loadCache();
    }

    private void loadCache() {
        loadJWTVerificationCache();
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    private void loadJWTVerificationCache() {
        log.info("Scheduled run at : {}", System.currentTimeMillis());
        Config appConfig = config.findByProperty(ApplicationConstant.SKIP_JWT_VERIFICATION);
        jwtVerification = Boolean.parseBoolean(appConfig.getValue());
    }

    public static boolean getJwtVerification() {
        return jwtVerification;
    }
}

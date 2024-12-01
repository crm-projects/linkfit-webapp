package com.server.storefront.configs;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
public class EhCacheConfig {

    @Bean(name = "ehCacheManager")
    public CacheManager cacheManager() {
        CacheConfiguration<String, Integer> productCountCacheConfig =
                CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String.class, Integer.class,
                                ResourcePoolsBuilder.heap(100))
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(1)))
                        .build();

        javax.cache.configuration.Configuration<String, Integer> jcacheConfig =
                Eh107Configuration.fromEhcacheCacheConfiguration(productCountCacheConfig);

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        cacheManager.createCache("productCount", jcacheConfig);
        cacheManager.createCache("collectionCount", jcacheConfig);

        return cacheManager;
    }
}

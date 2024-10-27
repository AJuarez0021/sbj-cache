package com.work.cache.config;

import com.work.cache.model.Item;
import java.time.Duration;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author linux
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager ehCacheManager() {

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("itemsCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Item.class,
                                ResourcePoolsBuilder.heap(1000))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60))))
                .build(true);
       
        return cacheManager;
    }

}

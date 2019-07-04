/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.yami.shop.common.serializer.redis.FstRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

/**
 * redis 缓存配置，仅当配置文件中spring.cache.type = redis时生效
 * @author lgh
 */
@EnableCaching
@Configuration
public class RedisCacheConfig  {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

    	RedisCacheManager redisCacheManager = new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationWithTtl(3600),
                // 指定 key 策略
                this.getRedisCacheConfigurationMap()
            );
    	redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(16);
//        redisCacheConfigurationMap.put("UserInfoList", this.getRedisCacheConfigurationWithTtl(3000));
//        redisCacheConfigurationMap.put("UserInfoListAnother", this.getRedisCacheConfigurationWithTtl(18000));

        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {

        FstRedisSerializer kryoRedisSerializer = new FstRedisSerializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(kryoRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new FstRedisSerializer());
        redisTemplate.setHashValueSerializer(new FstRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}

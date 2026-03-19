/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis 缓存配置，仅当配置文件中spring.cache.type = redis时生效
 * @author lgh
 */
@EnableCaching
@Configuration
public class RedisCacheConfig  {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {

        RedisCacheManager redisCacheManager = new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationWithTtl(3600,redisSerializer),
                // 指定 key 策略
                this.getRedisCacheConfigurationMap(redisSerializer)
        );
        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(RedisSerializer<Object>  redisSerializer) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(16);
        redisCacheConfigurationMap.put("product", this.getRedisCacheConfigurationWithTtl(1800, redisSerializer));
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds,RedisSerializer<Object> redisSerializer) {


        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(redisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,RedisSerializer<Object> redisSerializer) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setEnableTransactionSupport(false);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 自定义redis序列化的机制,重新定义一个ObjectMapper.防止和MVC的冲突
     * https://juejin.im/post/5e869d426fb9a03c6148c97e
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return GenericJacksonJsonRedisSerializer.builder()
                // 尽量兼容旧 GenericJackson2JsonRedisSerializer 的 @class 结构
                .typePropertyName("@class")
                // 兼容旧系统：保留宽松 default typing
                .enableUnsafeDefaultTyping()
                // 兼容 Spring Cache 的 NullValue
                .enableSpringCacheNullValueSupport("@class")
                .customize(mapper -> mapper
                        .disable(MapperFeature.USE_ANNOTATIONS)

                        // 1. 反序列化遇到未知属性不报错
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                        // 2. 序列化空对象不报错
                        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

                        // 3. 无效子类型不报错
                        .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)

                        // 4. 不把日期 key 写成时间戳
                        .disable(DateTimeFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                )
                .build();
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }

}

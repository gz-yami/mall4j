/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author lanhai
 */
@Component
@AllArgsConstructor
public class CacheManagerUtil {

    private CacheManager cacheManager;

    @SuppressWarnings({"unchecked"})
    public <T> T getCache(String cacheName,String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (T)valueWrapper.get();
    }

    public void putCache(String cacheName,String key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    public void evictCache(String cacheName,String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
}

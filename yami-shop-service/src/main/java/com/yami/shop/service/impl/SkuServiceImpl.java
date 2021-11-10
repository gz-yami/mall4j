/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.Sku;
import com.yami.shop.dao.SkuMapper;
import com.yami.shop.service.SkuService;

/**
 *
 * @author lgh on 2018/09/29.
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

	@Override
	@Cacheable(cacheNames = "skuList", key = "#prodId")
	public List<Sku> listByProdId(Long prodId) {
		return skuMapper.listByProdId(prodId);
	}

	@Override
	@Cacheable(cacheNames = "sku", key = "#skuId")
	public Sku getSkuBySkuId(Long skuId) {
		return skuMapper.selectById(skuId);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = "sku", key = "#skuId"),
			@CacheEvict(cacheNames = "skuList", key = "#prodId")
	})
	public void removeSkuCacheBySkuId(Long skuId,Long prodId) {

	}

}

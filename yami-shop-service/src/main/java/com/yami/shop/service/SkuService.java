/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Sku;
import org.springframework.cache.annotation.CacheEvict;

/**
 *
 * @author lgh on 2018/09/29.
 */
public interface SkuService extends IService<Sku> {

	/**
	 * 根据商品id获取商品中的sku列表（将会被缓存起来）
	 * @param prodId 商品id
	 * @return sku列表
	 */
	List<Sku> listByProdId(Long prodId);

	/**
	 * 根据skuId获取sku信息（将会被缓存起来）
	 * @param skuId
	 * @return
	 */
	Sku getSkuBySkuId(Long skuId);

	void removeSkuCacheBySkuId(Long skuId,Long prodId);
}

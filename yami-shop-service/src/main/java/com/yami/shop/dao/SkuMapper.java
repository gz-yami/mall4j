/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yami.shop.bean.model.Sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SkuMapper extends BaseMapper<Sku> {

	/**
	 * 批量插入sku
	 * @param prodId 商品id
	 * @param skus sku列表
	 */
	void insertBatch(@Param("prodId") Long prodId, @Param("skuList") List<Sku> skuList);

	List<Sku> listByProdId(Long prodId);

	int updateStocks(@Param("sku") Sku sku);
	
	void deleteByProdId(@Param("prodId") Long prodId);

	void returnStock(@Param("skuCollect") Map<Long, Integer> skuCollect);

}
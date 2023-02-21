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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.model.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lanhai
 */
public interface SkuMapper extends BaseMapper<Sku> {

	/**
	 * 批量插入sku
	 * @param prodId 商品id
	 * @param skuList sku列表
	 */
	void insertBatch(@Param("prodId") Long prodId, @Param("skuList") List<Sku> skuList);

	/**
	 * 根据商品id获取商品sku列表
	 * @param prodId
	 * @return
	 */
	List<Sku> listByProdId(Long prodId);

	/**
	 * 更新sku库存
	 * @param sku
	 * @return
	 */
	int updateStocks(@Param("sku") Sku sku);

	/**
	 * 根据商品id删除sku
	 * @param prodId
	 */
	void deleteByProdId(@Param("prodId") Long prodId);

	/**
	 * 返回sku库存
	 * @param skuCollect
	 */
	void returnStock(@Param("skuCollect") Map<Long, Integer> skuCollect);

}
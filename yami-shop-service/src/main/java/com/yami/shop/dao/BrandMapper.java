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
import com.yami.shop.bean.model.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface BrandMapper extends BaseMapper<Brand> {

	/**
	 * 根据品牌名称获取品牌
	 * @param brandName 品牌名称
	 * @return 品牌信息
	 */
	Brand getByBrandName(String brandName);

	/**
	 * 根据分类id获取品牌列表
	 * @param categoryId 分类id
	 * @return 品牌列表
	 */
	List<Brand> listByCategoryId(@Param("categoryId")Long categoryId);
}
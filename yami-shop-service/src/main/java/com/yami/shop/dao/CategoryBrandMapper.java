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
import com.yami.shop.bean.model.CategoryBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface CategoryBrandMapper extends BaseMapper<CategoryBrand> {

	/**
	 * 增加分类品牌
	 * @param categoryId 分类id
	 * @param brandIds 品牌id列表
	 */
	void insertCategoryBrand(@Param("categoryId") Long categoryId, @Param("brandIds") List<Long> brandIds);

	/**
	 * 删除分类
	 * @param categoryId 分类id
	 */
	void deleteByCategoryId(Long categoryId);

	/**
	 * 根据品牌名称删除分类品牌
	 * @param brandId 品牌id
	 */
	void deleteByBrandId(Long brandId);
}
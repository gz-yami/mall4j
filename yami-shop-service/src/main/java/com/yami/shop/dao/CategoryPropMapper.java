/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yami.shop.bean.model.CategoryProp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CategoryPropMapper extends BaseMapper<CategoryProp> {

	void insertCategoryProp(@Param("categoryId") Long categoryId, @Param("propIds") List<Long> propIds);

	void deleteByCategoryId(Long categoryId);

	void deleteByPropId(Long propId);
}
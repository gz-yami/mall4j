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
import com.yami.shop.bean.model.Category;

import java.util.List;

/**
 * @author lanhai
 */
public interface CategoryMapper extends BaseMapper<Category> {

	/**
	 * 根据父级id获取分类列表
	 *
	 * @param parentId
	 * @return
	 */
	List<Category> listByParentId(Long parentId);

	/**
	 * 根据店铺id获取分类列表
	 *
	 * @param shopId
	 * @return
	 */
	List<Category> tableCategory(Long shopId);
}
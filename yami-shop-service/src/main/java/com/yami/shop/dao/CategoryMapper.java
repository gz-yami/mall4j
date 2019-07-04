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

import com.yami.shop.bean.model.Category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CategoryMapper extends BaseMapper<Category> {

	List<Category> listByParentId(Long parentId);

	List<Category> tableCategory(Long shopId);

	List<Category> listCategoryAndProdByShopId(Long shopId);
}
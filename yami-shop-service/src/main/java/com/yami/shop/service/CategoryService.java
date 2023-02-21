/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Category;

import java.util.List;

/**
 * @author lanhai
 * 商品分类
 */
public interface CategoryService extends IService<Category> {

	/**
	 * 根据parentId获取分类
	 * @param parentId 0 一级分类
	 * @return
	 */
	List<Category> listByParentId(Long parentId);

	/**
	 * 获取用于页面表单展现的category列表，根据seq排序
	 * @param shopId 店铺id
	 * @return
	 */
	List<Category> tableCategory(Long shopId);

	/**
	 * 保存分类、品牌、参数
	 * @param category
	 */
	void saveCategory(Category category);

	/**
	 * 修改分类、品牌、参数
	 * @param category
	 */
	void updateCategory(Category category);

	/**
	 * 删除分类、品牌、参数 以及分类对应的图片
	 * @param categoryId 分类id
	 */
	void deleteCategory(Long categoryId);

	/**
	 * 根据店铺id和层级，获取商品分类树
	 * @param shopId
	 * @param grade
	 * @return
	 */
	List<Category> treeSelect(Long shopId,int grade);

}

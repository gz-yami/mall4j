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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yami.shop.bean.model.Category;
import com.yami.shop.dao.CategoryBrandMapper;
import com.yami.shop.dao.CategoryMapper;
import com.yami.shop.dao.CategoryPropMapper;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.CategoryService;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author lanhai
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private CategoryBrandMapper categoryBrandMapper;
	
	@Autowired
	private CategoryPropMapper categoryPropMapper;
	
	@Autowired
	private AttachFileService attachFileService;
	
	@Autowired
	private MapperFacade mapperFacade;
	
	@Override
	public List<Category> listByParentId(Long parentId) {
		return categoryMapper.listByParentId(parentId);
	}

	@Override
	public List<Category> tableCategory(Long shopId) {
		return categoryMapper.tableCategory(shopId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveCategory(Category category) {
		category.setRecTime(new Date());
		// 保存分类信息
		categoryMapper.insert(category);
		
		insertBrandsAndAttributes(category);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCategory(Category category) {
		Category dbCategory = categoryMapper.selectById(category.getCategoryId());
		category.setUpdateTime(new Date());
		// 保存分类信息
		categoryMapper.updateById(category);
		// 先删除后增加
		deleteBrandsAndAttributes(category.getCategoryId());
		insertBrandsAndAttributes(category);
		// 如果以前有图片，并且图片与现在不同，则删除以前的图片
//		if (StrUtil.isNotBlank(dbCategory.getPic()) && !dbCategory.getPic().equals(category.getPic())) {
//			attachFileService.deleteFile(dbCategory.getPic());
//		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteCategory(Long categoryId) {
		Category category = categoryMapper.selectById(categoryId);
		categoryMapper.deleteById(categoryId);
		
		deleteBrandsAndAttributes(categoryId);
//		if (StrUtil.isNotBlank(category.getPic())) {
//			attachFileService.deleteFile(category.getPic());
//		}
	}
	

	private void deleteBrandsAndAttributes(Long categoryId) {
		// 删除所有分类所关联的品牌
		categoryBrandMapper.deleteByCategoryId(categoryId);
		// 删除所有分类所关联的参数
		categoryPropMapper.deleteByCategoryId(categoryId);
	}
	
	private void insertBrandsAndAttributes(Category category) {
		//保存分类与品牌信息
		if(CollUtil.isNotEmpty(category.getBrandIds())){
			categoryBrandMapper.insertCategoryBrand(category.getCategoryId(), category.getBrandIds());
		}
		
		//保存分类与参数信息
		if(CollUtil.isNotEmpty(category.getAttributeIds())){
			categoryPropMapper.insertCategoryProp(category.getCategoryId(), category.getAttributeIds());
		}
	}

	@Override
	public List<Category> treeSelect(Long shopId,int grade) {
    	List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>().le(Category::getGrade,grade).eq(Category::getShopId,shopId));
		return categoryListToTree(categories);
	}
	
	public List<Category> categoryListToTree(List<Category> categorys){
		if (CollectionUtils.isEmpty(categorys)) {
			return Lists.newArrayList();
		}
		Map<Long, List<Category>> categoryMap = categorys.stream().collect(Collectors.groupingBy(Category::getParentId));
		
		List<Category> rootList = categoryMap.get(0L);
		transformCategoryTree(rootList,categoryMap);
		return rootList;
	}
	
	public void transformCategoryTree(List<Category> categorys,Map<Long, List<Category>> categoryMap) {
		for (Category category : categorys) {
			List<Category> nextList = categoryMap.get(category.getCategoryId());
			if (CollectionUtils.isEmpty(nextList)) {
				continue;
			}
			// 将排序完成的list设为下一层级
			category.setCategories(nextList);
			// 处理下 下一层级
			transformCategoryTree(nextList, categoryMap);
		}
	}

}

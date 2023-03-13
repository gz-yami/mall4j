/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.Category;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;



/**
 * 分类管理
 * @author lgh
 *
 */
@RestController
@RequestMapping("/prod/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 获取菜单页面的表
	 * @return
	 */
	@GetMapping("/table")
	@PreAuthorize("@pms.hasPermission('prod:category:page')")
	public ServerResponseEntity<List<Category>> table(){
		List<Category> categoryMenuList = categoryService.tableCategory(SecurityUtils.getSysUser().getShopId());
		return ServerResponseEntity.success(categoryMenuList);
	}

	/**
	 * 获取分类信息
	 */
	@GetMapping("/info/{categoryId}")
	public ServerResponseEntity<Category> info(@PathVariable("categoryId") Long categoryId){
		Category category = categoryService.getById(categoryId);
		return ServerResponseEntity.success(category);
	}



	/**
	 * 保存分类
	 */
	@SysLog("保存分类")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('prod:category:save')")
	public ServerResponseEntity<Void> save(@RequestBody Category category){
		category.setShopId(SecurityUtils.getSysUser().getShopId());
		category.setRecTime(new Date());
		Category categoryName = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName,category.getCategoryName())
				.eq(Category::getShopId,category.getShopId()));
		if(categoryName != null){
			throw new YamiShopBindException("类目名称已存在！");
		}
		categoryService.saveCategory(category);
		return ServerResponseEntity.success();
	}

	/**
	 * 更新分类
	 */
	@SysLog("更新分类")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('prod:category:update')")
	public ServerResponseEntity<String> update(@RequestBody Category category){
		category.setShopId(SecurityUtils.getSysUser().getShopId());
		if (Objects.equals(category.getParentId(),category.getCategoryId())) {
			return ServerResponseEntity.showFailMsg("分类的上级不能是自己本身");
		}
		Category categoryName = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName,category.getCategoryName())
				.eq(Category::getShopId,category.getShopId()).ne(Category::getCategoryId,category.getCategoryId()));
		if(categoryName != null){
			throw new YamiShopBindException("类目名称已存在！");
		}
		Category categoryDb = categoryService.getById(category.getCategoryId());
		// 如果从下线改成正常，则需要判断上级的状态
		if (Objects.equals(categoryDb.getStatus(),0) && Objects.equals(category.getStatus(),1) && !Objects.equals(category.getParentId(),0L)){
			Category parentCategory = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryId, category.getParentId()));
			if(Objects.isNull(parentCategory) || Objects.equals(parentCategory.getStatus(),0)){
				// 修改失败，上级分类不存在或者不为正常状态
				throw new YamiShopBindException("修改失败，上级分类不存在或者不为正常状态");
			}
		}
		categoryService.updateCategory(category);
		return ServerResponseEntity.success();
	}

	/**
	 * 删除分类
	 */
	@SysLog("删除分类")
	@DeleteMapping("/{categoryId}")
	@PreAuthorize("@pms.hasPermission('prod:category:delete')")
	public ServerResponseEntity<String> delete(@PathVariable("categoryId") Long categoryId){
		if (categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getParentId,categoryId)) >0) {
			return ServerResponseEntity.showFailMsg("请删除子分类，再删除该分类");
		}
		categoryService.deleteCategory(categoryId);
		return ServerResponseEntity.success();
	}

	/**
	 * 所有的
	 */
	@GetMapping("/listCategory")
	public ServerResponseEntity<List<Category>> listCategory(){

		return ServerResponseEntity.success(categoryService.list(new LambdaQueryWrapper<Category>()
														.le(Category::getGrade, 2)
														.eq(Category::getShopId, SecurityUtils.getSysUser().getShopId())
														.orderByAsc(Category::getSeq)));
	}

	/**
	 * 所有的产品分类
	 */
	@GetMapping("/listProdCategory")
	public ServerResponseEntity<List<Category>> listProdCategory(){
    	List<Category> categories = categoryService.treeSelect(SecurityUtils.getSysUser().getShopId(),2);
		return ServerResponseEntity.success(categories);
	}
}

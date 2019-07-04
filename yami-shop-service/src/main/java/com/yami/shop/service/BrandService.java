/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Brand;

import java.util.List;

/**
 *
 * Created by lgh on 2018/07/05.
 */
public interface BrandService extends IService<Brand> {

	/**
	 * 根据品牌名称获取该品牌
	 * @param brandName
	 * @return
	 */
	Brand getByBrandName(String brandName);

	/**
	 * 删除品牌，同时删除品牌与分类之间的关联关系
	 * @param brandId
	 */
	void deleteByBrand(Long brandId);

	/**
	 * 根据分类id获取品牌列表
	 * @param categoryId 分类id
	 * @return
	 */
	List<Brand> listByCategoryId(Long categoryId);

}

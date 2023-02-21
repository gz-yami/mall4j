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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.Brand;
import com.yami.shop.dao.BrandMapper;
import com.yami.shop.dao.CategoryBrandMapper;
import com.yami.shop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lanhai
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;
    
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    
	@Override
	public Brand getByBrandName(String brandName) {
		return brandMapper.getByBrandName(brandName);
	}

	@Override
	public void deleteByBrand(Long brandId) {
		brandMapper.deleteById(brandId);
		categoryBrandMapper.deleteByBrandId(brandId);
	}

	@Override
	public List<Brand> listByCategoryId(Long categoryId) {
		return brandMapper.listByCategoryId(categoryId);
	}

}

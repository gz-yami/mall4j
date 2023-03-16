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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Brand;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Objects;


/**
 * 品牌管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/admin/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:brand:page')")
    public ServerResponseEntity<IPage<Brand>> page(Brand brand,PageParam<Brand> page) {
        IPage<Brand> brands = brandService.page(page,
                new LambdaQueryWrapper<Brand>()
                        .like(StrUtil.isNotBlank(brand.getBrandName()), Brand::getBrandName, brand.getBrandName()).orderByAsc(Brand::getFirstChar));
        return ServerResponseEntity.success(brands);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pms.hasPermission('admin:brand:info')")
    public ServerResponseEntity<Brand> info(@PathVariable("id") Long id) {
        Brand brand = brandService.getById(id);
        return ServerResponseEntity.success(brand);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin:brand:save')")
    public ServerResponseEntity<Void> save(@Valid Brand brand) {
        Brand dbBrand = brandService.getByBrandName(brand.getBrandName());
        if (dbBrand != null) {
            throw new YamiShopBindException("该品牌名称已存在");
        }
        brandService.save(brand);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:brand:update')")
    public ServerResponseEntity<Void> update(@Valid Brand brand) {
        Brand dbBrand = brandService.getByBrandName(brand.getBrandName());
        if (dbBrand != null && !Objects.equals(dbBrand.getBrandId(), brand.getBrandId())) {
            throw new YamiShopBindException("该品牌名称已存在");
        }
        brandService.updateById(brand);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('admin:brand:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.deleteByBrand(id);
        return ServerResponseEntity.success();
    }

}

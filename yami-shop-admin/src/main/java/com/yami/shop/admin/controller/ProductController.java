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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.bean.param.ProductParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.BasketService;
import com.yami.shop.service.ProdTagReferenceService;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SkuService;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 商品列表、商品发布controller
 *
 * @author lgh
 */
@RestController
@RequestMapping("/prod/prod")
public class ProductController {

    @Autowired
    private ProductService productService;


    @Autowired
    private SkuService skuService;

    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    @Autowired
    private BasketService basketService;

    /**
     * 分页获取商品信息
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('prod:prod:page')")
    public ServerResponseEntity<IPage<Product>> page(ProductParam product, PageParam<Product> page) {
        IPage<Product> products = productService.page(page,
                new LambdaQueryWrapper<Product>()
                        .like(StrUtil.isNotBlank(product.getProdName()), Product::getProdName, product.getProdName())
                        .eq(Product::getShopId, SecurityUtils.getSysUser().getShopId())
                        .eq(product.getStatus() != null, Product::getStatus, product.getStatus())
                        .orderByDesc(Product::getPutawayTime));
        return ServerResponseEntity.success(products);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{prodId}")
    @PreAuthorize("@pms.hasPermission('prod:prod:info')")
    public ServerResponseEntity<Product> info(@PathVariable("prodId") Long prodId) {
        Product prod = productService.getProductByProdId(prodId);
        if (!Objects.equals(prod.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new YamiShopBindException("没有权限获取该商品规格信息");
        }
        List<Sku> skuList = skuService.listByProdId(prodId);
        prod.setSkuList(skuList);

        //获取分组标签
        List<Long> listTagId = prodTagReferenceService.listTagIdByProdId(prodId);
        prod.setTagList(listTagId);
        return ServerResponseEntity.success(prod);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:prod:save')")
    public ServerResponseEntity<String> save(@Valid @RequestBody ProductParam productParam) {
        checkParam(productParam);

        Product product = BeanUtil.copyProperties(productParam, Product.class);
        product.setDeliveryMode(Json.toJsonString(productParam.getDeliveryModeVo()));
        product.setShopId(SecurityUtils.getSysUser().getShopId());
        product.setUpdateTime(new Date());
        if (product.getStatus() == 1) {
            product.setPutawayTime(new Date());
        }
        product.setCreateTime(new Date());
        productService.saveProduct(product);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:prod:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody ProductParam productParam) {
        checkParam(productParam);
        Product dbProduct = productService.getProductByProdId(productParam.getProdId());
        if (!Objects.equals(dbProduct.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            return ServerResponseEntity.showFailMsg("无法修改非本店铺商品信息");
        }

        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId());


        Product product = BeanUtil.copyProperties(productParam, Product.class);
        product.setDeliveryMode(Json.toJsonString(productParam.getDeliveryModeVo()));
        product.setUpdateTime(new Date());

        if (dbProduct.getStatus() == 0 || productParam.getStatus() == 1) {
            product.setPutawayTime(new Date());
        }
        dbProduct.setSkuList(dbSkus);
        productService.updateProduct(product, dbProduct);


        List<String> userIds = basketService.listUserIdByProdId(product.getProdId());

        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    public ServerResponseEntity<Void> delete(Long prodId) {
        Product dbProduct = productService.getProductByProdId(prodId);
        if (!Objects.equals(dbProduct.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new YamiShopBindException("无法获取非本店铺商品信息");
        }
        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId());
        // 删除商品
        productService.removeProductByProdId(prodId);

        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }


        List<String> userIds = basketService.listUserIdByProdId(prodId);

        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }

        return ServerResponseEntity.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('prod:prod:delete')")
    public ServerResponseEntity<Void> batchDelete(@RequestBody Long[] prodIds) {
        for (Long prodId : prodIds) {
            delete(prodId);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 更新商品状态
     */
    @PutMapping("/prodStatus")
    @PreAuthorize("@pms.hasPermission('prod:prod:status')")
    public ServerResponseEntity<Void> shopStatus(@RequestParam Long prodId, @RequestParam Integer prodStatus) {
        Product product = new Product();
        product.setProdId(prodId);
        product.setStatus(prodStatus);
        if (prodStatus == 1) {
            product.setPutawayTime(new Date());
        }
        productService.updateById(product);
        productService.removeProductCacheByProdId(prodId);
        List<String> userIds = basketService.listUserIdByProdId(prodId);

        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
        return ServerResponseEntity.success();
    }

    private void checkParam(ProductParam productParam) {
        if (CollectionUtil.isEmpty(productParam.getTagList())) {
            throw new YamiShopBindException("请选择产品分组");
        }

        Product.DeliveryModeVO deliveryMode = productParam.getDeliveryModeVo();
        boolean hasDeliverMode = deliveryMode != null
                && (deliveryMode.getHasShopDelivery() || deliveryMode.getHasUserPickUp());
        if (!hasDeliverMode) {
            throw new YamiShopBindException("请选择配送方式");
        }
        List<Sku> skuList = productParam.getSkuList();
        boolean isAllUnUse = true;
        for (Sku sku : skuList) {
            if (sku.getStatus() == 1) {
                isAllUnUse = false;
            }
        }
        if (isAllUnUse) {
            throw new YamiShopBindException("至少要启用一种商品规格");
        }
    }
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.bean.model.Transport;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SkuService;
import com.yami.shop.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/prod")
@Tag(name = "商品接口")
public class ProdController {

    @Autowired
    private ProductService prodService;


    @Autowired
    private SkuService skuService;

    @Autowired
    private TransportService transportService;


    @GetMapping("/pageProd")
    @Operation(summary = "通过分类id商品列表信息" , description = "根据分类ID获取该分类下所有的商品列表信息")
    @Parameters({
            @Parameter(name = "categoryId", description = "分类ID" , required = true),
    })
    public ServerResponseEntity<IPage<ProductDto>> prodList(
            @RequestParam(value = "categoryId") Long categoryId,PageParam<ProductDto> page) {
        IPage<ProductDto> productPage = prodService.pageByCategoryId(page, categoryId);
        return ServerResponseEntity.success(productPage);
    }

    @GetMapping("/prodInfo")
    @Operation(summary = "商品详情信息" , description = "根据商品ID（prodId）获取商品信息")
    @Parameter(name = "prodId", description = "商品ID" , required = true)
    public ServerResponseEntity<ProductDto> prodInfo(Long prodId) {

        Product product = prodService.getProductByProdId(prodId);
        if (product == null) {
            return ServerResponseEntity.success();
        }

        List<Sku> skuList = skuService.listByProdId(prodId);
        // 启用的sku列表
        List<Sku> useSkuList = skuList.stream().filter(sku -> sku.getStatus() == 1).collect(Collectors.toList());
        product.setSkuList(useSkuList);
        ProductDto productDto = BeanUtil.copyProperties(product, ProductDto.class);


        // 商品的配送方式
        Product.DeliveryModeVO deliveryModeVO = Json.parseObject(product.getDeliveryMode(), Product.DeliveryModeVO.class);
        // 有店铺配送的方式, 且存在运费模板，才返回运费模板的信息，供前端查阅
        if (deliveryModeVO.getHasShopDelivery()  && product.getDeliveryTemplateId() != null) {
            Transport transportAndAllItems = transportService.getTransportAndAllItems(product.getDeliveryTemplateId());
            productDto.setTransport(transportAndAllItems);
        }

        return ServerResponseEntity.success(productDto);
    }

    @GetMapping("/lastedProdPage")
    @Operation(summary = "新品推荐" , description = "获取新品推荐商品列表")
    @Parameters({
    })
    public ServerResponseEntity<IPage<ProductDto>> lastedProdPage(PageParam<ProductDto> page) {
        IPage<ProductDto> productPage = prodService.pageByPutAwayTime(page);
        return ServerResponseEntity.success(productPage);
    }

    @GetMapping("/prodListByTagId")
    @Operation(summary = "通过分组标签获取商品列表" , description = "通过分组标签id（tagId）获取商品列表")
    @Parameters({
            @Parameter(name = "tagId", description = "当前页，默认为1" , required = true),
    })
    public ServerResponseEntity<IPage<ProductDto>> prodListByTagId(
            @RequestParam(value = "tagId") Long tagId,PageParam<ProductDto> page) {
        IPage<ProductDto> productPage = prodService.pageByTagId(page, tagId);
        return ServerResponseEntity.success(productPage);
    }

    @GetMapping("/moreBuyProdList")
    @Operation(summary = "每日疯抢" , description = "获取销量最多的商品列表")
    @Parameters({})
    public ServerResponseEntity<IPage<ProductDto>> moreBuyProdList(PageParam<ProductDto> page) {
        IPage<ProductDto> productPage = prodService.moreBuyProdList(page);
        return ServerResponseEntity.success(productPage);
    }

    @GetMapping("/tagProdList")
    @Operation(summary = "首页所有标签商品接口" , description = "获取首页所有标签商品接口")
    public ServerResponseEntity<List<TagProductDto>> getTagProdList() {
        List<TagProductDto> productDtoList = prodService.tagProdList();
        return ServerResponseEntity.success(productDtoList);
    }
}

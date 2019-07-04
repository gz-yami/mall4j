/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.util.PageParam;
import com.fasterxml.jackson.annotation.JsonView;
import com.yami.shop.bean.app.dto.SkuDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.model.Basket;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.service.*;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Transport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

@RestController
@RequestMapping("/prod")
@Api(tags = "商品接口")
public class ProdController {

    @Autowired
    private ProductService prodService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SkuService skuService;

    @Autowired
    private TransportService transportService;


    @GetMapping("/pageProd")
    @ApiOperation(value = "通过分类id商品列表信息", notes = "根据分类ID获取该分类下所有的商品列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "Long"),
    })
    public ResponseEntity<IPage<ProductDto>> prodList(
            @RequestParam(value = "categoryId") Long categoryId,PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoIPage = prodService.pageByCategoryId(page, categoryId);
        return ResponseEntity.ok(productDtoIPage);
    }

    @GetMapping("/prodInfo")
    @ApiOperation(value = "商品详情信息", notes = "根据商品ID（prodId）获取商品信息")
    @ApiImplicitParam(name = "prodId", value = "商品ID", required = true, dataType = "Long")
    public ResponseEntity<ProductDto> prodInfo(Long prodId) {

        Product product = prodService.getProductByProdId(prodId);
        if (product == null) {
            return ResponseEntity.ok(null);
        }

        List<Sku> skuList = skuService.listByProdId(prodId);
        // 启用的sku列表
        List<Sku> useSkuList = skuList.stream().filter(sku -> sku.getStatus() == 1).collect(Collectors.toList());
        product.setSkuList(useSkuList);
        ProductDto productDto = mapperFacade.map(product, ProductDto.class);
        Transport transportAndAllItems = transportService.getTransportAndAllItems(product.getDeliveryTemplateId());
        productDto.setTransport(transportAndAllItems);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/lastedProdPage")
    @ApiOperation(value = "新品推荐", notes = "获取新品推荐商品列表")
    @ApiImplicitParams({
    })
    public ResponseEntity<IPage<ProductDto>> lastedProdPage(PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoIPage = prodService.pageByPutawayTime(page);
        return ResponseEntity.ok(productDtoIPage);
    }

    @GetMapping("/prodListByTagId")
    @ApiOperation(value = "通过分组标签获取商品列表", notes = "通过分组标签id（tagId）获取商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagId", value = "当前页，默认为1", required = true, dataType = "Long"),
    })
    public ResponseEntity<IPage<ProductDto>> prodListByTagId(
            @RequestParam(value = "tagId") Long tagId,PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoIPage = prodService.pageByTagId(page, tagId);
        return ResponseEntity.ok(productDtoIPage);
    }

    @GetMapping("/discountProdList")
    @ApiOperation(value = "限时特惠", notes = "获取限时特惠商品列表")
    @ApiImplicitParams({
    })
    public ResponseEntity<IPage<ProductDto>> discountProdList(PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoIPage = prodService.discountProdList(page);
        return ResponseEntity.ok(productDtoIPage);
    }

    @GetMapping("/moreBuyProdList")
    @ApiOperation(value = "每日疯抢", notes = "获取销量最多的商品列表")
    @ApiImplicitParams({})
    public ResponseEntity<IPage<ProductDto>> moreBuyProdList(PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoIPage = prodService.moreBuyProdList(page);
        return ResponseEntity.ok(productDtoIPage);
    }

    @GetMapping("/tagProdList")
    @ApiOperation(value = "首页所有标签商品接口", notes = "获取首页所有标签商品接口")
    public ResponseEntity<List<TagProductDto>> getTagProdList() {
        List<TagProductDto> productDtoList = prodService.tagProdList();
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/discountProds")
    @ApiOperation(value = "根据活动id获取活动商品列表", notes = "根据活动id获取活动商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "discountId", value = "活动id", required = true, dataType = "Long"),
    })
    public ResponseEntity<IPage<ProductDto>> getDiscountProds(
            @RequestParam(value = "discountId", required = true) Long discountId,
            PageParam<ProductDto> page) {
        IPage<ProductDto> productDtoList = prodService.pageByDiscountId(page, discountId);
        return ResponseEntity.ok(productDtoList);
    }



}

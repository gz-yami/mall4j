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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.UserCollectionDto;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.UserCollection;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.UserCollectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/user/collection")
@Tag(name = "收藏接口")
@AllArgsConstructor
public class UserCollectionController {

    private final UserCollectionService userCollectionService;

    private final ProductService productService;

    @GetMapping("/page")
    @Operation(summary = "分页返回收藏数据" , description = "根据用户id获取")
    public ServerResponseEntity<IPage<UserCollectionDto>> getUserCollectionDtoPageByUserId(PageParam page) {
        return ServerResponseEntity.success(userCollectionService.getUserCollectionDtoPageByUserId(page, SecurityUtils.getUser().getUserId()));
    }

    @GetMapping("isCollection")
    @Operation(summary = "根据商品id获取该商品是否在收藏夹中" , description = "传入收藏商品id")
    public ServerResponseEntity<Boolean> isCollection(Long prodId) {
        if (productService.count(new LambdaQueryWrapper<Product>()
                .eq(Product::getProdId, prodId)) < 1) {
            throw new YamiShopBindException("该商品不存在");
        }
        return ServerResponseEntity.success(userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getProdId, prodId)
                .eq(UserCollection::getUserId, SecurityUtils.getUser().getUserId())) > 0);
    }

    @PostMapping("/addOrCancel")
    @Operation(summary = "添加/取消收藏" , description = "传入收藏商品id,如果商品未收藏则收藏商品，已收藏则取消收藏")
    @Parameter(name = "prodId", description = "商品id" , required = true)
    public ServerResponseEntity<Void> addOrCancel(@RequestBody Long prodId) {
        if (Objects.isNull(productService.getProductByProdId(prodId))) {
            throw new YamiShopBindException("该商品不存在");
        }
        String userId = SecurityUtils.getUser().getUserId();
        if (userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getProdId, prodId)
                .eq(UserCollection::getUserId, userId)) > 0) {
            userCollectionService.remove(new LambdaQueryWrapper<UserCollection>()
                    .eq(UserCollection::getProdId, prodId)
                    .eq(UserCollection::getUserId, userId));
        } else {
            UserCollection userCollection = new UserCollection();
            userCollection.setCreateTime(new Date());
            userCollection.setUserId(userId);
            userCollection.setProdId(prodId);
            userCollectionService.save(userCollection);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 查询用户收藏商品数量
     */
    @GetMapping("count")
    @Operation(summary = "查询用户收藏商品数量" , description = "查询用户收藏商品数量")
    public int findUserCollectionCount() {
        String userId = SecurityUtils.getUser().getUserId();
        return userCollectionService.count(new LambdaQueryWrapper<UserCollection>().eq(UserCollection::getUserId, userId));
    }

    @GetMapping("/prods")
    @Operation(summary = "获取用户收藏商品列表" , description = "获取用户收藏商品列表")
    public ServerResponseEntity<IPage<ProductDto>> collectionProds(PageParam page) {
        String userId = SecurityUtils.getUser().getUserId();
        IPage<ProductDto> productDtoPage = productService.collectionProds(page, userId);
        return ServerResponseEntity.success(productDtoPage);
    }

}

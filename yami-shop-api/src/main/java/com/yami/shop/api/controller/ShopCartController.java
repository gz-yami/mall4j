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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.yami.shop.bean.app.dto.*;
import com.yami.shop.bean.app.param.ChangeShopCartParam;
import com.yami.shop.bean.app.param.ShopCartParam;
import com.yami.shop.bean.event.ShopCartEvent;
import com.yami.shop.bean.model.Basket;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.util.Arith;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.BasketService;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SkuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/shopCart")
@Tag(name = "购物车接口")
@AllArgsConstructor
public class ShopCartController {

    private BasketService basketService;

    private ProductService productService;

    private SkuService skuService;

    private ApplicationContext applicationContext;

    /**
     * 获取用户购物车信息
     *
     * @param basketIdShopCartParamMap 购物车参数对象列表
     * @return
     */
    @PostMapping("/info")
    @Operation(summary = "获取用户购物车信息" , description = "获取用户购物车信息，参数为用户选中的活动项数组,以购物车id为key")
    public ServerResponseEntity<List<ShopCartDto>> info(@RequestBody Map<Long, ShopCartParam> basketIdShopCartParamMap) {
        String userId = SecurityUtils.getUser().getUserId();

        // 更新购物车信息，
        if (MapUtil.isNotEmpty(basketIdShopCartParamMap)) {
            basketService.updateBasketByShopCartParam(userId, basketIdShopCartParamMap);
        }

        // 拿到购物车的所有item
        List<ShopCartItemDto> shopCartItems = basketService.getShopCartItems(userId);
        return ServerResponseEntity.success(basketService.getShopCarts(shopCartItems));

    }

    @DeleteMapping("/deleteItem")
    @Operation(summary = "删除用户购物车物品" , description = "通过购物车id删除用户购物车物品")
    public ServerResponseEntity<Void> deleteItem(@RequestBody List<Long> basketIds) {
        String userId = SecurityUtils.getUser().getUserId();
        basketService.deleteShopCartItemsByBasketIds(userId, basketIds);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteAll")
    @Operation(summary = "清空用户购物车所有物品" , description = "清空用户购物车所有物品")
    public ServerResponseEntity<String> deleteAll() {
        String userId = SecurityUtils.getUser().getUserId();
        basketService.deleteAllShopCartItems(userId);
        return ServerResponseEntity.success("删除成功");
    }

    @PostMapping("/changeItem")
    @Operation(summary = "添加、修改用户购物车物品", description = "通过商品id(prodId)、skuId、店铺Id(shopId),添加/修改用户购物车商品，并传入改变的商品个数(count)，" +
            "当count为正值时，增加商品数量，当count为负值时，将减去商品的数量，当最终count值小于0时，会将商品从购物车里面删除")
    public ServerResponseEntity<String> addItem(@Valid @RequestBody ChangeShopCartParam param) {

        if (param.getCount() == 0) {
            return ServerResponseEntity.showFailMsg("输入更改数量");
        }

        String userId = SecurityUtils.getUser().getUserId();
        List<ShopCartItemDto> shopCartItems = basketService.getShopCartItems(userId);
        Product prodParam = productService.getProductByProdId(param.getProdId());
        Sku skuParam = skuService.getSkuBySkuId(param.getSkuId());

        // 当商品状态不正常时，不能添加到购物车
        if (prodParam.getStatus() != 1 || skuParam.getStatus() != 1) {
            return ServerResponseEntity.showFailMsg("当前商品已下架");
        }
        for (ShopCartItemDto shopCartItemDto : shopCartItems) {
            if (Objects.equals(param.getSkuId(), shopCartItemDto.getSkuId())) {
                Basket basket = new Basket();
                basket.setUserId(userId);
                basket.setBasketCount(param.getCount() + shopCartItemDto.getProdCount());
                basket.setBasketId(shopCartItemDto.getBasketId());

                // 防止购物车变成负数
                if (basket.getBasketCount() <= 0) {
                    basketService.deleteShopCartItemsByBasketIds(userId, Collections.singletonList(basket.getBasketId()));
                    return ServerResponseEntity.success();
                }

                // 当sku实际库存不足时，不能添加到购物车
                if (skuParam.getStocks() < basket.getBasketCount() && shopCartItemDto.getProdCount() > 0) {
                    return ServerResponseEntity.showFailMsg("库存不足");
                }
                basketService.updateShopCartItem(basket);
                return ServerResponseEntity.success();
            }
        }

        // 防止购物车已被删除的情况下,添加了负数的商品
        if (param.getCount() < 0) {
            return ServerResponseEntity.showFailMsg("商品已从购物车移除");
        }
        // 当sku实际库存不足时，不能添加到购物车
        if (skuParam.getStocks() < param.getCount()) {
            return ServerResponseEntity.showFailMsg("库存不足");
        }
        // 所有都正常时
        basketService.addShopCartItem(param,userId);
        return ServerResponseEntity.success("添加成功");
    }

    @GetMapping("/prodCount")
    @Operation(summary = "获取购物车商品数量" , description = "获取所有购物车商品数量")
    public ServerResponseEntity<Integer> prodCount() {
        String userId = SecurityUtils.getUser().getUserId();
        List<ShopCartItemDto> shopCartItems = basketService.getShopCartItems(userId);
        if (CollectionUtil.isEmpty(shopCartItems)) {
            return ServerResponseEntity.success(0);
        }
        Integer totalCount = shopCartItems.stream().map(ShopCartItemDto::getProdCount).reduce(0, Integer::sum);
        return ServerResponseEntity.success(totalCount);
    }

    @GetMapping("/expiryProdList")
    @Operation(summary = "获取购物车失效商品信息" , description = "获取购物车失效商品列表")
    public ServerResponseEntity<List<ShopCartExpiryItemDto>> expiryProdList() {
        String userId = SecurityUtils.getUser().getUserId();
        List<ShopCartItemDto> shopCartItems = basketService.getShopCartExpiryItems(userId);
        //根据店铺ID划分item
        Map<Long, List<ShopCartItemDto>> shopCartItemDtoMap = shopCartItems.stream().collect(Collectors.groupingBy(ShopCartItemDto::getShopId));

        // 返回一个店铺对应的所有信息
        List<ShopCartExpiryItemDto> shopcartExpiryitems = Lists.newArrayList();

        for (Long key : shopCartItemDtoMap.keySet()) {
            ShopCartExpiryItemDto shopCartExpiryItemDto = new ShopCartExpiryItemDto();
            shopCartExpiryItemDto.setShopId(key);
            List<ShopCartItemDto> shopCartItemDtos = Lists.newArrayList();
            for (ShopCartItemDto tempShopCartItemDto : shopCartItemDtoMap.get(key)) {
                shopCartExpiryItemDto.setShopName(tempShopCartItemDto.getShopName());
                shopCartItemDtos.add(tempShopCartItemDto);
            }
            shopCartExpiryItemDto.setShopCartItemDtoList(shopCartItemDtos);
            shopcartExpiryitems.add(shopCartExpiryItemDto);
        }

        return ServerResponseEntity.success(shopcartExpiryitems);
    }

    @DeleteMapping("/cleanExpiryProdList")
    @Operation(summary = "清空用户失效商品" , description = "清空用户失效商品")
    public ServerResponseEntity<Void> cleanExpiryProdList() {
        String userId = SecurityUtils.getUser().getUserId();
        basketService.cleanExpiryProdList(userId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/totalPay")
    @Operation(summary = "获取选中购物项总计、选中的商品数量" , description = "获取选中购物项总计、选中的商品数量,参数为购物车id数组")
    public ServerResponseEntity<ShopCartAmountDto> getTotalPay(@RequestBody List<Long> basketIds) {

        // 拿到购物车的所有item
        List<ShopCartItemDto> dbShopCartItems = basketService.getShopCartItems(SecurityUtils.getUser().getUserId());

        List<ShopCartItemDto> chooseShopCartItems = dbShopCartItems
                                                        .stream()
                                                        .filter(shopCartItemDto -> {
                                                            for (Long basketId : basketIds) {
                                                                if (Objects.equals(basketId,shopCartItemDto.getBasketId())) {
                                                                    return  true;
                                                                }
                                                            }
                                                            return false;
                                                        })
                                                        .collect(Collectors.toList());

        // 根据店铺ID划分item
        Map<Long, List<ShopCartItemDto>> shopCartMap = chooseShopCartItems.stream().collect(Collectors.groupingBy(ShopCartItemDto::getShopId));

        double total = 0.0;
        int count = 0;
        double reduce = 0.0;
        for (Long shopId : shopCartMap.keySet()) {
            //获取店铺的所有商品项
            List<ShopCartItemDto> shopCartItemDtoList = shopCartMap.get(shopId);
            // 构建每个店铺的购物车信息
            ShopCartDto shopCart = new ShopCartDto();
            shopCart.setShopId(shopId);

            applicationContext.publishEvent(new ShopCartEvent(shopCart, shopCartItemDtoList));

            List<ShopCartItemDiscountDto> shopCartItemDiscounts = shopCart.getShopCartItemDiscounts();

            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemDto> shopCartItems = shopCartItemDiscount.getShopCartItems();

                for (ShopCartItemDto shopCartItem : shopCartItems) {
                    count = shopCartItem.getProdCount() + count;
                    total = Arith.add(shopCartItem.getProductTotalAmount(), total);
                }
            }
        }
        ShopCartAmountDto shopCartAmountDto = new ShopCartAmountDto();
        shopCartAmountDto.setCount(count);
        shopCartAmountDto.setTotalMoney(total);
        shopCartAmountDto.setSubtractMoney(reduce);
        shopCartAmountDto.setFinalMoney(Arith.sub(shopCartAmountDto.getTotalMoney(), shopCartAmountDto.getSubtractMoney()));

        return ServerResponseEntity.success(shopCartAmountDto);
    }

}

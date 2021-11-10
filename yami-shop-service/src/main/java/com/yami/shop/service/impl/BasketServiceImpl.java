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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yami.shop.bean.app.dto.*;
import com.yami.shop.bean.app.param.ChangeShopCartParam;
import com.yami.shop.bean.app.param.OrderItemParam;
import com.yami.shop.bean.app.param.ShopCartParam;
import com.yami.shop.bean.event.ShopCartEvent;
import com.yami.shop.bean.model.Basket;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.util.Arith;
import com.yami.shop.common.util.CacheManagerUtil;
import com.yami.shop.dao.BasketMapper;
import com.yami.shop.service.BasketService;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.SkuService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lgh on 2018/10/18.
 */
@Service
@AllArgsConstructor
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService {

    private final BasketMapper basketMapper;

    private final CacheManagerUtil cacheManagerUtil;

    private final ApplicationContext applicationContext;

    private final SkuService skuService;

    private final ShopDetailService shopDetailService;

    private final ProductService productService;

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void deleteShopCartItemsByBasketIds(String userId, List<Long> basketIds) {
        basketMapper.deleteShopCartItemsByBasketIds(userId, basketIds);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void addShopCartItem(ChangeShopCartParam param, String userId) {
        Basket basket = new Basket();
        basket.setBasketCount(param.getCount());
        basket.setBasketDate(new Date());
        basket.setProdId(param.getProdId());
        basket.setShopId(param.getShopId());
        basket.setUserId(userId);
        basket.setSkuId(param.getSkuId());
        basket.setDistributionCardNo(param.getDistributionCardNo());
        basketMapper.insert(basket);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#basket.userId")
    public void updateShopCartItem(Basket basket) {
        basketMapper.updateById(basket);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void deleteAllShopCartItems(String userId) {
        basketMapper.deleteAllShopCartItems(userId);
    }

    @Override
    public List<ShopCartItemDto> getShopCartItems(String userId) {
        // 在这个类里面要调用这里的缓存信息，并没有使用aop，所以不使用注解
        List<ShopCartItemDto> shopCartItemDtoList = cacheManagerUtil.getCache("ShopCartItems", userId);
        if (shopCartItemDtoList != null) {
            return shopCartItemDtoList;
        }
        shopCartItemDtoList = basketMapper.getShopCartItems(userId);
        for (ShopCartItemDto shopCartItemDto : shopCartItemDtoList) {
            shopCartItemDto.setProductTotalAmount(Arith.mul(shopCartItemDto.getProdCount(), shopCartItemDto.getPrice()));
        }
        cacheManagerUtil.putCache("ShopCartItems", userId, shopCartItemDtoList);
        return shopCartItemDtoList;
    }

    @Override
    public List<ShopCartItemDto> getShopCartExpiryItems(String userId) {
        return basketMapper.getShopCartExpiryItems(userId);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void cleanExpiryProdList(String userId) {
        basketMapper.cleanExpiryProdList(userId);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void updateBasketByShopCartParam(String userId, Map<Long, ShopCartParam> basketIdShopCartParamMap) {
        basketMapper.updateDiscountItemId(userId, basketIdShopCartParamMap);
    }

    @Override
    @CacheEvict(cacheNames = "ShopCartItems", key = "#userId")
    public void removeShopCartItemsCacheByUserId(String userId) {

    }

    @Override
    public List<String> listUserIdByProdId(Long prodId) {
        return basketMapper.listUserIdByProdId(prodId);
    }

    @Override
    public List<ShopCartDto> getShopCarts(List<ShopCartItemDto> shopCartItems) {
        // 根据店铺ID划分item
        Map<Long, List<ShopCartItemDto>> shopCartMap = shopCartItems.stream().collect(Collectors.groupingBy(ShopCartItemDto::getShopId));

        // 返回一个店铺的所有信息
        List<ShopCartDto> shopCartDtos = Lists.newArrayList();
        for (Long shopId : shopCartMap.keySet()) {

            //获取店铺的所有商品项
            List<ShopCartItemDto> shopCartItemDtoList = shopCartMap.get(shopId);

            // 构建每个店铺的购物车信息
            ShopCartDto shopCart = new ShopCartDto();

            //店铺信息
            shopCart.setShopId(shopId);
            shopCart.setShopName(shopCartItemDtoList.get(0).getShopName());

            applicationContext.publishEvent(new ShopCartEvent(shopCart, shopCartItemDtoList));

            shopCartDtos.add(shopCart);
        }

        return shopCartDtos;
    }

    @Override
    public List<ShopCartItemDto> getShopCartItemsByOrderItems(List<Long> basketId, OrderItemParam orderItem,String userId) {
        if (orderItem == null && CollectionUtil.isEmpty(basketId)) {
            return Collections.emptyList();
        }

        // 当立即购买时，没有提交的订单是没有购物车信息的
        if (CollectionUtil.isEmpty(basketId) && orderItem != null) {

            Sku sku = skuService.getSkuBySkuId(orderItem.getSkuId());
            if (sku == null) {
                throw new RuntimeException("订单包含无法识别的商品");
            }
            Product prod = productService.getProductByProdId(orderItem.getProdId());
            if (prod == null) {
                throw new RuntimeException("订单包含无法识别的商品");
            }

            // 拿到购物车的所有item
            ShopCartItemDto shopCartItemDto = new ShopCartItemDto();
            shopCartItemDto.setBasketId(-1L);
            shopCartItemDto.setSkuId(orderItem.getSkuId());
            shopCartItemDto.setProdCount(orderItem.getProdCount());
            shopCartItemDto.setProdId(orderItem.getProdId());
            shopCartItemDto.setSkuName(sku.getSkuName());
            shopCartItemDto.setPic(StrUtil.isBlank(sku.getPic())? prod.getPic() : sku.getPic());
            shopCartItemDto.setProdName(sku.getProdName());
            shopCartItemDto.setProductTotalAmount(Arith.mul(sku.getPrice(),orderItem.getProdCount()));
            shopCartItemDto.setPrice(sku.getPrice());
            shopCartItemDto.setDistributionCardNo(orderItem.getDistributionCardNo());
            shopCartItemDto.setBasketDate(new Date());
            ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(orderItem.getShopId());
            shopCartItemDto.setShopId(shopDetail.getShopId());
            shopCartItemDto.setShopName(shopDetail.getShopName());
            return Collections.singletonList(shopCartItemDto);
        }
        List<ShopCartItemDto> dbShopCartItems = getShopCartItems(userId);

        // 返回购物车选择的商品信息
        return dbShopCartItems
                .stream()
                .filter(shopCartItemDto -> basketId.contains(shopCartItemDto.getBasketId()))
                .collect(Collectors.toList());
    }


}

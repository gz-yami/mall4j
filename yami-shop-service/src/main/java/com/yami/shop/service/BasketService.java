/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.ShopCartDto;
import com.yami.shop.bean.app.dto.ShopCartItemDto;
import com.yami.shop.bean.app.param.ChangeShopCartParam;
import com.yami.shop.bean.app.param.OrderItemParam;
import com.yami.shop.bean.app.param.ShopCartParam;
import com.yami.shop.bean.model.Basket;

import java.util.List;
import java.util.Map;

/**
 * @author lgh on 2018/10/18.
 */
public interface BasketService extends IService<Basket> {

    /**
     * 根据购物车id列表删除购物项
     * @param userId 用户id
     * @param basketIds 购物车id列表
     */
    void deleteShopCartItemsByBasketIds(String userId, List<Long> basketIds);

    /**
     * 添加购物车
     * @param param
     * @param userId
     */
    void addShopCartItem(ChangeShopCartParam param, String userId);

    /**
     * 更新购物车
     * @param basket
     */
    void updateShopCartItem(Basket basket);

    /**
     * 删除所有购物车
     * @param userId
     */
    void deleteAllShopCartItems(String userId);

    /**
     * 根据用户id获取购物车列表
     * @param userId 用户id
     * @return 购物车列表
     */
    List<ShopCartItemDto> getShopCartItems(String userId);

    /**
     * 获取购物车失效列表
     * @param userId 用户id
     * @return 失效商品
     */
    List<ShopCartItemDto> getShopCartExpiryItems(String userId);

    /**
     * 清除失效的购物项
     * @param userId 用户id
     */
    void cleanExpiryProdList(String userId);

    /**
     * 更新满减活动id
     * @param userId 用户id
     * @param basketIdShopCartParamMap 购物车map
     */
    void updateBasketByShopCartParam(String userId, Map<Long, ShopCartParam> basketIdShopCartParamMap);

    /**
     * 删除购物车缓存
     * @param userId
     */
    void removeShopCartItemsCacheByUserId(String userId);

    /**
     * 获取购物车中拥有某件商品的用户，用于清除该用户购物车的缓存
     * @param prodId 商品id
     * @return 用户id
     */
    List<String> listUserIdByProdId(Long prodId);

    /**
     * 根据店铺组装购车中的商品信息，返回每个店铺中的购物车商品信息
     * @param shopCartItems 指定的购物项目
     * @return 每个店铺的购物车信息
     */
    List<ShopCartDto> getShopCarts(List<ShopCartItemDto> shopCartItems);

    /**
     * 组装获取用户提交的购物车商品项
     * @param orderItem 提交订单时携带的商品信息
     * @param userId 当前用户的用户id
     * @param basketId 购物车id
     * @return 所有的商品购物项
     */
    List<ShopCartItemDto> getShopCartItemsByOrderItems(List<Long> basketId, OrderItemParam orderItem,String userId);

}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.listener;

import com.google.common.collect.Lists;
import com.yami.shop.bean.app.dto.ShopCartDto;
import com.yami.shop.bean.app.dto.ShopCartItemDiscountDto;
import com.yami.shop.bean.app.dto.ShopCartItemDto;
import com.yami.shop.bean.event.ShopCartEvent;
import com.yami.shop.bean.order.ShopCartEventOrder;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认的购物车链进行组装时的操作
 * @author LGH
 */
@Component("defaultShopCartListener")
public class ShopCartListener {

    /**
     * 将店铺下的所有商品归属到该店铺的购物车当中
     * @param event#getShopCart() 购物车
     * @param event#shopCartItemDtoList 该购物车的商品
     * @return 是否继续组装
     */
    @EventListener(ShopCartEvent.class)
    @Order(ShopCartEventOrder.DEFAULT)
    public void defaultShopCartEvent(ShopCartEvent event) {
        ShopCartDto shopCart = event.getShopCartDto();
        List<ShopCartItemDto> shopCartItemDtoList = event.getShopCartItemDtoList();
        // 对数据进行组装
        List<ShopCartItemDiscountDto> shopCartItemDiscountDtoList = Lists.newArrayList();
        ShopCartItemDiscountDto shopCartItemDiscountDto = new ShopCartItemDiscountDto();

        shopCartItemDiscountDto.setShopCartItems(shopCartItemDtoList);
        shopCartItemDiscountDtoList.add(shopCartItemDiscountDto);

        shopCart.setShopCartItemDiscounts(shopCartItemDiscountDtoList);
    }

}

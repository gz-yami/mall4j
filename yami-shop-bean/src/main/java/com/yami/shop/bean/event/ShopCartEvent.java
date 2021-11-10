/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.event;

import com.yami.shop.bean.app.dto.ShopCartDto;
import com.yami.shop.bean.app.dto.ShopCartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 购物车商品发生改变时的事件
 * @author LGH
 */
@Data
@AllArgsConstructor
public class ShopCartEvent {

    /**
     * 将要组装的单个店铺的店铺信息
     */
    private ShopCartDto shopCartDto;

    /**
     * 该店铺下的所有商品信息
     */
    private List<ShopCartItemDto> shopCartItemDtoList;
}

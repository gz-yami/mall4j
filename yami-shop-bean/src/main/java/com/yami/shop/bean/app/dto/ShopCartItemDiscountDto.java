/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lanhai
 */
@Data
public class ShopCartItemDiscountDto implements Serializable {


    @Schema(description = "已选满减项" , required = true)
    private ChooseDiscountItemDto chooseDiscountItemDto;

    @Schema(description = "商品列表" )
    private List<ShopCartItemDto> shopCartItems;
}

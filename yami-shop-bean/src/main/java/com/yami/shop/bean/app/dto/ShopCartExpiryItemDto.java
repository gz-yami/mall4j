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

import java.util.List;

/**
 * @author lanhai
 */
@Data
@Schema(description = "购物车失效商品对象")
public class ShopCartExpiryItemDto {
    @Schema(description = "店铺ID" , required = true)
    private Long shopId;

    @Schema(description = "店铺名称" , required = true)
    private String shopName;

    @Schema(description = "商品项" , required = true)
    private List<ShopCartItemDto> shopCartItemDtoList;

}

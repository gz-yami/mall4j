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
public class ShopCartDto implements Serializable {

	@Schema(description = "店铺ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private Long shopId;

	@Schema(description = "店铺名称" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String shopName;

	@Schema(description = "购物车商品" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private List<ShopCartItemDiscountDto> shopCartItemDiscounts;


}

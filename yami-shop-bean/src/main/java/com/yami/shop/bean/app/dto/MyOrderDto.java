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
@Schema(description = "我的订单")
public class MyOrderDto {
	
	@Schema(description = "订单项" ,required=true)
	private List<MyOrderItemDto> orderItemDtos;
	
	@Schema(description = "订单号" ,required=true)
	private String orderNumber;
	
	@Schema(description = "总价" ,required=true)
	private Double actualTotal;
	
	@Schema(description = "订单状态" ,required=true)
	private Integer status;
	
}

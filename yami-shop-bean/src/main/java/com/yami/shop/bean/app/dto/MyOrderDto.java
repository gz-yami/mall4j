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

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("我的订单")
public class MyOrderDto {
	
	@ApiModelProperty(value = "订单项",required=true)
	private List<MyOrderItemDto> orderItemDtos;
	
	@ApiModelProperty(value = "订单号",required=true)
	private String orderNumber;
	
	@ApiModelProperty(value = "总价",required=true)
	private Double actualTotal;
	
	@ApiModelProperty(value = "订单状态",required=true)
	private Integer status;
	
}

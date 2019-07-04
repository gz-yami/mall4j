/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class DeliveryOrderParam {
	
	@NotBlank(message="订单号不能为空")
	@ApiModelProperty(value = "订单号",required=true)
	private String orderNumber;
	
	@NotBlank(message="快递公司id不能为空")
	@ApiModelProperty(value = "快递公司",required=true)
	private Long dvyId;
	
	@NotBlank(message="物流单号不能为空")
	@ApiModelProperty(value = "物流单号",required=true)
	private String dvyFlowId;


	public Long getDvyId() {
		return dvyId;
	}

	public void setDvyId(Long dvyId) {
		this.dvyId = dvyId;
	}

	public String getDvyFlowId() {
		return dvyFlowId;
	}

	public void setDvyFlowId(String dvyFlowId) {
		this.dvyFlowId = dvyFlowId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
}

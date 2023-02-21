/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * @author lanhai
 */
public class DeliveryOrderParam {
	
	@NotBlank(message="订单号不能为空")
	@Schema(description = "订单号" ,required=true)
	private String orderNumber;
	
	@NotBlank(message="快递公司id不能为空")
	@Schema(description = "快递公司" ,required=true)
	private Long dvyId;
	
	@NotBlank(message="物流单号不能为空")
	@Schema(description = "物流单号" ,required=true)
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

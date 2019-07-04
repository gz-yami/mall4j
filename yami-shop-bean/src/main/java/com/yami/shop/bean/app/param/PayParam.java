/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value= "支付参数")
public class PayParam {

	/**
	  * 订单号
	 */
	@NotBlank(message="订单号不能为空")
	@ApiModelProperty(value = "订单号",required=true)
	private String orderNumbers;

	/**
	 * 支付方式
	 */
	@NotNull(message="支付方式不能为空")
	@ApiModelProperty(value = "支付方式 (1:微信支付 2:支付宝)",required=true)
	private Integer payType;

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getOrderNumbers() {
		return orderNumbers;
	}

	public void setOrderNumbers(String orderNumbers) {
		this.orderNumbers = orderNumbers;
	}

}

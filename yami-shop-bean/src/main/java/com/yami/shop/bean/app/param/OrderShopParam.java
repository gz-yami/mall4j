/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author lanhai
 */
public class OrderShopParam {

	/** 店铺ID **/
	@Schema(description = "店铺id" ,required=true)
	private Long shopId;
	
	/**
	 * 订单备注信息
	 */
	@Schema(description = "订单备注信息" ,required=true)
	private String remarks;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}

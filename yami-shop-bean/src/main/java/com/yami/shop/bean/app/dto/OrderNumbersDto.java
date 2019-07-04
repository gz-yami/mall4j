/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderNumbersDto {

	@ApiModelProperty(value = "多个订单号拼接的字符串",required=true)
	private String orderNumbers;

	public OrderNumbersDto(String orderNumbers) {
		this.orderNumbers = orderNumbers;
	}
}

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
import lombok.Data;

import java.util.List;

/**
 * @author lanhai
 */
@Data
@Schema(description = "提交订单参数")
public class SubmitOrderParam {
	@Schema(description = "每个店铺提交的订单信息" ,required=true)
	private List<OrderShopParam> orderShopParam;
}

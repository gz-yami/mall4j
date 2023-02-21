/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.OrderSettlement;

/**
 *
 * @author lgh on 2018/11/10.
 */
public interface OrderSettlementService extends IService<OrderSettlement> {

	/**
	 * 根据内部订单号更新order settlement
	 * @param outTradeNo 外部单号
	 * @param transactionId 支付单号
	 */
	void updateSettlementsByPayNo(String outTradeNo, String transactionId);
}

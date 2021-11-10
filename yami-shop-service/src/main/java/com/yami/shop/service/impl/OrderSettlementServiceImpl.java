/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.OrderSettlement;
import com.yami.shop.dao.OrderSettlementMapper;
import com.yami.shop.service.OrderSettlementService;

/**
 *
 * @author lgh on 2018/11/10.
 */
@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService {

    @Autowired
    private OrderSettlementMapper orderSettlementMapper;

	@Override
	public void updateSettlementsByPayNo(String outTradeNo, String transactionId) {
		orderSettlementMapper.updateSettlementsByPayNo(outTradeNo, transactionId);
	}

}

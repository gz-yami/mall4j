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
import com.yami.shop.bean.model.OrderItem;

import java.util.List;

/**
 *
 * @author lgh on 2018/09/15.
 */
public interface OrderItemService extends IService<OrderItem> {

	/**
	 * 根据订单编号获取订单项
	 * @param orderNumber
	 * @return
	 */
	List<OrderItem> getOrderItemsByOrderNumber(String orderNumber);

}

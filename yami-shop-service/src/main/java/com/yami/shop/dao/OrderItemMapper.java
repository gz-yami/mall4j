/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

	/**
	 * 根据订单编号获取订单项
	 * @param orderNumber
	 * @return
	 */
	List<OrderItem> listByOrderNumber(@Param("orderNumber") String orderNumber);

	/**
	 * 插入订单项
	 * @param orderItems
	 */
	void insertBatch(List<OrderItem> orderItems);

}
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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.MyOrderDto;
import com.yami.shop.bean.model.Order;

/**
 * 我的订单
 * @author lgh
 */
public interface MyOrderService extends IService<Order> {

	/**
	 * 通过用户id和订单状态分页获取订单信息
	 * @param page   分页参数
	 * @param userId 用户id
	 * @param status 订单状态
	 * @return
	 */
	IPage<MyOrderDto> pageMyOrderByUserIdAndStatus(Page<MyOrderDto> page, String userId, Integer status);
}

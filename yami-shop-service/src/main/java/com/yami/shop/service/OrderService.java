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

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.OrderCountData;
import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.param.OrderParam;

import cn.hutool.core.date.DateTime;

/**
 * @author lgh on 2018/09/15.
 */
public interface OrderService extends IService<Order> {


    Order getOrderByOrderNumber(String orderNumber);

    ShopCartOrderMergerDto putConfirmOrderCache(String userId ,ShopCartOrderMergerDto shopCartOrderMergerDto);

    ShopCartOrderMergerDto getConfirmOrderCache(String userId);

    void removeConfirmOrderCache(String userId);

    List<Order> submit(String userId, ShopCartOrderMergerDto mergerOrder);

    void delivery(Order order);

    List<Order> listOrderAndOrderItems(Integer orderStatus, DateTime lessThanUpdateTime);

    void cancelOrders(List<Order> orders);

    void confirmOrder(List<Order> orders);

    List<Order> listOrdersDetialByOrder(Order order, Date startTime, Date endTime);

    IPage<Order> pageOrdersDetialByOrderParam(Page<Order> page, OrderParam orderParam);

    void deleteOrders(List<Order> orders);

    OrderCountData getOrderCount(String userId);
}

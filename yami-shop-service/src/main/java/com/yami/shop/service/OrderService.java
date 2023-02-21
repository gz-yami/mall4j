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

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.OrderCountData;
import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.param.OrderParam;

import java.util.Date;
import java.util.List;

/**
 * @author lgh on 2018/09/15.
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据订单编号获取订单
     * @param orderNumber
     * @return
     */
    Order getOrderByOrderNumber(String orderNumber);

    /**
     * 新增订单缓存
     * @param userId
     * @param shopCartOrderMergerDto
     * @return
     */
    ShopCartOrderMergerDto putConfirmOrderCache(String userId ,ShopCartOrderMergerDto shopCartOrderMergerDto);

    /**
     * 根据用户id获取订单缓存
     * @param userId
     * @return
     */
    ShopCartOrderMergerDto getConfirmOrderCache(String userId);

    /**
     * 根据用户id删除订单缓存
     * @param userId
     */
    void removeConfirmOrderCache(String userId);

    /**
     * 提交订单
     * @param userId
     * @param mergerOrder
     * @return
     */
    List<Order> submit(String userId, ShopCartOrderMergerDto mergerOrder);

    /**
     * 发货
     * @param order
     */
    void delivery(Order order);

    /**
     * 根据参数获取订单列表
     * @param orderStatus 订单状态
     * @param lessThanUpdateTime 更新时间
     * @return
     */
    List<Order> listOrderAndOrderItems(Integer orderStatus, DateTime lessThanUpdateTime);

    /**
     * 取消订单
     * @param orders
     */
    void cancelOrders(List<Order> orders);

    /**
     * 订单确认收货
     * @param orders
     */
    void confirmOrder(List<Order> orders);

    /**
     * 根据参数获取订单列表
     * @param order 订单参数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<Order> listOrdersDetailByOrder(Order order, Date startTime, Date endTime);

    /**
     * 根据参数分页获取订单
     * @param page
     * @param orderParam
     * @return
     */
    IPage<Order> pageOrdersDetailByOrderParam(Page<Order> page, OrderParam orderParam);

    /**
     * 删除订单
     * @param orders
     */
    void deleteOrders(List<Order> orders);

    /**
     * 根据用户id获取各个状态的订单数目
     * @param userId
     * @return
     */
    OrderCountData getOrderCount(String userId);
}

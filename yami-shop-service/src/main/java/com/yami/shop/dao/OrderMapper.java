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

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.app.dto.MyOrderDto;
import com.yami.shop.bean.app.dto.OrderCountData;
import com.yami.shop.bean.distribution.UserShoppingDataDto;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.common.util.PageAdapter;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author lanhai
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单编号获取订单
     * @param orderNumber
     * @return
     */
    Order getOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 根据参数获取订单
     * @param orderStatus 订单状态
     * @param lessThanUpdateTime 更新时间
     * @return
     */
    List<Order> listOrderAndOrderItems(@Param("orderStatus") Integer orderStatus, @Param("lessThanUpdateTime") DateTime lessThanUpdateTime);

    /**
     * 取消订单
     * @param orders
     */
    void cancelOrders(@Param("orders") List<Order> orders);

    /**
     * 订单确认收货
     * @param orders
     */
    void confirmOrder(@Param("orders") List<Order> orders);

    /**
     * 更新订单为支付成功
     * @param orderNumbers 订单编号列表
     * @param payType 支付类型
     */
    void updateByToPaySuccess(@Param("orderNumbers") List<String> orderNumbers, @Param("payType") Integer payType);

    /**
     * 根据参数和时间获取订单列表
     * @param order 参数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<Order> listOrdersDetailByOrder(@Param("order") Order order, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据分页参数和订单参数获取订单列表
     * @param adapter 分页参数
     * @param orderParam 订单参数
     * @return
     */
    List<Order> listOrdersDetailByOrderParam(@Param("adapter") PageAdapter adapter, @Param("orderParam") OrderParam orderParam);

    /**
     * 根据订单参数获取订单数
     * @param orderParam
     * @return
     */
    Long countOrderDetail(@Param("orderParam") OrderParam orderParam);

    /**
     * 根据店铺id和用户id获取购买过的商品id列表
     * @param shopId
     * @param userId
     * @return
     */
    List<Long> listBoughtProdByUserIdAndShopId(@Param("shopId") Long shopId, @Param("userId") String userId);

    /**
     * 根据参数计算用户消费信息
     * @param shopId
     * @param userId
     * @return
     */
    UserShoppingDataDto calculateUserInShopData(@Param("shopId") Long shopId, @Param("userId") String userId);

    /**
     * 根据用户id和订单状态获取订单列表
     * @param adapter
     * @param userId
     * @param status
     * @return
     */
    List<MyOrderDto> listMyOrderByUserIdAndStatus(@Param("adapter") PageAdapter adapter, @Param("userId") String userId, @Param("status") Integer status);

    /**
     * 根据用户id和订单状态获取订单数量
     * @param userId
     * @param status
     * @return
     */
    Long countMyOrderByUserIdAndStatus(@Param("userId") String userId, @Param("status") Integer status);

    /**
     * 删除订单
     * @param orders
     */
    void deleteOrders(@Param("orders") List<Order> orders);

    /**
     * 根据用户id获取各个状态的订单数量
     * @param userId
     * @return
     */
    OrderCountData getOrderCount(String userId);

}

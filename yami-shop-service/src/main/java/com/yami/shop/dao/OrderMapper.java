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

public interface OrderMapper extends BaseMapper<Order> {

    Order getOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    List<Order> listOrderAndOrderItems(@Param("orderStatus") Integer orderStatus, @Param("lessThanUpdateTime") DateTime lessThanUpdateTime);

    void cancelOrders(@Param("orders") List<Order> orders);

    void confirmOrder(@Param("orders") List<Order> orders);

    void updateByToPaySuccess(@Param("orderNumbers") List<String> orderNumbers, @Param("payType") Integer payType);

    List<Order> listOrdersDetialByOrder(@Param("order") Order order, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Order> listOrdersDetialByOrderParam(@Param("adapter") PageAdapter adapter, @Param("orderParam") OrderParam orderParam);

    Long countOrderDetial(@Param("orderParam") OrderParam orderParam);

    List<Long> listBoughtProdByUserIdAndShopId(@Param("shopId") Long shopId, @Param("userId") String userId);

    UserShoppingDataDto calculateUserInShopData(@Param("shopId") Long shopId, @Param("userId") String userId);

    List<MyOrderDto> listMyOrderByUserIdAndStatus(@Param("adapter") PageAdapter adapter, @Param("userId") String userId, @Param("status") Integer status);

    Long countMyOrderByUserIdAndStatus(@Param("userId") String userId, @Param("status") Integer status);

    void deleteOrders(@Param("orders") List<Order> orders);

    OrderCountData getOrderCount(String userId);

}

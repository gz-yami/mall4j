/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.*;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.model.UserAddrOrder;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Arith;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import cn.hutool.core.bean.BeanUtil;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/myOrder")
@Tag(name = "我的订单接口")
@AllArgsConstructor
public class MyOrderController {

    private final OrderService orderService;

    

    private final UserAddrOrderService userAddrOrderService;

    private final ProductService productService;

    private final SkuService skuService;

    private final MyOrderService myOrderService;

    private final ShopDetailService shopDetailService;

    private final OrderItemService orderItemService;


    /**
     * 订单详情信息接口
     */
    @GetMapping("/orderDetail")
    @Operation(summary = "订单详情信息" , description = "根据订单号获取订单详情信息")
    @Parameter(name = "orderNumber", description = "订单号" , required = true)
    public ServerResponseEntity<OrderShopDto> orderDetail(@RequestParam(value = "orderNumber", required = true) String orderNumber) {

        String userId = SecurityUtils.getUser().getUserId();
        OrderShopDto orderShopDto = new OrderShopDto();

        Order order = orderService.getOrderByOrderNumber(orderNumber);

        if (order == null) {
            throw new RuntimeException("该订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new RuntimeException("你没有权限获取该订单信息");
        }

        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(order.getShopId());
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        UserAddrDto userAddrDto = BeanUtil.copyProperties(userAddrOrder, UserAddrDto.class);
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        List<OrderItemDto> orderItemDtos = BeanUtil.copyToList(orderItems, OrderItemDto.class);

        orderShopDto.setShopId(shopDetail.getShopId());
        orderShopDto.setShopName(shopDetail.getShopName());
        orderShopDto.setActualTotal(order.getActualTotal());
        orderShopDto.setUserAddrDto(userAddrDto);
        orderShopDto.setOrderItemDtos(orderItemDtos);
        orderShopDto.setTransfee(order.getFreightAmount());
        orderShopDto.setReduceAmount(order.getReduceAmount());
        orderShopDto.setCreateTime(order.getCreateTime());
        orderShopDto.setRemarks(order.getRemarks());
        orderShopDto.setStatus(order.getStatus());

        double total = 0.0;
        Integer totalNum = 0;
        for (OrderItemDto orderItem : orderShopDto.getOrderItemDtos()) {
            total = Arith.add(total, orderItem.getProductTotalAmount());
            totalNum += orderItem.getProdCount();
        }
        orderShopDto.setTotal(total);
        orderShopDto.setTotalNum(totalNum);

        return ServerResponseEntity.success(orderShopDto);
    }


    /**
     * 订单列表接口
     */
    @GetMapping("/myOrder")
    @Operation(summary = "订单列表信息" , description = "根据订单状态获取订单列表信息，状态为0时获取所有订单")
    @Parameters({
            @Parameter(name = "status", description = "订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败")
    })
    public ServerResponseEntity<IPage<MyOrderDto>> myOrder(@RequestParam(value = "status") Integer status,PageParam<MyOrderDto> page) {

        String userId = SecurityUtils.getUser().getUserId();
        IPage<MyOrderDto> myOrderDtoIpage = myOrderService.pageMyOrderByUserIdAndStatus(page, userId, status);
        return ServerResponseEntity.success(myOrderDtoIpage);
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderNumber}")
    @Operation(summary = "根据订单号取消订单" , description = "根据订单号取消订单")
    @Parameter(name = "orderNumber", description = "订单号" , required = true)
    public ServerResponseEntity<String> cancel(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new YamiShopBindException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.UNPAY.value())) {
            throw new YamiShopBindException("订单已支付，无法取消订单");
        }
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        order.setOrderItems(orderItems);
        // 取消订单
        orderService.cancelOrders(Arrays.asList(order));

        // 清除缓存
        for (OrderItem orderItem : orderItems) {
            productService.removeProductCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
        }
        return ServerResponseEntity.success();
    }


    /**
     * 确认收货
     */
    @PutMapping("/receipt/{orderNumber}")
    @Operation(summary = "根据订单号确认收货" , description = "根据订单号确认收货")
    public ServerResponseEntity<String> receipt(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new YamiShopBindException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.CONSIGNMENT.value())) {
            throw new YamiShopBindException("订单未发货，无法确认收货");
        }
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        order.setOrderItems(orderItems);
        // 确认收货
        orderService.confirmOrder(Arrays.asList(order));

        for (OrderItem orderItem : orderItems) {
            productService.removeProductCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{orderNumber}")
    @Operation(summary = "根据订单号删除订单" , description = "根据订单号删除订单")
    @Parameter(name = "orderNumber", description = "订单号" , required = true)
    public ServerResponseEntity<String> delete(@PathVariable("orderNumber") String orderNumber) {
        String userId = SecurityUtils.getUser().getUserId();

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new YamiShopBindException("该订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new YamiShopBindException("你没有权限获取该订单信息");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value()) ) {
            throw new YamiShopBindException("订单未完成或未关闭，无法删除订单");
        }

        // 删除订单
        orderService.deleteOrders(Arrays.asList(order));

        return ServerResponseEntity.success("删除成功");
    }

    /**
     * 获取我的订单订单数量
     */
    @GetMapping("/orderCount")
    @Operation(summary = "获取我的订单订单数量" , description = "获取我的订单订单数量")
    public ServerResponseEntity<OrderCountData> getOrderCount() {
        String userId = SecurityUtils.getUser().getUserId();
        OrderCountData orderCountMap = orderService.getOrderCount(userId);
        return ServerResponseEntity.success(orderCountMap);
    }


}

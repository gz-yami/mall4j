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

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yami.shop.bean.app.dto.DeliveryDto;
import com.yami.shop.bean.model.Delivery;
import com.yami.shop.bean.model.Order;
import com.yami.shop.common.util.Json;
import com.yami.shop.service.DeliveryService;

import cn.hutool.http.HttpUtil;

import java.util.Objects;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/delivery")
@Tag(name = "查看物流接口")
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private OrderService orderService;

    /**
     * 查看物流接口
     */
    @GetMapping("/check")
    @Operation(summary = "查看物流" , description = "根据订单号查看物流")
    @Parameter(name = "orderNumber", description = "订单号" , required = true)
    public ServerResponseEntity<DeliveryDto> checkDelivery(@RequestParam("orderNumber") String orderNumber) {
    	String userId = SecurityUtils.getUser().getUserId();
    	Order order = orderService.getOrderByOrderNumber(orderNumber);
    	if (order == null || !Objects.equals(order.getUserId(), userId)) {
    		throw new YamiShopBindException("订单不存在");
    	}
    	if (order.getDvyId() == null) {
    		throw new YamiShopBindException("订单暂无物流信息");
    	}
    	Delivery delivery = deliveryService.getById(order.getDvyId());
    	if (delivery == null || StrUtil.isBlank(delivery.getQueryUrl())) {
    		throw new YamiShopBindException("物流公司信息不存在");
    	}
    	String dvyFlowId = order.getDvyFlowId() == null ? "" : order.getDvyFlowId();
    	String url = delivery.getQueryUrl().replace("{dvyFlowId}", dvyFlowId);
    	String deliveryJson = HttpUtil.get(url);

    	DeliveryDto deliveryDto = Json.parseObject(deliveryJson, DeliveryDto.class);
    	if (deliveryDto == null) {
    		throw new YamiShopBindException("物流信息查询失败");
    	}
    	deliveryDto.setDvyFlowId(order.getDvyFlowId());
    	deliveryDto.setCompanyHomeUrl(delivery.getCompanyHomeUrl());
    	deliveryDto.setCompanyName(delivery.getDvyName());
        return ServerResponseEntity.success(deliveryDto);
    }
}

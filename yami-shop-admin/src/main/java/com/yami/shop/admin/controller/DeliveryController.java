/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yami.shop.bean.model.Delivery;
import com.yami.shop.service.DeliveryService;

/**
 *
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/admin/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

	/**
	 * 分页获取
	 */
    @GetMapping("/list")
	public ServerResponseEntity<List<Delivery>> page(){
		
		List<Delivery> list = deliveryService.list();
		return ServerResponseEntity.success(list);
	}

}

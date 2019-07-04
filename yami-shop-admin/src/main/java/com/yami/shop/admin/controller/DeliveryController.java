/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Delivery>> page(){
		
		List<Delivery> list = deliveryService.list();
		return ResponseEntity.ok(list);
	}

}

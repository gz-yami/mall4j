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

import java.util.List;

import com.yami.shop.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yami.shop.bean.model.Area;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

/**
 *
 * @author lgh on 2018/10/26.
 */
@RestController
@RequestMapping("/p/area")
@Tag(name = "省市区接口")
public class AreaController {

    @Autowired
    private AreaService areaService;
    
    /**
	 * 分页获取
	 */
    @GetMapping("/listByPid")
    @Operation(summary = "获取省市区信息" , description = "根据省市区的pid获取地址信息")
    @Parameter(name = "pid", description = "省市区的pid(pid为0获取所有省份)" , required = true)
	public ServerResponseEntity<List<Area>> listByPid(Long pid){
		List<Area> list = areaService.listByPid(pid);
		return ServerResponseEntity.success(list);
	}
    
}

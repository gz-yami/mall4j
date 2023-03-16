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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.PickAddr;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.PickAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Objects;



/**
 *
 * @author lgh on 2018/10/17.
 */
@RestController
@RequestMapping("/shop/pickAddr")
public class PickAddrController {

    @Autowired
    private PickAddrService pickAddrService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:page')")
	public ServerResponseEntity<IPage<PickAddr>> page(PickAddr pickAddr,PageParam<PickAddr> page){
		IPage<PickAddr> pickAddrs = pickAddrService.page(page,new LambdaQueryWrapper<PickAddr>()
													.like(StrUtil.isNotBlank(pickAddr.getAddrName()),PickAddr::getAddrName,pickAddr.getAddrName())
													.orderByDesc(PickAddr::getAddrId));
		return ServerResponseEntity.success(pickAddrs);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:info')")
	public ServerResponseEntity<PickAddr> info(@PathVariable("id") Long id){
		PickAddr pickAddr = pickAddrService.getById(id);
		return ServerResponseEntity.success(pickAddr);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:save')")
	public ServerResponseEntity<Void> save(@Valid @RequestBody PickAddr pickAddr){
		pickAddr.setShopId(SecurityUtils.getSysUser().getShopId());
		pickAddrService.save(pickAddr);
		return ServerResponseEntity.success();
	}

	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:update')")
	public ServerResponseEntity<Void> update(@Valid @RequestBody PickAddr pickAddr){
		PickAddr dbPickAddr = pickAddrService.getById(pickAddr.getAddrId());

		if (!Objects.equals(dbPickAddr.getShopId(),SecurityUtils.getSysUser().getShopId())) {
			throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED);
		}
		pickAddrService.updateById(pickAddr);
		return ServerResponseEntity.success();
	}

	/**
	 * 删除
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:delete')")
	public ServerResponseEntity<Void> delete(@RequestBody Long[] ids){
		pickAddrService.removeByIds(Arrays.asList(ids));
		return ServerResponseEntity.success();
	}
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.controller;


import jakarta.validation.Valid;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.sys.model.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.yami.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.sys.service.SysConfigService;
import com.yami.shop.common.annotation.SysLog;

import cn.hutool.core.util.StrUtil;



/**
 * 系统配置信息
 * @author lgh
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController{
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有配置列表
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('sys:config:page')")
	public ServerResponseEntity<IPage<SysConfig>> page(String paramKey,PageParam<SysConfig> page){
		IPage<SysConfig> sysConfigs = sysConfigService.page(page, new LambdaQueryWrapper<SysConfig>().like(StrUtil.isNotBlank(paramKey),SysConfig::getParamKey,paramKey));
		return ServerResponseEntity.success(sysConfigs);
	}


	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('sys:config:info')")
	public ServerResponseEntity<SysConfig> info(@PathVariable("id") Long id){
		SysConfig config = sysConfigService.getById(id);
		return ServerResponseEntity.success(config);
	}

	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys:config:save')")
	public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig config){
		sysConfigService.save(config);
		return ServerResponseEntity.success();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys:config:update')")
	public ServerResponseEntity<Void> update(@RequestBody @Valid SysConfig config){
		sysConfigService.updateById(config);
		return ServerResponseEntity.success();
	}

	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('sys:config:delete')")
	public ServerResponseEntity<Void> delete(@RequestBody Long[] configIds){
		sysConfigService.deleteBatch(configIds);
		return ServerResponseEntity.success();
	}

}

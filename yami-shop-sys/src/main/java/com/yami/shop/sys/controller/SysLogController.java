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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.sys.model.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.yami.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.sys.service.SysLogService;

import cn.hutool.core.util.StrUtil;




/**
 * 系统日志
 * @author lgh
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('sys:log:page')")
	public ServerResponseEntity<IPage<SysLog>> page(SysLog sysLog,PageParam<SysLog> page){
		IPage<SysLog> sysLogs = sysLogService.page(page,
				new LambdaQueryWrapper<SysLog>()
						.like(StrUtil.isNotBlank(sysLog.getUsername()),SysLog::getUsername, sysLog.getUsername())
						.like(StrUtil.isNotBlank(sysLog.getOperation()), SysLog::getOperation,sysLog.getOperation()));
		return ServerResponseEntity.success(sysLogs);
	}
	
}

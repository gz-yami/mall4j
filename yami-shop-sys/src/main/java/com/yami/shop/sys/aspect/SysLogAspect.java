/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;


import com.yami.shop.common.util.IPHelper;
import com.yami.shop.security.util.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yami.shop.sys.service.SysLogService;
import com.yami.shop.sys.model.SysLog;
import com.yami.shop.common.util.Json;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;

/**
 * @author lgh
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;
	private static Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint joinPoint,com.yami.shop.common.annotation.SysLog sysLog) throws Throwable {
		long beginTime = SystemClock.now();
		//执行方法
		Object result = joinPoint.proceed();
		//执行时长(毫秒)
		long time = SystemClock.now() - beginTime;


		SysLog sysLogEntity = new SysLog();
		if(sysLog != null){
			//注解上的描述
			sysLogEntity.setOperation(sysLog.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		sysLogEntity.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		String params = Json.toJsonString(args[0]);
		sysLogEntity.setParams(params);

		//设置IP地址
		sysLogEntity.setIp(IPHelper.getIpAddr());

		//用户名
		String username = SecurityUtils.getSysUser().getUsername();
		sysLogEntity.setUsername(username);

		sysLogEntity.setTime(time);
		sysLogEntity.setCreateDate(new Date());
		//保存系统日志
		sysLogService.save(sysLogEntity);


		return result;
	}

}

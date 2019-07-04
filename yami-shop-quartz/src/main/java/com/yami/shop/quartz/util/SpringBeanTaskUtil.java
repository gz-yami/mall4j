/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.util.SpringContextUtils;
import com.yami.shop.quartz.model.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


/**
 * 定时任务spring bean 执行定时任务
 * @author LGH
 */
@Slf4j
public class SpringBeanTaskUtil {
	
	public static void invokeMethod(ScheduleJob scheduleJob) {
		Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
		try {
			if (StrUtil.isNotEmpty(scheduleJob.getParams())) {
				Method method = target.getClass().getDeclaredMethod(scheduleJob.getMethodName(), String.class);
				ReflectionUtils.makeAccessible(method);
				method.invoke(target, scheduleJob.getParams());
			} else {
				Method method = target.getClass().getDeclaredMethod(scheduleJob.getMethodName());
				ReflectionUtils.makeAccessible(method);
				method.invoke(target);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行定时任务失败", e);
		}
	}
}

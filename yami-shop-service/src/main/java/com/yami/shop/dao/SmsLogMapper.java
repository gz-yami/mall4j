/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import org.apache.ibatis.annotations.Param;

import com.yami.shop.bean.model.SmsLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lanhai
 */
public interface SmsLogMapper extends BaseMapper<SmsLog> {
	/**
	 * 根据手机号和短信类型失效短信
	 * @param mobile
	 * @param type
	 */
	void invalidSmsByMobileAndType(@Param("mobile") String mobile, @Param("type") Integer type);
}
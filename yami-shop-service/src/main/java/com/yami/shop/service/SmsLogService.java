/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.enums.SmsType;
import com.yami.shop.bean.model.SmsLog;

import java.util.Map;

/**
 *
 * @author lgh on 2018/11/29.
 */
public interface SmsLogService extends IService<SmsLog> {
	/**
	 * 发送短信
	 * @param smsType 短信类型
	 * @param userId 用户id
	 * @param mobile 手机号
	 * @param params 内容
	 */
	void sendSms(SmsType smsType, String userId, String mobile, Map<String, String> params);
}

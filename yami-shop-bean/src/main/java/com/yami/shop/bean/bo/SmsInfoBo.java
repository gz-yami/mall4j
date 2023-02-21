/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.bo;

import com.yami.shop.bean.enums.SmsType;

import java.util.Map;

/**
 * @author lanhai
 */
public class SmsInfoBo {
	
	private SmsType smsType;
	
	private String userId;
	
	private String mobile;
	
	private Map<String, String> params;

	
	
	
	public SmsInfoBo(SmsType smsType, String userId, String mobile, Map<String, String> params) {
		this.smsType = smsType;
		this.userId = userId;
		this.mobile = mobile;
		this.params = params;
	}

	public SmsType getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsType smsType) {
		this.smsType = smsType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.enums;

/**
 * @author lanhai
 */
public enum MessageStatus {

	/**
	 * 小程序
	 */
	CANCEL(0),
	RELEASE(1);
	
	private Integer num;
	
	public Integer value() {
		return num;
	}
	
	MessageStatus(Integer num){
		this.num = num;
	}
}

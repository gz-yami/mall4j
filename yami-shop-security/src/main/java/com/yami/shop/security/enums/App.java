/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.enums;

public enum App {

	/**
	 * 小程序
	 */
	MINI(1),

	/**
	 * 微信公众号
	 */
	MP(2)
	;
	
	private Integer num;
	
	public Integer value() {
		return num;
	}
	
	App(Integer num){
		this.num = num;
	}
	
	public static App instance(Integer value) {
		App[] enums = values();
		for (App statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}

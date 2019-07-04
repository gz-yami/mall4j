/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.enums;

/**
 * 满减规则
 *
 */
public enum DiscountRule {

	/**
	 * 满钱减钱
	 * M money
	 */
	M2M(0),
	/**
	 * 满件减钱
	 * P piece
	 */
	P2M(1),
	/**
	 * 满钱打折
	 * Discount
	 */
	M2D(2),
	/**
	 * 满件打折
	 * Discount
	 */
	P2D(3)
	;
	
	private Integer num;
	
	public Integer value() {
		return num;
	}
	
	DiscountRule(Integer num){
		this.num = num;
	}
	
	public static DiscountRule instance(Integer value) {
		DiscountRule[] enums = values();
		for (DiscountRule statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.order;

/**
 * 提交订单事件先后顺序
 * @author LGH
 */
public interface SubmitOrderOrder {

    /**
     * 没有任何活动时的顺序
     */
    int DEFAULT = 0;

    /**
     * 优惠券，排在DEFAULT后面
     */
    int DISCOUNT = 100;

    /**
     * 优惠券，排在DEFAULT后面
     */
    int COUPON = 200;
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lanhai
 */
@Data
public class OrderParam {
    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单状态 -1 已取消 0:待付款 1:待发货 2:待收货 3:已完成
     */
    private Integer status;

    /**
     * 是否已经支付，1：已经支付过，0：，没有支付过
     */
    private Integer isPayed;

    /**
     * 订购流水号
     */
    private String orderNumber;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}

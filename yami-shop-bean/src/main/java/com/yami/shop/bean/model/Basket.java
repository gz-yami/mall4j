/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lanhai
 */
@Data
@TableName("tz_basket")
public class Basket implements Serializable {
    /**
     * 主键
     */
    @TableId

    private Long basketId;

    /**
     * 店铺ID
     */

    private Long shopId;

    /**
     * 产品ID
     */

    private Long prodId;

    /**
     * SkuID
     */

    private Long skuId;

    /**
     * 用户ID
     */

    private String userId;

    /**
     * 购物车产品个数
     */

    private Integer basketCount;

    /**
     * 购物时间
     */

    private Date basketDate;

    /**
     * 满减活动ID
     */
    private Long discountId;

    /**
     * 分销推广人卡号
     */
    private String distributionCardNo;
}
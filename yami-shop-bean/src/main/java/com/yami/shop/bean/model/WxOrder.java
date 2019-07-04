/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tz_wx_order")
public class WxOrder implements Serializable {
    /**
     * 微信订单id
     */
    @TableId
    private Long id;

    /**
     * 订单流水号
     */
    private String sn;

    /**
     * 微信流水号
     */

    private String wxSn;

    /**
     * 用户id
     */

    private String userId;

    /**
     * 订单状态
     */
    private Byte status;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 订单类型
     */

    private Byte orderType;

    /**
     * 扩展信息
     */

    private String extendInfo;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 更新时间
     */

    private Date updateTime;
}
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

import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName( "tz_points_change")
public class PointsChange {
    /**
     * 积分流动记录表
     */

    private Long pointsChangeId;

    /**
     * 积分钱包id
     */

    private Long pointsWalletId;

    /**
     * 增加或减少(增加 0 减少 1)
     */

    private Integer addOrReduce;

    /**
     * 原因(订单，邀请，签到，兑换)
     */
    private Integer reason;

    /**
     * 积分状态（0:用户未收货待结算，1:已结算 2:用户退货退单）
     */
    private Integer state;

    /**
     * 积分数额
     */

    private Double pointsNumber;

    /**
     * 关联订单id
     */

    private Long orderId;

    /**
     * 商户订单id
     */

    private Long merchantOrderId;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 更新时间
     */

    private Date updateTime;
}
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

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName( "tz_points_wallet")
public class PointsWallet {
    /**
     * 积分钱包id
     */

    private Long pointsWalletId;

    /**
     * 积分Id
     */

    private Long pointsId;

    /**
     * 用户id
     */

    private Long userId;

    /**
     * 待结算积分
     */
    private Double unsettled;

    /**
     * 已结算积分
     */
    private Double settled;

    /**
     * 积累收益积分
     */
    private Double addup;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;
}
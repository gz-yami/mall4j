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
@TableName( "tz_points_prod")
public class PointsProd {
    /**
     * 积分商品id
     */

    private Long pointsProdId;

    /**
     * 所需积分id
     */

    private Long pointsId;

    /**
     * 所需积分量
     */

    private Double pointsNumber;

    /**
     * 所需金额
     */
    private Double amount;

    /**
     * 关联商品id
     */

    private Long prodId;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 状态(0下架 1上架)
     */
    private Integer state;

    /**
     * 上架时间
     */

    private Date upperShelfTime;

    /**
     * 下架时间
     */

    private Date lowerShelf;
}
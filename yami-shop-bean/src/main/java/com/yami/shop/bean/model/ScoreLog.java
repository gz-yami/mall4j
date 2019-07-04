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
@TableName("tz_score_log")
public class ScoreLog {
    /**
     * 积分记录id
     */
    @TableId

    private Long id;

    /**
     * 用户id
     */

    private String userId;

    /**
     * 0支出 1收入
     */
    private Byte type;

    /**
     * 记录创建时间
     */

    private Date createTime;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 积分类型：1回收加积分 2购买减积分
     */

    private Byte scoreType;

}
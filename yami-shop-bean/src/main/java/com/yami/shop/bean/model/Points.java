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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yami.shop.bean.vo.SysUserVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;

@Data
@TableName( "tz_points")
public class Points {
    /**
     * 积分id
     */
    @TableId

    private Long pointsId;

    /**
     * 店铺id
     */

    private Long shopId;

    /**
     * 积分名称
     */
    private String name;

    /**
     * 状态(0 禁用 1启用)
     */
    private Integer state;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 操作人
     */
    private Long modifier;

    /**
     * 关联操作人
     */
    @TableField(exist = false)
    private SysUserVO sysUser;

}
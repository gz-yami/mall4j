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
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏表
 *
 * @author xwc
 * @date 2019-04-19 16:57:20
 */
@Data
@TableName("tz_user_collection")
@EqualsAndHashCode
public class UserCollection implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 收藏表
     */
    @TableId
    private Long id;
    /**
     * 商品id
     */
    private Long prodId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 收藏时间
     */
    private Date createTime;

}

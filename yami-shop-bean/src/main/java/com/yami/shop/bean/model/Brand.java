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
@TableName("tz_brand")
public class Brand implements Serializable {
    /**
     * 主键
     */
    @TableId

    private Long brandId;

    /**
     * 品牌名称
     */

    private String brandName;

    /**
     * 图片路径
     */

    private String brandPic;

    /**
     * 用户ID
     */

    private String userId;

    /**
     * 备注
     */
    private String memo;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 默认是1，表示正常状态,0为下线状态
     */
    private Integer status;

    /**
     * 简要描述
     */
    private String brief;

    /**
     * 记录时间
     */

    private Date recTime;

    /**
     * 更新时间
     */

    private Date updateTime;

    /**
     * 品牌首字母
     */

    private String firstChar;

    /**
     * 内容
     */
    private String content;
}
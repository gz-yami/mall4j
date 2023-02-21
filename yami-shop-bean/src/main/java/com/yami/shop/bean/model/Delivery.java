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
@TableName("tz_delivery")
public class Delivery implements Serializable {
    /**
     * ID
     */
    @TableId

    private Long dvyId;

    /**
     * 物流公司名称
     */

    private String dvyName;

    /**
     * 公司主页
     */

    private String companyHomeUrl;

    /**
     * 建立时间
     */

    private Date recTime;

    /**
     * 修改时间
     */

    private Date modifyTime;

    /**
     * 物流查询接口
     */

    private String queryUrl;
}
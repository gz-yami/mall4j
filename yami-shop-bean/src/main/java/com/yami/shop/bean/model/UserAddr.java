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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户地址管理
 *
 * @author hzm
 * @date 2019-04-15 10:49:40
 */
@Data
@TableName("tz_user_addr")
@EqualsAndHashCode(callSuper = false)
public class UserAddr implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long addrId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 收货人
     */
    private String receiver;
    /**
     * 省ID
     */
    private Long provinceId;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 城市ID
     */
    private Long cityId;
    /**
     * 区
     */
    private String area;
    /**
     * 区ID
     */
    private Long areaId;
    /**
     * 邮编
     */
    private String postCode;
    /**
     * 地址
     */
    private String addr;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 状态,1正常，0无效
     */
    private Integer status;
    /**
     * 是否默认地址 1是
     */
    private Integer commonAddr;
    /**
     * 建立时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}

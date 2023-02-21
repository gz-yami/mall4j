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
@TableName("tz_user_addr_order")
public class UserAddrOrder implements Serializable {
    /**
     * ID
     */
    @TableId


    private Long addrOrderId;

    /**
     * 地址ID
     */

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
     * 省
     */

    private String province;
    
    /**
     * 城市
     */

    private String city;
    
    /**
     * 区
     */

    private String area;

    /**
     * 地址
     */
    private String addr;

    /**
     * 邮编
     */

    private String postCode;

    /**
     * 省ID
     */

    private Long provinceId;
    
    /**
     * 城市ID
     */

    private Long cityId;
    
    /**
     * 区域ID
     */

    private Long areaId;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 建立时间
     */

    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

}
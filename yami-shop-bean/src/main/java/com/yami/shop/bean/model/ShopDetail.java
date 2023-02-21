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

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @author lanhai
 */
@Data
@TableName("tz_shop_detail")
public class ShopDetail implements Serializable{
    private static final long serialVersionUID = 3300529542917772262L;
    /**
     * 店铺id
     */
    @TableId
    private Long shopId;

    /**
     * 店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    private String shopName;

    /**
     * 店长用户id
     */
    private String userId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 店铺简介(可修改)
     */
    private String intro;

    /**
     * 店铺公告(可修改)
     */
    private String shopNotice;

    /**
     * 店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    private Integer shopIndustry;

    /**
     * 店长
     */
    private String shopOwner;

    /**
     * 店铺绑定的手机(登录账号：唯一)
     */
    private String mobile;

    /**
     * 店铺联系电话
     */
    private String tel;

    /**
     * 店铺所在纬度(可修改)
     */
    private String shopLat;

    /**
     * 店铺所在经度(可修改)
     */
    private String shopLng;

    /**
     * 店铺详细地址
     */
    private String shopAddress;

    /**
     * 店铺所在省份（描述）
     */
    private String province;

    /**
     * 店铺所在城市（描述）
     */
    private String city;

    /**
     * 店铺所在区域（描述）
     */
    private String area;

    /**
     * 店铺省市区代码，用于回显
     */
    private String pcaCode;

    /**
     * 店铺logo(可修改)
     */
    private String shopLogo;

    /**
     * 店铺相册
     */
    private String shopPhotos;

    /**
     * 每天营业时间段(可修改)
     */
    private String openTime;

    /**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    private Integer shopStatus;

    /**
     * 0:商家承担运费; 1:买家承担运费
     */
    private Integer transportType;

    /**
     * 固定运费
     */
    private Double fixedFreight;

    /**
     * 满X包邮
     */
    private Double fullFreeShipping;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 分销设置(0关闭 1开启)
     */
    private Integer isDistribution;
}

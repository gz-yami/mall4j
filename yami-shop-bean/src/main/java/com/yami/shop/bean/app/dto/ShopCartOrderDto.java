/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单个店铺的订单信息
 */
@Data
public class ShopCartOrderDto implements Serializable{

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "运费", required = true)
    private Double transfee;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Double discountReduce;

    @ApiModelProperty(value = "优惠券优惠金额", required = true)
    private Double couponReduce;

    @ApiModelProperty(value = "店铺优惠金额(促销活动 + 优惠券 + 其他)", required = true)
    private Double shopReduce = 0.0;

    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    @ApiModelProperty(value = "购物车商品", required = true)
    private List<ShopCartItemDiscountDto> shopCartItemDiscounts;

    @ApiModelProperty(value = "整个店铺可以使用的优惠券列表", required = true)
    private List<CouponOrderDto> coupons;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNumber;
}

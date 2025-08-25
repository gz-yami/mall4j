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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单个店铺的订单信息
 * @author lanhai
 */
@Data
public class ShopCartOrderDto implements Serializable{

    @Schema(description = "店铺id" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long shopId;

    @Schema(description = "店铺名称" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String shopName;

    @Schema(description = "实际总值" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double actualTotal;

    @Schema(description = "商品总值" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double total;

    @Schema(description = "商品总数" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalCount;

    @Schema(description = "运费" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double transfee;

    @Schema(description = "促销活动优惠金额" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double discountReduce;

    @Schema(description = "优惠券优惠金额" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double couponReduce;

    @Schema(description = "店铺优惠金额(促销活动 + 优惠券 + 其他)" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Double shopReduce = 0.0;

    @Schema(description = "订单备注信息" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String remarks;

    @Schema(description = "购物车商品" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ShopCartItemDiscountDto> shopCartItemDiscounts;

    @Schema(description = "整个店铺可以使用的优惠券列表" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private List<CouponOrderDto> coupons;

    @Schema(description = "订单编号" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNumber;
}

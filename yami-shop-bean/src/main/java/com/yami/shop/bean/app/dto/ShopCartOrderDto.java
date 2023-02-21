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

    @Schema(description = "店铺id" , required = true)
    private Long shopId;

    @Schema(description = "店铺名称" , required = true)
    private String shopName;

    @Schema(description = "实际总值" , required = true)
    private Double actualTotal;

    @Schema(description = "商品总值" , required = true)
    private Double total;

    @Schema(description = "商品总数" , required = true)
    private Integer totalCount;

    @Schema(description = "运费" , required = true)
    private Double transfee;

    @Schema(description = "促销活动优惠金额" , required = true)
    private Double discountReduce;

    @Schema(description = "优惠券优惠金额" , required = true)
    private Double couponReduce;

    @Schema(description = "店铺优惠金额(促销活动 + 优惠券 + 其他)" , required = true)
    private Double shopReduce = 0.0;

    @Schema(description = "订单备注信息" , required = true)
    private String remarks;

    @Schema(description = "购物车商品" , required = true)
    private List<ShopCartItemDiscountDto> shopCartItemDiscounts;

    @Schema(description = "整个店铺可以使用的优惠券列表" , required = true)
    private List<CouponOrderDto> coupons;

    @Schema(description = "订单编号" , required = true)
    private String orderNumber;
}

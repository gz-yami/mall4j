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
 * 多个店铺订单合并在一起的合并类
 * "/confirm" 使用
 */
@Data
public class ShopCartOrderMergerDto implements Serializable{

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "订单优惠金额(所有店铺优惠金额相加)", required = true)
    private Double orderReduce;

    @ApiModelProperty(value = "地址Dto", required = true)
    private UserAddrDto userAddr;

    @ApiModelProperty(value = "每个店铺的购物车信息", required = true)
    private List<ShopCartOrderDto> shopCartOrders;

    @ApiModelProperty(value = "整个订单可以使用的优惠券列表", required = true)
    private List<CouponOrderDto> coupons;
}

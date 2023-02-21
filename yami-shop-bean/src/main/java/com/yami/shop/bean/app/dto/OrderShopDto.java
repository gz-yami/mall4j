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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单下的每个店铺
 *
 * @author YaMi
 */
@Data
public class OrderShopDto implements Serializable {

    /**
     * 店铺ID
     **/
    @Schema(description = "店铺id" , required = true)
    private Long shopId;

    /**
     * 店铺名称
     **/
    @Schema(description = "店铺名称" , required = true)
    private String shopName;

    @Schema(description = "实际总值" , required = true)
    private Double actualTotal;

    @Schema(description = "商品总值" , required = true)
    private Double total;

    @Schema(description = "商品总数" , required = true)
    private Integer totalNum;

    @Schema(description = "地址Dto" , required = true)
    private UserAddrDto userAddrDto;

    @Schema(description = "产品信息" , required = true)
    private List<OrderItemDto> orderItemDtos;

    @Schema(description = "运费" , required = true)
    private Double transfee;

    @Schema(description = "优惠总额" , required = true)
    private Double reduceAmount;

    @Schema(description = "促销活动优惠金额" , required = true)
    private Double discountMoney;

    @Schema(description = "优惠券优惠金额" , required = true)
    private Double couponMoney;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "订单创建时间" , required = true)
    private Date createTime;

    /**
     * 订单备注信息
     */
    @Schema(description = "订单备注信息" , required = true)
    private String remarks;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态" , required = true)
    private Integer status;
}

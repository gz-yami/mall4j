/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
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

import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    /**
     * 店铺名称
     **/
    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "地址Dto", required = true)
    private UserAddrDto userAddrDto;

    @ApiModelProperty(value = "产品信息", required = true)
    private List<OrderItemDto> orderItemDtos;

    @ApiModelProperty(value = "运费", required = true)
    private Double transfee;

    @ApiModelProperty(value = "优惠总额", required = true)
    private Double reduceAmount;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Double discountMoney;

    @ApiModelProperty(value = "优惠券优惠金额", required = true)
    private Double couponMoney;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date createTime;

    /**
     * 订单备注信息
     */
    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;
}

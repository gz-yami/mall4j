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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("商品优惠券对象")
@Data
public class ProdCouponDto {

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "原价")
    private Double oriPrice;

    @ApiModelProperty(value = "现价")
    private Double price;

    @ApiModelProperty(value = "商品主图")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = " 优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty(value = "使用条件金额")
    private Double cashCondition;

    @ApiModelProperty(value = "减免金额")
    private Double reduceAmount;

    @ApiModelProperty(value = "折扣额度")
    private Double couponDiscount;

    @ApiModelProperty(value = "库存")
    private Integer stocks;


}

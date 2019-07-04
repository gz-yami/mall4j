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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * 购物车中选中的满减活动项信息
 */
@Data
public class ChooseDiscountItemDto implements Serializable {

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("优惠规则(0:满钱减钱 1:满件减钱 2:满钱打折 3:满件打折)")
    private Integer discountRule;

    @ApiModelProperty(value = "优惠项id")
    @JsonIgnore
    private Long discountItemId;

    @ApiModelProperty(value = "所需需要金额")
    private Double needAmount;

    @ApiModelProperty(value = "减免类型 0按满足最高层级减一次 1每满一次减一次")
    private Integer discountType;

    @ApiModelProperty(value = "参与满减满折优惠的商品数量")
    private int prodCount = 0;

    @ApiModelProperty(value = "参与满减满折优惠的商品金额")
    private double prodsPrice = 0.00;

    @ApiModelProperty(value = "优惠（元/折）")
    private Double discount;

    @ApiModelProperty(value = "参与满减满折优惠的金额")
    private Double reduceAmount = 0.0;

}

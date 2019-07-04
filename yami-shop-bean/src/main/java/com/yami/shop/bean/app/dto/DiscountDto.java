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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DiscountDto implements Serializable {

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("活动名称")
    private String discountName;

    @ApiModelProperty("优惠规则(0:满钱减钱 1:满件减钱 2:满钱打折 3:满件打折)")
    private Integer discountRule;

    @ApiModelProperty("减免类型(0:按满足最高层级减一次 1:每满一次减一次)")
    private Integer discountType;

    @ApiModelProperty("适用商品类型(0:通用券 1:商品券)")
    private Integer suitableProdType;

    @ApiModelProperty("最多减多少")
    private Double maxReduceAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    private List<DiscountItemDto> discountItems;

}

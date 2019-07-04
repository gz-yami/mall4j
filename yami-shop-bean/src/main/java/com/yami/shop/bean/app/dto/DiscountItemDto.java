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

@Data
public class DiscountItemDto implements Serializable {

    @ApiModelProperty(value = "优惠项id")
    private Long discountItemId;

    @ApiModelProperty(value = "优惠活动id")
    private Long discountId;

    @ApiModelProperty(value = "所需需要金额")
    private Double needAmount;

    @ApiModelProperty(value = "优惠（元/折）")
    private Double discount;
}

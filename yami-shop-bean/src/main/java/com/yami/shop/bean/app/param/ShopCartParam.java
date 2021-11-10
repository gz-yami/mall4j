/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value= "购物车参数")
@Data
public class ShopCartParam {

    @ApiModelProperty(value = "购物项id")
    private Long basketId;

    @ApiModelProperty(value = "活动id,传0则不参与该活动")
    private Long discountId;
}

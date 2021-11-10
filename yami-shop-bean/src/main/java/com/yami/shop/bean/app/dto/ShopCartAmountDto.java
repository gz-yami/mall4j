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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购物车合计")
public class ShopCartAmountDto {

    @ApiModelProperty("总额")
    private Double totalMoney;

    @ApiModelProperty("总计")
    private Double finalMoney;

    @ApiModelProperty("减额")
    private Double subtractMoney;

    @ApiModelProperty("商品数量")
    private Integer count;
}

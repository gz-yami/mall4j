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

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author LGH
 */
@Data
public class ChangeShopCartParam {

    @Schema(description = "购物车ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long basketId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long prodId;

    @NotNull(message = "skuId不能为空")
    @Schema(description = "skuId" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long skuId;

    @NotNull(message = "店铺ID不能为空")
    @Schema(description = "店铺ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long shopId;

    @NotNull(message = "商品个数不能为空")
    @Schema(description = "商品个数" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer count;

    @Schema(description = "分销推广人卡号" )
    private String distributionCardNo;
}

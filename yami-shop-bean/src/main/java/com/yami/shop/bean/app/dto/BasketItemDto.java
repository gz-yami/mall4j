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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BasketItemDto implements Serializable {

    @ApiModelProperty(value = "购物车ID", required = true)
    private Long basketId;

    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    /**
     * 商品名称
     */
    @JsonIgnore
    private String shopName;

    @ApiModelProperty(value = "产品ID", required = true)
    private Long prodId;

    @ApiModelProperty(value = "skuID", required = true)
    private Long skuId;

    @ApiModelProperty(value = "产品个数", required = true)
    private Integer prodCount;

    @ApiModelProperty(value = "产品名称", required = true)
    private String prodName;

    @ApiModelProperty(value = "产品主图", required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "产品现价", required = true)
    private Double price;

    @ApiModelProperty(value = "产品原价", required = true)
    private Double oriPrice;

    @ApiModelProperty(value = "产品简介", required = true)
    private String brief;

    @ApiModelProperty(value = "产品sku信息", required = true)
    private String skuName;

    @ApiModelProperty("参与满减活动列表")
    private List<DiscountDto> discounts;

}

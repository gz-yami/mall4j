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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lanhai
 */
@Data
public class BasketItemDto implements Serializable {

    @Schema(description = "购物车ID" , required = true)
    private Long basketId;

    @Schema(description = "店铺ID" , required = true)
    private Long shopId;

    /**
     * 商品名称
     */
    @JsonIgnore
    private String shopName;

    @Schema(description = "产品ID" , required = true)
    private Long prodId;

    @Schema(description = "skuID" , required = true)
    private Long skuId;

    @Schema(description = "产品个数" , required = true)
    private Integer prodCount;

    @Schema(description = "产品名称" , required = true)
    private String prodName;

    @Schema(description = "产品主图" , required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @Schema(description = "产品现价" , required = true)
    private Double price;

    @Schema(description = "产品原价" , required = true)
    private Double oriPrice;

    @Schema(description = "产品简介" , required = true)
    private String brief;

    @Schema(description = "产品sku信息" , required = true)
    private String skuName;

    @Schema(description = "参与满减活动列表" )
    private List<DiscountDto> discounts;

}

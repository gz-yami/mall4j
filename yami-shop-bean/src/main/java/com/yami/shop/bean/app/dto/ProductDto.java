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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.bean.model.Transport;

import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductDto {
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID", required = true)
    private Long prodId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "详细描述")
    private String content;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价", required = true)
    private Double oriPrice;

    /**
     * 库存量
     */
    @ApiModelProperty(value = "库存量", required = true)
    private Integer totalStocks;

    /**
     * 简要描述,卖点等
     */
    @ApiModelProperty(value = "简要描述,卖点等", required = true)
    private String brief;

    /**
     * 商品主图
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品主图", required = true)
    private String pic;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品图片列表，以逗号分割", required = true)
    private String imgs;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类id", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "sku列表")
    private List<SkuDto> skuList;

    @ApiModelProperty(value = "运费信息", required = true)
    private Transport transport;

    public static interface WithNoContent{}

    public static interface WithContent extends WithNoContent{}


}

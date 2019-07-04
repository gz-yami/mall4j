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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SkuDto implements Serializable {

    private static final long serialVersionUID = 6457261945829470666L;

    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;
    @ApiModelProperty(value = "价格", required = true)
    private Double price;

    @ApiModelProperty(value = "库存(-1表示无穷)", required = true)
    private Integer stocks;

    @ApiModelProperty(value = "sku名称", required = true)
    private String skuName;

    @ApiModelProperty(value = "图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "销售属性组合字符串,格式是p1:v1;p2:v2", required = true)
    private String properties;
}

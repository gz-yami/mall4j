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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lanhai
 */
@Schema(description = "我的订单-订单项")
@Data
public class MyOrderItemDto {

    @Schema(description = "商品图片" , required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @Schema(description = "商品名称" , required = true)
    private String prodName;

    @Schema(description = "商品数量" , required = true)
    private Integer prodCount;

    @Schema(description = "商品价格" , required = true)
    private Double price;

    @Schema(description = "skuName" , required = true)
    private String skuName;

}

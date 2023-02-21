/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lanhai
 */
@Data
@Schema(description = "搜索商品数据")
public class SearchProdDto {

    @Schema(description = "商品id" )
    private Long prodId;

    @Schema(description = "商品照片" )
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @Schema(description = "商品名字" )
    private String prodName;

    @Schema(description = "商品价格" )
    private Double price;

    @Schema(description = "商品评论数量" )
    private Integer prodCommNumber;

    @Schema(description = "好评率" )
    private Double positiveRating;

    @Schema(description = "好评数量" )
    private Integer praiseNumber;

}

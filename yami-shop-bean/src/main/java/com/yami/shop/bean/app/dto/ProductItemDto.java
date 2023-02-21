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
import java.util.ArrayList;
import java.util.List;

/**
 * @author LGH
 */
@Data
public class ProductItemDto implements Serializable {

	@Schema(description = "产品名称" ,required=true)
	private String prodName;

	@Schema(description = "产品个数" ,required=true)
	private Integer prodCount;

	@Schema(description = "产品图片路径" ,required=true)
	@JsonSerialize(using = ImgJsonSerializer.class)
	private String pic;

	@Schema(description = "产品价格" ,required=true)
	private Double price;

	@Schema(description = "商品总金额" ,required=true)
	private Double productTotalAmount;

	@Schema(description = "产品ID" ,required=true)
	private Long prodId;

    @Schema(description = "skuId" ,required=true)
    private Long skuId;

	@Schema(description = "规格名称" , required = true)
	private String skuName;

	@Schema(description = "basketId" ,required=true)
	private Long basketId;

	@Schema(description = "商品实际金额 = 商品总金额 - 分摊的优惠金额" )
	private Double actualTotal;

	@Schema(description = "满减满折优惠id，0不主动参与活动（用户没有主动参与该活动），-1主动不参与活动" )
	private Long discountId = 0L;

	@Schema(description = "分摊的优惠金额" )
	private Double shareReduce;

	@Schema(description = "参与满减活动列表" )
	private List<DiscountDto> discounts = new ArrayList<>();
}

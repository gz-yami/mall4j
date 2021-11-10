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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryDto {

	@ApiModelProperty(value = "分类id",required=true)
	private Long categoryId;

	@ApiModelProperty(value = "分类父id",required=true)
	private Long parentId;

	@ApiModelProperty(value = "分类名称",required=true)
	private String categoryName;

	@ApiModelProperty(value = "分类图片",required=true)
	@JsonSerialize(using = ImgJsonSerializer.class)
	private String pic;

}

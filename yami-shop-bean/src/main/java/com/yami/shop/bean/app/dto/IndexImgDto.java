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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("首页图片对象")
@Data
public class IndexImgDto {

    /**
     * 图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "图片Url", required = true)
    private String imgUrl;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "图片顺序", required = true)
    private Integer seq;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间", required = true)
    private Date uploadTime;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    private int type;

    /**
     * 关联id
     */
    @ApiModelProperty(value = "关联id", required = true)
    private Long relation;



}

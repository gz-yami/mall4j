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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("收藏对象")
@Data
public class UserCollectionDto {

    @ApiModelProperty(value = "收藏id")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "收藏时间")
    private Date createTime;

}

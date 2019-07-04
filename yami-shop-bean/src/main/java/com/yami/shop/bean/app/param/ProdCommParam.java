/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value= "添加评论信息")
public class ProdCommParam {
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品id")
    private Long prodId;
    /**
     * 订单项ID
     */
    @ApiModelProperty(value = "订单项ID")
    private Long orderItemId;

    /**
     * 评价，0-5分
     */
    @ApiModelProperty(value = "评价，0-5分",required=true)
    @NotNull(message = "评价不能为空")
    private Integer score;

    @ApiModelProperty(value = "评论内容",required=true)
    private String content;

    @ApiModelProperty(value = "评论图片, 用逗号分隔")
    private String pics;

    /**
     * 是否匿名(1:是  0:否)
     */
    @ApiModelProperty(value = "是否匿名(1:是  0:否) 默认为否")
    private Integer isAnonymous;


    @ApiModelProperty(value = "* 评价(0好评 1中评 2差评)")
    private Integer evaluate;

}

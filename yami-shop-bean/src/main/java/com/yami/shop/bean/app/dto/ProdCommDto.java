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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.EmojiJsonSerializer;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel("评论对象")
@Data
public class ProdCommDto {

    @ApiModelProperty(value = "id")
    private Long prodCommId;

    /**
     * 得分，0-5分
     */
    @ApiModelProperty(value = "得分，0-5分")
    private Integer score;

    /**
     * 是否匿名(1:是  0:否)
     */
    @ApiModelProperty(value = "是否匿名(1:是  0:否)")
    private Integer isAnonymous;

    /**
     * 掌柜回复
     */
    @ApiModelProperty(value = "掌柜回复")
    private String replyContent;

    /**
     * 记录时间
     */
    @ApiModelProperty(value = "记录时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recTime;

    /**
     * 回复时间
     */
    @ApiModelProperty(value = "回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replyTime;

    @JsonSerialize(using = EmojiJsonSerializer.class)
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 头像图片路径
     */
    @ApiModelProperty(value = "头像图片路径")
    private String pic;

    /**
     * 是否回复 0:未回复  1:已回复
     */
    @ApiModelProperty(value = "商家是否回复 0:未回复  1:已回复")
    private Integer replySts;

    /**
     * 评论图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "评论图片")
    private String pics;

    /**
     * 评价等级
     */
    @ApiModelProperty(value = "0好评 1中评 2差评")
    private Byte evaluate;

    @ApiModelProperty(value = "评论内容")
    private String content;

}

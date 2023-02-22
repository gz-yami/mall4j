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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lanhai
 */
@Schema(description = "评论对象")
@Data
public class ProdCommDto {

    @Schema(description = "id" )
    private Long prodCommId;

    /**
     * 得分，0-5分
     */
    @Schema(description = "得分，0-5分" )
    private Integer score;

    /**
     * 是否匿名(1:是  0:否)
     */
    @Schema(description = "是否匿名(1:是  0:否)" )
    private Integer isAnonymous;

    /**
     * 掌柜回复
     */
    @Schema(description = "掌柜回复" )
    private String replyContent;

    /**
     * 记录时间
     */
    @Schema(description = "记录时间" )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recTime;

    /**
     * 回复时间
     */
    @Schema(description = "回复时间" )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replyTime;

    @Schema(description = "用户昵称" )
    private String nickName;

    /**
     * 头像图片路径
     */
    @Schema(description = "头像图片路径" )
    private String pic;

    /**
     * 是否回复 0:未回复  1:已回复
     */
    @Schema(description = "商家是否回复 0:未回复  1:已回复" )
    private Integer replySts;

    /**
     * 评论图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @Schema(description = "评论图片" )
    private String pics;

    /**
     * 评价等级
     */
    @Schema(description = "0好评 1中评 2差评" )
    private Byte evaluate;

    @Schema(description = "评论内容" )
    private String content;

}

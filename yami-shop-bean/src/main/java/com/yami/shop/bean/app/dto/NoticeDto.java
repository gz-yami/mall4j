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

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author lanhai
 */
@Schema(description = "公告对象")
@Data
public class NoticeDto {

    @JsonView(NoContent.class)
    @Schema(description = "公告id" )
    private Long id;

    @JsonView(NoContent.class)
    @Schema(description = "店铺id" )
    private Long shopId;

    @JsonView(NoContent.class)
    @Schema(description = "标题" )
    private String title;

    @Schema(description = "公告内容" )
    @JsonView(WithContent.class)
    private String content;

    @JsonView(NoContent.class)
    @Schema(description = "公告发布时间" )
    private Date publishTime;

    public static interface NoContent{}

    public static interface WithContent extends NoContent{}

}

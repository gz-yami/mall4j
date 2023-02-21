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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
/**
 * @author lanhai
 */
@Schema(description = "收藏对象")
@Data
public class UserCollectionDto {

    @Schema(description = "收藏id" )
    private Long id;

    @Schema(description = "商品名称" )
    private String prodName;

    @Schema(description = "收藏时间" )
    private Date createTime;

}

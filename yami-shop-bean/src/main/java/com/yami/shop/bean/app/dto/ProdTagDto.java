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

/**
 * @author lanhai
 */
@Data
public class ProdTagDto {

    @Schema(description = "分组标签id" )
    private Long id;

    @Schema(description = "分组标签标题" )
    private String title;

    @Schema(description = "排序（数值越高越靠前）" )
    private String seq;

    @Schema(description = "列表样式(0:一列一个,1:一列两个,2:一列三个)" )
    private String style;

}

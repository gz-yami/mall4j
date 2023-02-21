/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lanhai
 */
@Schema(description = "热搜数据")
@Data
public class HotSearchDto implements Serializable {

    @Schema(description = "热搜id" )
    private Long hotSearchId;

    @Schema(description = "标题" )
    private String title;

    @Schema(description = "内容" )
    private String content;

}

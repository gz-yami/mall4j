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
@Schema(description = "商品评论数据")
@Data
public class ProdCommDataDto {

    @Schema(description = "好评率" )
    private Double positiveRating;

    @Schema(description = "评论数量" )
    private Integer number;

    @Schema(description = "好评数" )
    private Integer praiseNumber;

    @Schema(description = "中评数" )
    private Integer secondaryNumber;

    @Schema(description = "差评数" )
    private Integer negativeNumber;

    @Schema(description = "有图数" )
    private Integer picNumber;

}

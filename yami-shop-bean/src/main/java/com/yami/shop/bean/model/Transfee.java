/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @author lanhai
 */
@Data
@TableName("tz_transfee")
public class Transfee implements Serializable {
    private static final long serialVersionUID = 8039640964056626028L;
    /**
     * 运费项id
     */
    @TableId
    @Schema(description = "运费项id" ,required=true)
    private Long transfeeId;

    /**
     * 运费模板id
     */

    @Schema(description = "运费模板id" ,required=true)
    private Long transportId;

    /**
     * 续件数量
     */

    @Schema(description = "续件数量" ,required=true)
    private Double continuousPiece;

    /**
     * 首件数量
     */

    @Schema(description = "首件数量" ,required=true)
    private Double firstPiece;

    /**
     * 续件费用
     */

    @Schema(description = "续件费用" ,required=true)
    private Double continuousFee;

    /**
     * 首件费用
     */

    @Schema(description = "首件费用" ,required=true)
    private Double firstFee;

    @TableField(exist=false)
    @Schema(description = "指定条件运费城市项" ,required=true)
    private List<Area> cityList;

}

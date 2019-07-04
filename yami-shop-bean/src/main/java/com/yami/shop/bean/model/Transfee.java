/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("tz_transfee")
public class Transfee implements Serializable {
    private static final long serialVersionUID = 8039640964056626028L;
    /**
     * 运费项id
     */
    @TableId
    @ApiModelProperty(value = "运费项id",required=true)
    private Long transfeeId;

    /**
     * 运费模板id
     */

    @ApiModelProperty(value = "运费模板id",required=true)
    private Long transportId;

    /**
     * 续件数量
     */

    @ApiModelProperty(value = "续件数量",required=true)
    private Double continuousPiece;

    /**
     * 首件数量
     */

    @ApiModelProperty(value = "首件数量",required=true)
    private Double firstPiece;

    /**
     * 续件费用
     */

    @ApiModelProperty(value = "续件费用",required=true)
    private Double continuousFee;

    /**
     * 首件费用
     */

    @ApiModelProperty(value = "首件费用",required=true)
    private Double firstFee;

    @TableField(exist=false)
    @ApiModelProperty(value = "指定条件运费城市项",required=true)
    private List<Area> cityList;

}

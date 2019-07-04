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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("tz_transfee_free")
public class TransfeeFree implements Serializable {
    private static final long serialVersionUID = -2811714952219888223L;
    /**
     * 指定条件包邮项id
     */
    @TableId
    @ApiModelProperty(value = "指定条件包邮项id",required=true)
    private Long transfeeFreeId;

    /**
     * 运费模板id
     */

    @ApiModelProperty(value = "运费模板id",required=true)
    private Long transportId;

    /**
     * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
     */

    @ApiModelProperty(value = "包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）",required=true)
    private Integer freeType;

    /**
     * 需满金额
     */
    @ApiModelProperty(value = "需满金额",required=true)
    private Double amount;

    /**
     * 包邮x件/重量/体积
     */
    @ApiModelProperty(value = "包邮x件/重量/体积",required=true)
    private Double piece;

    /**
     * 指定条件包邮城市项
     */
    @TableField(exist=false)
    @ApiModelProperty(value = "指定条件包邮城市项",required=true)
    private List<Area> freeCityList;
}

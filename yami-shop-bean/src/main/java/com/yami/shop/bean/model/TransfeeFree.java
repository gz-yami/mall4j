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
@TableName("tz_transfee_free")
public class TransfeeFree implements Serializable {
    private static final long serialVersionUID = -2811714952219888223L;
    /**
     * 指定条件包邮项id
     */
    @TableId
    @Schema(description = "指定条件包邮项id" ,required=true)
    private Long transfeeFreeId;

    /**
     * 运费模板id
     */

    @Schema(description = "运费模板id" ,required=true)
    private Long transportId;

    /**
     * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
     */

    @Schema(description = "包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）" ,required=true)
    private Integer freeType;

    /**
     * 需满金额
     */
    @Schema(description = "需满金额" ,required=true)
    private Double amount;

    /**
     * 包邮x件/重量/体积
     */
    @Schema(description = "包邮x件/重量/体积" ,required=true)
    private Double piece;

    /**
     * 指定条件包邮城市项
     */
    @TableField(exist=false)
    @Schema(description = "指定条件包邮城市项" ,required=true)
    private List<Area> freeCityList;
}

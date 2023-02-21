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
@TableName("tz_area")
public class Area implements Serializable {
    private static final long serialVersionUID = -6013320537436191451L;
    @TableId
    @Schema(description = "地区id" ,required=true)
    private Long areaId;

    @Schema(description = "地区名称" ,required=true)
    private String areaName;

    @Schema(description = "地区上级id" ,required=true)
    private Long parentId;

    @Schema(description = "地区层级" ,required=true)
    private Integer level;

    @TableField(exist=false)
    private List<Area> areas;
}

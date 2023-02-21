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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @author lanhai
 */
@Data
@TableName("tz_transport")
public class Transport implements Serializable {
    private static final long serialVersionUID = 1876655654053364580L;
    /**
     * 运费模板id
     */
    @TableId
    @Schema(description = "运费模板id" ,required=true)
    private Long transportId;

    /**
     * 运费模板名称
     */

    @Schema(description = "运费模板名称" ,required=true)
    private String transName;

    /**
     * 创建时间
     */

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间" ,required=true)
    private Date createTime;

    /**
     * 店铺id
     */

    @Schema(description = "店铺id" ,required=true)
    private Long shopId;

    /**
     * 参考 TransportChargeType
     * 收费方式（0 按件数,1 按重量 2 按体积）
     */
    @Schema(description = "收费方式（0 按件数,1 按重量 2 按体积）" ,required=true)
    private Integer chargeType;


   /**
    * 是否包邮 0:不包邮 1:包邮
    */
    @Schema(description = "是否包邮 0:不包邮 1:包邮" ,required=true)
    private Integer isFreeFee;

    /**
     * 是否含有包邮条件
     */
    @Schema(description = "是否含有包邮条件" ,required=true)
    private Integer hasFreeCondition;

    /**
     * 指定条件包邮项
     */
    @TableField(exist=false)
    @Schema(description = "指定条件包邮项" ,required=true)
    private List<TransfeeFree> transfeeFrees;

    /**
     * 运费项
     */
    @TableField(exist=false)
    @Schema(description = "运费项" ,required=true)
    private List<Transfee> transfees;

}

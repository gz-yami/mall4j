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
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author lanhai
 */
@Data
@TableName("tz_prod_prop")
public class ProdProp implements Serializable {
    private static final long serialVersionUID = -8761177918672000191L;

    /**
     * 属性id
     */
    @TableId
    private Long propId;

    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    private String propName;

    /**
     * 1:销售属性(规格); 2:参数属性;
     */
    private Integer rule;

    private Long shopId;

    /**
     * 属性值
     */
    @TableField(exist=false)
    @NotEmpty(message="规格属性值不能为空")
    private List<ProdPropValue> prodPropValues;

}

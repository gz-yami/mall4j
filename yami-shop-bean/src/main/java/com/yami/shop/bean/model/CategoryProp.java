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

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lanhai
 */
@Data
@TableName("tz_category_prop")
public class CategoryProp implements Serializable {
    @TableId

    private Long id;

    /**
     * 分类id
     */

    private Long categoryId;

    /**
     * 商品属性id即表tz_prod_prop中的prop_id
     */

    private Long propId;
}
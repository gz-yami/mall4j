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

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tz_category_brand")
public class CategoryBrand implements Serializable {
    @TableId

    private Long id;

    /**
     * 分类id
     */

    private Long categoryId;

    /**
     * 品牌id
     */

    private Long brandId;
}
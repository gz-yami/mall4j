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

@Data
@TableName("tz_sens_word")
public class SensWord {
    /**
     * ID
     */
    @TableId

    private Long sensId;

    /**
     * 敏感字
     */
    private String words;
}
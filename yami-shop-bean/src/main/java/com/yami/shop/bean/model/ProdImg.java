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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lanhai
 */
@Data
@TableName("tz_prod_img")
public class ProdImg implements Serializable {
    /**
     * 图片ID
     */
    @TableId

    private Long imgId;

    /**
     * 产品ID
     */

    private Long prodId;

    /**
     * skuID(商品通用图片 skuid为0)
     */

    private Long skuId;

    /**
     * 文件路径
     */

    private String imgPath;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 顺序
     */
    private Integer seq;

}
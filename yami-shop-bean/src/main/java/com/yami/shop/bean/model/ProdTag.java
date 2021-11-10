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
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品分组标签
 *
 * @author hzm
 * @date 2019-04-18 10:48:44
 */
@Data
@TableName("tz_prod_tag")
public class ProdTag implements Serializable {

    private static final long serialVersionUID = 1991508792679311621L;
    /**
     * 分组标签id
     */
    @TableId
    private Long id;
    /**
     * 分组标题
     */
    private String title;
    /**
     * 店铺Id
     */
    private Long shopId;
    /**
     * 状态(1为正常,0为删除)
     */
    private Integer status;
    /**
     * 默认类型(0:商家自定义,1:系统默认类型)
     */
    private Integer isDefault;
    /**
     * 商品数量
     */
    private Long prodCount;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 列表样式(0:一列一个,1:一列两个,2:一列三个)
     */
    private Integer style;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除时间
     */
    private Date deleteTime;

}

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
 * 分组标签引用
 *
 * @author hzm
 * @date 2019-04-18 16:28:01
 */
@Data
@TableName("tz_prod_tag_reference")
public class ProdTagReference implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分组引用id
     */
    @TableId
    private Long referenceId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 商品id
     */
    private Long prodId;
    /**
     * 状态(1:正常,0:删除)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

}

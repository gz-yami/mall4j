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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lanhai
 */
@Data
@TableName("tz_sku")
public class Sku implements Serializable {
    /**
     * 单品ID
     */
    @TableId
    private Long skuId;

    /**
     * 商品ID
     */
    private Long prodId;

    /**
     * 销售属性组合字符串,格式是p1:v1;p2:v2
     */
    private String properties;

    /**
     * 原价
     */
    private Double oriPrice;

    /**
     * 价格
     */
    private Double price;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 实际库存
     */
    private Integer actualStocks;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 记录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recTime;

    /**
     * 商家编码
     */
    private String partyCode;

    /**
     * 商品条形码
     */
    private String modelId;

    /**
     * sku图片
     */
    private String pic;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 商品名称
     */
    private String prodName;

    private Integer version;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 状态：0禁用 1 启用
     */
    private Integer status;

    /**
     * 0 正常 1 已被删除
     */
    private Integer isDelete;

}

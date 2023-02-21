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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lanhai
 */
@Data
@TableName("tz_prod")
public class Product implements Serializable {

    private static final long serialVersionUID = -4644407386444894349L;
    /**
     * 商品ID
     */
    @TableId
    private Long prodId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 原价
     */
    private Double oriPrice;

    /**
     * 现价
     */
    private Double price;

    /**
     * 简要描述,卖点等
     */
    private String brief;

    /**
     * 商品主图
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    /**
     * 商品图片
     */
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    private Integer status;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 已经销售数量
     */
    private Integer soldNum;

    /**
     * 库存量
     */
    private Integer totalStocks;

    /**
     * 配送方式json
     */
    private String deliveryMode;

    /**
     * 运费模板id
     */
    private Long deliveryTemplateId;

    /**
     * 录入时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 详细描述
     */
    private String content;


    /**
     * 上架时间
     */
    private Date putawayTime;

    /**
     * 版本
     */
    @Version
    private Integer version;

    /**
     * sku列表
     */
    @TableField(exist = false)
    private List<Sku> skuList;

    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopName;

    @TableField(exist = false)
    private List<Long> tagList;


    @Data
    public static class DeliveryModeVO {

        /**
         * 用户自提
         */
        private Boolean hasUserPickUp;

        /**
         * 店铺配送
         */
        private Boolean hasShopDelivery;

    }
}

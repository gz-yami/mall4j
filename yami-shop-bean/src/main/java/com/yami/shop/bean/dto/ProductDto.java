/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.dto;

import com.yami.shop.bean.model.Sku;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lanhai
 */
@Data
public class ProductDto {
    /**
     * 商品ID
     */
    private Long prodId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 版本号
     */
    private Integer version;

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
    private String pic;

    /**
     * 商品图片
     */
    private String imgs;


    /**
     * 默认是1，表示正常状态
     */
    private Integer status;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 观看人数
     */
    private Integer views;

    /**
     * 已经销售数量
     */
    private Integer buys;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 评论得分
     */
    private Integer commentScore;

    /**
     * 库存量
     */
    private Integer stocks;

    /**
     * 实际库存
     */
    private Integer actualStocks;

    /**
     * 库存警告
     */
    private Integer stocksArm;

    /**
     * 关键字
     */
    private String keyWord;

    /**
     * 是否包含用户自提
     */
    private Integer hasUserPickUp;

    /**
     * 是否包含商家配送
     */
    private Integer hasShopDelivery;

    /**
     * 运费模板id
     */
    private Long transportId;

    /**
     * 有没发票
     */
    private Integer hasInvoice;

    /**
     * 是否保修
     */
    private Integer hasGuarantee;

    /**
     * 商品SEOTitle
     */
    private String metaTitle;

    /**
     * 商品SEO desc
     */
    private String metaDesc;

    /**
     * 商品SEO keywords
     */
    private String metaKeywords;

    /**
     * 录入时间
     */
    private Date recTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 详细描述
     */
    private String content;

    /**
     * sku列表
     */
    private List<Sku> skuList;

    /**
     * 商品分类名称
     */
    private String categoryName;

}

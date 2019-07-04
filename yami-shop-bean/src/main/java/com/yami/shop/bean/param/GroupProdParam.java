/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

public class GroupProdParam {
    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 商品最小金额区间
     */
    private Double minMoney;


    /**
     * 商品最大金额区间
     */
    private Double maxMoney;

    /**
     * 商品分类Id
     */
    private Long categoryId;

    /**
     * 活动group_activity_id
     */
    private Long groupActivityId;

    /**
     * 商品店铺Id
     */
    private Long shopId;

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }

    public Double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(Long groupActivityId) {
        this.groupActivityId = groupActivityId;
    }
}

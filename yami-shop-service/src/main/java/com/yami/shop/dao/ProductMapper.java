/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.dto.SearchProdDto;
import com.yami.shop.common.util.PageParam;
import org.apache.ibatis.annotations.Param;

import com.yami.shop.bean.model.Product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ProductMapper extends BaseMapper<Product> {

    int updateStocks(@Param("prod") Product product);

    Product getProductByProdNameAndShopId(@Param("prodName") String prodName, @Param("shopId") Long shopId);

    void returnStock(@Param("prodCollect") Map<Long, Integer> prodCollect);

    IPage<ProductDto> pageByPutawayTime(IPage<ProductDto> page);

    IPage<ProductDto> pageByTagId(Page<ProductDto> page, @Param("tagId") Long tagId);

    IPage<ProductDto> moreBuyProdList(Page<ProductDto> page);

    IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, @Param("categoryId") Long categoryId);

    List<Product> listProdByCategoryId(@Param("categoryId") Long categoryId);

    IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page page, @Param("prodName") String prodName, @Param("sort") Integer sort, @Param("orderBy") Integer orderBy);

    TagProductDto tagProdList(@Param("tagId") Long tagId);

    List<TagProductDto> tagProdList();

    /**
     * 查看店铺的所有活动商品
     */
    IPage<ProductDto> listByShopId(@Param("page") PageParam<ProductDto> page, @Param("shopId") Long shopId);

    /**
     * 通过优惠券适用商品类型、优惠券id、店铺id获取商品列表
     */
    IPage<ProductDto> listBySuitableProdTypeAndCouponIdAndShopId(@Param("page") PageParam<ProductDto> page, @Param("shopId") Long shopId, @Param("suitableProdType") Integer suitableProdType, @Param("couponId") Long couponId);

    /**
     * 获取用户的收藏商品列表
     *
     * @param userId
     * @return
     */
    IPage<ProductDto> collectionProds(@Param("page") PageParam page, @Param("userId") String userId);

}

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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.dto.SearchProdDto;
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lanhai
 */
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 更新商品库存
     * @param product
     * @return
     */
    int updateStocks(@Param("prod") Product product);

    /**
     * 返回库存
     * @param prodCollect
     */
    void returnStock(@Param("prodCollect") Map<Long, Integer> prodCollect);

    /**
     * 根据上架时间分页获取商品
     * @param page
     * @return
     */
    IPage<ProductDto> pageByPutAwayTime(IPage<ProductDto> page);

    /**
     * 根据标签id分页获取商品信息
     * @param page
     * @param tagId
     * @return
     */
    IPage<ProductDto> pageByTagId(Page<ProductDto> page, @Param("tagId") Long tagId);

    /**
     * 分页获取销量最多的商品
     * @param page
     * @return
     */
    IPage<ProductDto> moreBuyProdList(Page<ProductDto> page);

    /**
     * 根据分类id分页获取商品
     * @param page
     * @param categoryId
     * @return
     */
    IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, @Param("categoryId") Long categoryId);

    /**
     * 根据商品名称和排序分页获取商品
     * @param page
     * @param prodName
     * @param sort
     * @param orderBy
     * @return
     */
    IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page page, @Param("prodName") String prodName, @Param("sort") Integer sort, @Param("orderBy") Integer orderBy);

    /**
     * 获取分组商品列表
     * @return
     */
    List<TagProductDto> tagProdList();


    /**
     * 获取用户的收藏商品列表
     * @param page
     * @param userId
     * @return
     */
    IPage<ProductDto> collectionProds(@Param("page") PageParam page, @Param("userId") String userId);

}

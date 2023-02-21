/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.dto.SearchProdDto;
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.util.PageParam;

import java.util.List;

/**
 * 商品
 * @author lanhai
 */
public interface ProductService extends IService<Product> {

    /**
     * 保存商品
     *
     * @param product 商品信息
     */
    void saveProduct(Product product);

    /**
     * 更新商品
     * @param dbProduct
     * @param product 商品信息
     */
    void updateProduct(Product product, Product dbProduct);

    /**
     * 根据商品id获取商品信息
     *
     * @param prodId
     * @return
     */
    Product getProductByProdId(Long prodId);

    /**
     * 根据商品id删除商品
     * @param prodId
     */
    void removeProductByProdId(Long prodId);

    /**
     * 根据商品id删除缓存
     * @param prodId
     */
    void removeProductCacheByProdId(Long prodId);

    /**
     * 根据上架时间倒序分页获取商品
     * @param page
     * @return
     */
    IPage<ProductDto> pageByPutAwayTime(IPage<ProductDto> page);

    /**
     * 根据标签分页获取商品
     * @param page
     * @param tagId
     * @return
     */
    IPage<ProductDto> pageByTagId(Page<ProductDto> page, Long tagId);

    /**
     * 分页获取销量较高商品
     * @param page
     * @return
     */
    IPage<ProductDto> moreBuyProdList(Page<ProductDto> page);

    /**
     * 根据分类id分页获取商品列表
     * @param page
     * @param categoryId
     * @return
     */
    IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, Long categoryId);

    /**
     * 根据商品名称
     * @param page
     * @param prodName
     * @param sort
     * @param orderBy
     * @return
     */
    IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page page, String prodName, Integer sort, Integer orderBy);

    /**
     * 分组获取商品列表
     * @return
     */
    List<TagProductDto> tagProdList();

    /**
     * 分页获取收藏商品
     * @param page
     * @param userId
     * @return
     */
    IPage<ProductDto> collectionProds(PageParam page, String userId);
}

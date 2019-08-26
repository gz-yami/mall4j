/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
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
     *
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


    void removeProductByProdId(Long prodId);

    void removeProductCacheByProdId(Long prodId);

    IPage<ProductDto> pageByPutawayTime(IPage<ProductDto> page);

    IPage<ProductDto> pageByTagId(Page<ProductDto> page, Long tagId);

    IPage<ProductDto> moreBuyProdList(Page<ProductDto> page);

    IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, Long categoryId);

    List<Product> listProdByCategoryId(Long categoryId);

    IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page page, String prodName, Integer sort, Integer orderBy);

    List<TagProductDto> tagProdList();

    IPage<ProductDto> collectionProds(PageParam page, String userId);
}

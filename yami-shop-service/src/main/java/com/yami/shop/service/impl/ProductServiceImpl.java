/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.app.dto.ProductDto;
import com.yami.shop.bean.app.dto.TagProductDto;
import com.yami.shop.bean.dto.SearchProdDto;
import com.yami.shop.bean.model.ProdTagReference;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.util.Arith;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.dao.ProdTagReferenceMapper;
import com.yami.shop.dao.ProductMapper;
import com.yami.shop.dao.SkuMapper;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanhai
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ProdTagReferenceMapper prodTagReferenceMapper;

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product) {
        productMapper.insert(product);
        if (CollectionUtil.isNotEmpty(product.getSkuList())) {
            skuMapper.insertBatch(product.getProdId(), product.getSkuList());
        }
        prodTagReferenceMapper.insertBatch(product.getShopId(), product.getProdId(), product.getTagList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#product.prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#product.prodId")
    })
    public void updateProduct(Product product, Product dbProduct) {

        productMapper.updateById(product);
        List<Long> dbSkuIds = dbProduct.getSkuList().stream().map(Sku::getSkuId).collect(Collectors.toList());
        // 将所有该商品的sku标记为已删除状态
        skuMapper.deleteByProdId(product.getProdId());

        // 接口传入sku列表
        List<Sku> skuList = product.getSkuList();

        if (CollectionUtil.isEmpty(skuList)) {
            return;
        }

        List<Sku> insertSkuList = new ArrayList<>();
        for (Sku sku : skuList) {
            sku.setIsDelete(0);
            // 如果数据库中原有sku就更新，否者就插入
            if (dbSkuIds.contains(sku.getSkuId())) {
                skuMapper.updateById(sku);
            } else {
                insertSkuList.add(sku);
            }
        }
        // 批量插入sku
        if (CollectionUtil.isNotEmpty(insertSkuList)) {
            skuMapper.insertBatch(product.getProdId(), insertSkuList);
        }

        //更新分组信息
        List<Long> tagList = product.getTagList();
        if (CollectionUtil.isNotEmpty(tagList)) {
            prodTagReferenceMapper.delete(new LambdaQueryWrapper<ProdTagReference>().eq(ProdTagReference::getProdId, product.getProdId()));
            prodTagReferenceMapper.insertBatch(dbProduct.getShopId(), product.getProdId(), tagList);
        }
    }


    @Override
    @Cacheable(cacheNames = "product", key = "#prodId")
    public Product getProductByProdId(Long prodId) {
        return productMapper.selectById(prodId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void removeProductByProdId(Long prodId) {
        Product dbProduct = getProductByProdId(prodId);

        productMapper.deleteById(prodId);

        skuMapper.deleteByProdId(prodId);

        //删除商品关联的分组标签
        prodTagReferenceMapper.delete(new LambdaQueryWrapper<ProdTagReference>()
                .eq(ProdTagReference::getProdId, prodId));

        // 删除数据库中的商品图片
//        if (StrUtil.isNotBlank(dbProduct.getImgs())) {
//            String[] imgs = dbProduct.getImgs().split(StrUtil.COMMA);
//            for (String img : imgs) {
//                attachFileService.deleteFile(img);
//            }
//        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void removeProductCacheByProdId(Long prodId) {


    }

    @Override
    public IPage<ProductDto> pageByPutAwayTime(IPage<ProductDto> page) {
        return productMapper.pageByPutAwayTime(page);
    }

    @Override
    public IPage<ProductDto> pageByTagId(Page<ProductDto> page, Long tagId) {
        return productMapper.pageByTagId(page, tagId);
    }

    @Override
    public IPage<ProductDto> moreBuyProdList(Page<ProductDto> page) {
        return productMapper.moreBuyProdList(page);
    }

    @Override
    public IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, Long categoryId) {
        return productMapper.pageByCategoryId(page, categoryId);
    }

    @Override
    public IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page page, String prodName, Integer sort, Integer orderBy) {
        IPage<SearchProdDto> searchProdDtoPage = productMapper.getSearchProdDtoPageByProdName(page, prodName, sort, orderBy);
        for (SearchProdDto searchProdDto : searchProdDtoPage.getRecords()) {
            //计算出好评率
            if (searchProdDto.getPraiseNumber() == 0 || searchProdDto.getProdCommNumber() == 0) {
                searchProdDto.setPositiveRating(0.0);
            } else {
                searchProdDto.setPositiveRating(Arith.mul(Arith.div(searchProdDto.getPraiseNumber(), searchProdDto.getProdCommNumber()), 100));
            }
        }
        return searchProdDtoPage;
    }

    @Override
    public List<TagProductDto> tagProdList() {
        return productMapper.tagProdList();
    }

    @Override
    public IPage<ProductDto> collectionProds(PageParam page, String userId) {
        return productMapper.collectionProds(page, userId);
    }


}

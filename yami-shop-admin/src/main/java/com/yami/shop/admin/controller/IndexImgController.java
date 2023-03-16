/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.IndexImgService;
import com.yami.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/admin/indexImg")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    @Autowired
    private ProductService productService;


    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:indexImg:page')")
    public ServerResponseEntity<IPage<IndexImg>> page(IndexImg indexImg, PageParam<IndexImg> page) {
        IPage<IndexImg> indexImgPage = indexImgService.page(page,
                new LambdaQueryWrapper<IndexImg>()
                        .eq(indexImg.getStatus() != null, IndexImg::getStatus, indexImg.getStatus())
                        .orderByAsc(IndexImg::getSeq));
        return ServerResponseEntity.success(indexImgPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{imgId}")
    @PreAuthorize("@pms.hasPermission('admin:indexImg:info')")
    public ServerResponseEntity<IndexImg> info(@PathVariable("imgId") Long imgId) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        IndexImg indexImg = indexImgService.getOne(new LambdaQueryWrapper<IndexImg>().eq(IndexImg::getShopId, shopId).eq(IndexImg::getImgId, imgId));
        if (Objects.nonNull(indexImg.getRelation())) {
            Product product = productService.getProductByProdId(indexImg.getRelation());
            indexImg.setPic(product.getPic());
            indexImg.setProdName(product.getProdName());
        }
        return ServerResponseEntity.success(indexImg);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin:indexImg:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid IndexImg indexImg) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        indexImg.setShopId(shopId);
        indexImg.setUploadTime(new Date());
        checkProdStatus(indexImg);
        indexImgService.save(indexImg);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:indexImg:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid IndexImg indexImg) {
        checkProdStatus(indexImg);
        indexImgService.saveOrUpdate(indexImg);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin:indexImg:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        indexImgService.deleteIndexImgByIds(ids);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    private void checkProdStatus(IndexImg indexImg) {
        if (!Objects.equals(indexImg.getType(), 0)) {
            return;
        }
        if (Objects.isNull(indexImg.getRelation())) {
            throw new YamiShopBindException("请选择商品");
        }
        Product product = productService.getById(indexImg.getRelation());
        if (Objects.isNull(product)) {
            throw new YamiShopBindException("商品信息不存在");
        }
        if (!Objects.equals(product.getStatus(), 1)) {
            throw new YamiShopBindException("该商品未上架，请选择别的商品");
        }
    }
}

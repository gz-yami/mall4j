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


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.ProdTag;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 商品分组
 *
 * @author hzm
 * @date 2019-04-18 09:08:36
 */
@RestController
@RequestMapping("/prod/prodTag")
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param prodTag 商品分组标签
     * @return 分页数据
     */
    @GetMapping("/page")
    public ServerResponseEntity<IPage<ProdTag>> getProdTagPage(PageParam<ProdTag> page, ProdTag prodTag) {
        IPage<ProdTag> tagPage = prodTagService.page(
                page, new LambdaQueryWrapper<ProdTag>()
                        .eq(prodTag.getStatus() != null, ProdTag::getStatus, prodTag.getStatus())
                        .like(prodTag.getTitle() != null, ProdTag::getTitle, prodTag.getTitle())
                        .orderByDesc(ProdTag::getSeq, ProdTag::getCreateTime));
        return ServerResponseEntity.success(tagPage);

    }


    /**
     * 通过id查询商品分组标签
     *
     * @param id id
     * @return 单个数据
     */
    @GetMapping("/info/{id}")
    public ServerResponseEntity<ProdTag> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(prodTagService.getById(id));
    }

    /**
     * 新增商品分组标签
     *
     * @param prodTag 商品分组标签
     * @return 是否新增成功
     */
    @SysLog("新增商品分组标签")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:prodTag:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdTag prodTag) {
        // 查看是否相同的标签
        List<ProdTag> list = prodTagService.list(new LambdaQueryWrapper<ProdTag>().like(ProdTag::getTitle, prodTag.getTitle()));
        if (CollectionUtil.isNotEmpty(list)) {
            throw new YamiShopBindException("标签名称已存在，不能添加相同的标签");
        }
        prodTag.setIsDefault(0);
        prodTag.setProdCount(0L);
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        prodTag.setShopId(SecurityUtils.getSysUser().getShopId());
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.save(prodTag));
    }

    /**
     * 修改商品分组标签
     *
     * @param prodTag 商品分组标签
     * @return 是否修改成功
     */
    @SysLog("修改商品分组标签")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:prodTag:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.updateById(prodTag));
    }

    /**
     * 通过id删除商品分组标签
     *
     * @param id id
     * @return 是否删除成功
     */
    @SysLog("删除商品分组标签")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('prod:prodTag:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long id) {
        ProdTag prodTag = prodTagService.getById(id);
        if (prodTag.getIsDefault() != 0) {
            throw new YamiShopBindException("默认标签不能删除");
        }
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.removeById(id));
    }

    @GetMapping("/listTagList")
    public ServerResponseEntity<List<ProdTag>> listTagList() {
        return ServerResponseEntity.success(prodTagService.listProdTag());

    }


}

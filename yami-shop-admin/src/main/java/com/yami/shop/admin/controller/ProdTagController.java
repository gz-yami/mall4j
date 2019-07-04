/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.bean.model.ProdTag;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.util.SecurityUtils;
import com.yami.shop.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<IPage<ProdTag>> getProdTagPage(PageParam<ProdTag> page, ProdTag prodTag) {
        IPage<ProdTag> tagIPage = prodTagService.page(
                page, new LambdaQueryWrapper<ProdTag>()
                        .eq(prodTag.getStatus() != null, ProdTag::getStatus, prodTag.getStatus())
                        .like(prodTag.getTitle() != null, ProdTag::getTitle, prodTag.getTitle())
                        .orderByDesc(ProdTag::getSeq));
        return ResponseEntity.ok(tagIPage);

    }


    /**
     * 通过id查询商品分组标签
     *
     * @param id id
     * @return 单个数据
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<ProdTag> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(prodTagService.getById(id));
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
    public ResponseEntity<Boolean> save(@RequestBody @Valid ProdTag prodTag) {
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        prodTag.setIsDefault(0);
        prodTag.setShopId(SecurityUtils.getSysUser().getShopId());
        prodTag.setStatus(1);
        prodTag.setProdCount(0L);
        prodTagService.removeProdTag();
        return ResponseEntity.ok(prodTagService.save(prodTag));
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
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        prodTagService.removeProdTag();
        return ResponseEntity.ok(prodTagService.updateById(prodTag));
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
    public ResponseEntity<Boolean> removeById(@PathVariable Long id) {
        ProdTag prodTag = prodTagService.getById(id);
        if (prodTag.getIsDefault() != 0) {
            throw new YamiShopBindException("默认标签不能删除");
        }
        prodTagService.removeProdTag();
        return ResponseEntity.ok(prodTagService.removeById(id));
    }

    @GetMapping("/listTagList")
    @PreAuthorize("@pms.hasPermission('prod:prodTag:update')")
    public ResponseEntity<List<ProdTag>> listTagList() {
        return ResponseEntity.ok(prodTagService.list());

    }


}

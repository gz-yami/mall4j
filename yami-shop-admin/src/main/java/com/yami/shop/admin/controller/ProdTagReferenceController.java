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
import com.yami.shop.bean.model.ProdTagReference;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.service.ProdTagReferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分组标签引用
 *
 * @author hzm
 * @date 2019-04-18 16:28:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/generator/prodTagReference" )
public class ProdTagReferenceController {

    private final ProdTagReferenceService prodTagReferenceService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param prodTagReference 分组标签引用
     * @return 分页数据
     */
    @GetMapping("/page" )
    public ResponseEntity<IPage<ProdTagReference>> getProdTagReferencePage(PageParam page, ProdTagReference prodTagReference) {
        return ResponseEntity.ok(prodTagReferenceService.page(page, new LambdaQueryWrapper<ProdTagReference>()));
    }


    /**
     * 通过id查询分组标签引用
     * @param referenceId id
     * @return 单个数据
     */
    @GetMapping("/info/{referenceId}" )
    public ResponseEntity<ProdTagReference> getById(@PathVariable("referenceId" ) Long referenceId) {
        return ResponseEntity.ok(prodTagReferenceService.getById(referenceId));
    }

    /**
     * 新增分组标签引用
     * @param prodTagReference 分组标签引用
     * @return 是否新增成功
     */
    @SysLog("新增分组标签引用" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('generator:prodTagReference:save')" )
    public ResponseEntity<Boolean> save(@RequestBody @Valid ProdTagReference prodTagReference) {
        return ResponseEntity.ok(prodTagReferenceService.save(prodTagReference));
    }

    /**
     * 修改分组标签引用
     * @param prodTagReference 分组标签引用
     * @return 是否修改成功
     */
    @SysLog("修改分组标签引用" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('generator:prodTagReference:update')" )
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid ProdTagReference prodTagReference) {
        return ResponseEntity.ok(prodTagReferenceService.updateById(prodTagReference));
    }

    /**
     * 通过id删除分组标签引用
     * @param referenceId id
     * @return 是否删除成功
     */
    @SysLog("删除分组标签引用" )
    @DeleteMapping("/{referenceId}" )
    @PreAuthorize("@pms.hasPermission('generator:prodTagReference:delete')" )
    public ResponseEntity<Boolean> removeById(@PathVariable Long referenceId) {
        return ResponseEntity.ok(prodTagReferenceService.removeById(referenceId));
    }

}

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.ProdPropRule;
import com.yami.shop.bean.model.ProdProp;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.ProdPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 参数管理
 * @author lgh
 */
@RestController
@RequestMapping("/admin/attribute")
public class AttributeController {

    @Autowired
    private ProdPropService prodPropService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin:attribute:page')")
	public ResponseEntity<IPage<ProdProp>> page(ProdProp prodProp,PageParam<ProdProp> page){
    	prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
    	prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		IPage<ProdProp> prodPropIPage = prodPropService.pagePropAndValue(prodProp,page);
		return ResponseEntity.ok(prodPropIPage);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('admin:attribute:info')")
	public ResponseEntity<ProdProp> info(@PathVariable("id") Long id){
		ProdProp prodProp = prodPropService.getById(id);
		return ResponseEntity.ok(prodProp);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin:attribute:save')")
	public ResponseEntity<Void> save(@Valid ProdProp prodProp){
		prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
		prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		prodPropService.saveProdPropAndValues(prodProp);
		return ResponseEntity.ok().build();
	}

	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin:attribute:update')")
	public ResponseEntity<Void> update(@Valid ProdProp prodProp){
		ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
		if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getSysUser().getShopId())) {
			throw new YamiShopBindException("没有权限获取该商品规格信息");
		}
		prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
		prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		prodPropService.updateProdPropAndValues(prodProp);
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin:attribute:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		prodPropService.deleteProdPropAndValues(id,ProdPropRule.ATTRIBUTE.value(),SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}
}

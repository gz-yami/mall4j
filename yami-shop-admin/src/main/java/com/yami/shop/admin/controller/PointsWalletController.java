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

import javax.validation.Valid;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yami.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;



import com.yami.shop.service.PointsWalletService;
import com.yami.shop.bean.model.PointsWallet;

/**
 *
 * @author lgh on 2019/04/10.
 */
@RestController
@RequestMapping("/admin/pointsWallet")
public class PointsWalletController {

    @Autowired
    private PointsWalletService pointsWalletService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	//@PreAuthorize("@pms.hasPermission('admin:pointsWallet:page')")
	public ResponseEntity<IPage<PointsWallet>> page(PointsWallet pointsWallet,PageParam<PointsWallet> page){
		IPage<PointsWallet> list = pointsWalletService.page(page,new LambdaQueryWrapper<PointsWallet>());
		return ResponseEntity.ok(list);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsWallet:info')")
	public ResponseEntity<PointsWallet> info(@PathVariable("id") Long id){
		PointsWallet pointsWallet = pointsWalletService.getById(id);
		return ResponseEntity.ok(pointsWallet);
	}
	
	/**
	 * 保存
	 */
	@PostMapping
	//@PreAuthorize("@pms.hasPermission('admin:pointsWallet:save')")
	public ResponseEntity<Void> save(@RequestBody @Valid PointsWallet pointsWallet){
		pointsWalletService.save(pointsWallet);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
	//@PreAuthorize("@pms.hasPermission('admin:pointsWallet:update')")
	public ResponseEntity<Void> update(@RequestBody @Valid PointsWallet pointsWallet){
		pointsWalletService.updateById(pointsWallet);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsWallet:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		pointsWalletService.removeById(id);
		return ResponseEntity.ok().build();
	}
}

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
import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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




import com.yami.shop.service.PointsChangeService;
import com.yami.shop.bean.model.PointsChange;

/**
 *
 * @author lgh on 2019/04/10.
 */
@RestController
@RequestMapping("/admin/pointsChange")
public class PointsChangeController {

    @Autowired
    private PointsChangeService pointsChangeService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
//	@PreAuthorize("@pms.hasPermission('admin:pointsChange:page')")
	public ResponseEntity<IPage<PointsChange>> page(PointsChange pointsChange,PageParam<PointsChange> page){
		IPage<PointsChange> list = pointsChangeService.page(page,new LambdaQueryWrapper<PointsChange>());
		return ResponseEntity.ok(list);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsChange:info')")
	public ResponseEntity<PointsChange> info(@PathVariable("id") Long id){
		PointsChange pointsChange = pointsChangeService.getById(id);
		return ResponseEntity.ok(pointsChange);
	}
	
	/**
	 * 保存
	 */
	@PostMapping
//	@PreAuthorize("@pms.hasPermission('admin:pointsChange:save')")
	public ResponseEntity<Void> save(@RequestBody @Valid PointsChange pointsChange){
		pointsChangeService.save(pointsChange);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
//	@PreAuthorize("@pms.hasPermission('admin:pointsChange:update')")
	public ResponseEntity<Void> update(@RequestBody @Valid PointsChange pointsChange){
		pointsChangeService.updateById(pointsChange);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsChange:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		pointsChangeService.removeById(id);
		return ResponseEntity.ok().build();
	}
}

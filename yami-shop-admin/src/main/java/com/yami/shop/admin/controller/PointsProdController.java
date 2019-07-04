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




import com.yami.shop.service.PointsProdService;
import com.yami.shop.bean.model.PointsProd;

/**
 *
 * @author lgh on 2019/04/10.
 */
@RestController
@RequestMapping("/admin/pointsProd")
public class PointsProdController {

    @Autowired
    private PointsProdService pointsProdService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	//@PreAuthorize("@pms.hasPermission('admin:pointsProd:page')")
	public ResponseEntity<IPage<PointsProd>> page(PointsProd pointsProd,PageParam<PointsProd> page){
		IPage<PointsProd> list = pointsProdService.page(page,new LambdaQueryWrapper<PointsProd>());
		return ResponseEntity.ok(list);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsProd:info')")
	public ResponseEntity<PointsProd> info(@PathVariable("id") Long id){
		PointsProd pointsProd = pointsProdService.getById(id);
		return ResponseEntity.ok(pointsProd);
	}
	
	/**
	 * 保存
	 */
	@PostMapping
//	@PreAuthorize("@pms.hasPermission('admin:pointsProd:save')")
	public ResponseEntity<Void> save(@RequestBody @Valid PointsProd pointsProd){
		pointsProdService.save(pointsProd);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
//@PreAuthorize("@pms.hasPermission('admin:pointsProd:update')")
	public ResponseEntity<Void> update(@RequestBody @Valid PointsProd pointsProd){
		pointsProdService.updateById(pointsProd);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	//@PreAuthorize("@pms.hasPermission('admin:pointsProd:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		pointsProdService.removeById(id);
		return ResponseEntity.ok().build();
	}
}

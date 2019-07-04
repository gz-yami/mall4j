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
import java.util.Date;
import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.security.util.SecurityUtils;
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




import com.yami.shop.service.PointsService;
import com.yami.shop.bean.model.Points;

/**
 *
 * @author lgh on 2019/04/10.
 */
@RestController
@RequestMapping("/points/points")
public class PointsController {

    @Autowired
    private PointsService pointsService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('points:points:page')")
	public ResponseEntity<IPage<Points>> page(Points points,PageParam<Points> page){
		IPage<Points> list = pointsService.page(page,new LambdaQueryWrapper<Points>());
		return ResponseEntity.ok(list);
	}


	/**
	 * 分页获取(携带user)
	 */
	@GetMapping("/page/anduser")
	@PreAuthorize("@pms.hasPermission('points:points:page')")
	public ResponseEntity<IPage<Points>> andUserPage( Points points ,PageParam< Points> page){
		points.setShopId(SecurityUtils.getSysUser().getShopId());
		IPage<Points> list = pointsService.getPointsAndSysUserPage(page,points);
		return ResponseEntity.ok(list);
	}

	/**
	 * 分页获取(只取name和id)
	 */
	@GetMapping("/page/name")
	@PreAuthorize("@pms.hasPermission('points:points:page')")
	public ResponseEntity<List<Points>> nameAndIdPage(Points points){
		points.setShopId(SecurityUtils.getSysUser().getShopId());
		List<Points> list = pointsService.getNameAndId(points);
		return ResponseEntity.ok(list);
	}


    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('points:points:info')")
	public ResponseEntity<Points> info(@PathVariable("id") Long id){
		Points points = pointsService.getById(id);
		return ResponseEntity.ok(points);
	}



	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('points:points:save')")
	public ResponseEntity<Void> save(@RequestBody @Valid Points points){
		points.setModifier(SecurityUtils.getSysUser().getUserId());
		points.setUpdateTime(new Date());
		points.setShopId(SecurityUtils.getSysUser().getShopId());
		pointsService.save(points);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('points:points:update')")
	public ResponseEntity<Void> update(@RequestBody @Valid Points points){
		points.setModifier(SecurityUtils.getSysUser().getUserId());
		points.setUpdateTime(new Date());
		pointsService.updateById(points);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('points:points:delete')")
	public ResponseEntity<Void> delete(@RequestBody List<Long> ids){
		pointsService.removeByIds(ids);
		return ResponseEntity.ok().build();
	}
}

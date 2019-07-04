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


import cn.hutool.core.util.StrUtil;
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





import com.yami.shop.service.HotSearchService;
import com.yami.shop.bean.model.HotSearch;

/**
 *
 * @author lgh on 2019/03/27.
 */
@RestController
@RequestMapping("/admin/hotSearch")
public class HotSearchController {

    @Autowired
    private HotSearchService hotSearchService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:page')")
	public ResponseEntity<IPage<HotSearch>> page(HotSearch hotSearch,PageParam<HotSearch> page){
		IPage<HotSearch> hotSearchs = hotSearchService.page(page,new LambdaQueryWrapper<HotSearch>()
			.eq(HotSearch::getShopId, SecurityUtils.getSysUser().getShopId())
			.like(StrUtil.isNotBlank(hotSearch.getContent()), HotSearch::getContent,hotSearch.getContent())
				.like(StrUtil.isNotBlank(hotSearch.getTitle()), HotSearch::getTitle,hotSearch.getTitle())
			.eq(hotSearch.getStatus()!=null, HotSearch::getStatus,hotSearch.getStatus())
		);
		return ResponseEntity.ok(hotSearchs);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:info')")
	public ResponseEntity<HotSearch> info(@PathVariable("id") Long id){
		HotSearch hotSearch = hotSearchService.getById(id);
		return ResponseEntity.ok(hotSearch);
	}
	
	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:save')")
	public ResponseEntity<Void> save(@RequestBody @Valid HotSearch hotSearch){
		hotSearch.setRecDate(new Date());
		hotSearch.setShopId(SecurityUtils.getSysUser().getShopId());
		hotSearchService.save(hotSearch);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:update')")
	public ResponseEntity<Void> update(@RequestBody @Valid HotSearch hotSearch){
		hotSearchService.updateById(hotSearch);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:delete')")
	public ResponseEntity<Void> delete(@RequestBody List<Long> ids){
		hotSearchService.removeByIds(ids);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}
}

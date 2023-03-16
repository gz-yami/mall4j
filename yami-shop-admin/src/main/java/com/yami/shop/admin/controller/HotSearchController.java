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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.HotSearch;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.HotSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;

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
	public ServerResponseEntity<IPage<HotSearch>> page(HotSearch hotSearch,PageParam<HotSearch> page){
		IPage<HotSearch> hotSearchs = hotSearchService.page(page,new LambdaQueryWrapper<HotSearch>()
			.eq(HotSearch::getShopId, SecurityUtils.getSysUser().getShopId())
			.like(StrUtil.isNotBlank(hotSearch.getContent()), HotSearch::getContent,hotSearch.getContent())
				.like(StrUtil.isNotBlank(hotSearch.getTitle()), HotSearch::getTitle,hotSearch.getTitle())
			.eq(hotSearch.getStatus()!=null, HotSearch::getStatus,hotSearch.getStatus())
				.orderByAsc(HotSearch::getSeq)
		);
		return ServerResponseEntity.success(hotSearchs);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	public ServerResponseEntity<HotSearch> info(@PathVariable("id") Long id){
		HotSearch hotSearch = hotSearchService.getById(id);
		return ServerResponseEntity.success(hotSearch);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:save')")
	public ServerResponseEntity<Void> save(@RequestBody @Valid HotSearch hotSearch){
		hotSearch.setRecDate(new Date());
		hotSearch.setShopId(SecurityUtils.getSysUser().getShopId());
		hotSearchService.save(hotSearch);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
		return ServerResponseEntity.success();
	}

	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:update')")
	public ServerResponseEntity<Void> update(@RequestBody @Valid HotSearch hotSearch){
		hotSearchService.updateById(hotSearch);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
		return ServerResponseEntity.success();
	}

	/**
	 * 删除
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('admin:hotSearch:delete')")
	public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids){
		hotSearchService.removeByIds(ids);
		//清除缓存
		hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
		return ServerResponseEntity.success();
	}
}

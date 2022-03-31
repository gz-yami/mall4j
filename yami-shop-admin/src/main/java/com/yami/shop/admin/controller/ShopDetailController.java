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
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.param.ShopDetailParam;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.ShopDetailService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



/**
 *
 * @author lgh on 2018/08/29.
 */
@Controller
@RequestMapping("/shop/shopDetail")
public class ShopDetailController {

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
	private MapperFacade mapperFacade;


	/**
	 * 修改分销开关
	 */
	@PutMapping("/isDistribution")
	//@PreAuthorize("@pms.hasPermission('shop:shopDetail:update')")
	public ResponseEntity<Void> updateIsDistribution(@RequestParam Integer isDistribution){
		ShopDetail shopDetail=new ShopDetail();
		shopDetail.setShopId(SecurityUtils.getSysUser().getShopId());
		shopDetail.setIsDistribution(isDistribution);
		shopDetailService.updateById(shopDetail);
		// 更新完成后删除缓存
		shopDetailService.removeShopDetailCacheByShopId(shopDetail.getShopId());
		return ResponseEntity.ok().build();
	}
	/**
	 * 获取信息
	 */
	@GetMapping("/info")
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:info')")
	public ResponseEntity<ShopDetail> info(){
		ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok(shopDetail);
	}


	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:page')")
	public ResponseEntity<IPage<ShopDetail>> page(ShopDetail shopDetail,PageParam<ShopDetail> page){
		IPage<ShopDetail> shopDetails = shopDetailService.page(page,
				new LambdaQueryWrapper<ShopDetail>()
						.like(StrUtil.isNotBlank(shopDetail.getShopName()),ShopDetail::getShopName,shopDetail.getShopName())
						.orderByDesc(ShopDetail::getShopId));
		return ResponseEntity.ok(shopDetails);
	}
    
	/**
	 * 获取信息
	 */
	@GetMapping("/info/{shopId}")
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:info')")
	public ResponseEntity<ShopDetail> info(@PathVariable("shopId") Long shopId){
		ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
		// 店铺图片
		return ResponseEntity.ok(shopDetail);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:save')")
	public ResponseEntity<Void> save(@Valid ShopDetailParam shopDetailParam){
		ShopDetail shopDetail = mapperFacade.map(shopDetailParam, ShopDetail.class);
		shopDetail.setCreateTime(new Date());
		shopDetail.setShopStatus(1);
		shopDetailService.save(shopDetail);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:update')")
	public ResponseEntity<Void> update(@Valid ShopDetailParam shopDetailParam){
		ShopDetail daShopDetail = shopDetailService.getShopDetailByShopId(shopDetailParam.getShopId());
		ShopDetail shopDetail = mapperFacade.map(shopDetailParam, ShopDetail.class);
		shopDetail.setUpdateTime(new Date());
		shopDetailService.updateShopDetail(shopDetail,daShopDetail);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		shopDetailService.deleteShopDetailByShopId(id);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 更新店铺状态
	 */
	@PutMapping("/shopStatus")
	@PreAuthorize("@pms.hasPermission('shop:shopDetail:shopStatus')")
	public ResponseEntity<Void> shopStatus(@RequestParam Long shopId,@RequestParam Integer shopStatus){
		ShopDetail shopDetail = new ShopDetail();
		shopDetail.setShopId(shopId);
		shopDetail.setShopStatus(shopStatus);
		shopDetailService.updateById(shopDetail);
		// 更新完成后删除缓存
		shopDetailService.removeShopDetailCacheByShopId(shopDetail.getShopId());
		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * 获取所有的店铺名称
	 */
    @GetMapping("/listShopName")
	public ResponseEntity<List<ShopDetail>> listShopName(){
		List<ShopDetail> list = shopDetailService.list().stream().map((dbShopDetail) ->{
			ShopDetail shopDetail = new ShopDetail();
			shopDetail.setShopId(dbShopDetail.getShopId());
			shopDetail.setShopName(dbShopDetail.getShopName());
			return shopDetail;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}
}

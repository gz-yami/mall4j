/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.dao.ShopDetailMapper;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.ShopDetailService;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 *
 * @author lgh on 2018/08/29.
 */
@Service
public class ShopDetailServiceImpl extends ServiceImpl<ShopDetailMapper, ShopDetail> implements ShopDetailService {

    @Autowired
    private ShopDetailMapper shopDetailMapper;
    
    @Autowired
    private AttachFileService attachFileService;

    @Override
    @Transactional(rollbackFor=Exception.class)
	@CacheEvict(cacheNames = "shop_detail", key = "#shopDetail.shopId")
    public void updateShopDetail(ShopDetail shopDetail,ShopDetail dbShopDetail) {
    	// 更新除数据库中的信息，再删除图片
    	shopDetailMapper.updateById(shopDetail);
//    	if (!Objects.equals(dbShopDetail.getShopLogo(), shopDetail.getShopLogo())) {
//    		// 删除logo
//    		attachFileService.deleteFile(shopDetail.getShopLogo());
//    	}
//    	// 店铺图片
//    	String shopPhotos = shopDetail.getShopPhotos();
//    	String[] shopPhotoArray =StrUtil.isBlank(shopPhotos)?new String[]{}: shopPhotos.split(",");
//
//    	// 数据库中的店铺图片
//    	String dbShopPhotos = dbShopDetail.getShopPhotos();
//    	String[] dbShopPhotoArray =StrUtil.isBlank(dbShopPhotos)?new String[]{}: dbShopPhotos.split(",");
//    	for (String dbShopPhoto : dbShopPhotoArray) {
//    		// 如果新插入的图片中没有旧数据中的图片，则删除旧数据中的图片
//			if (!ArrayUtil.contains(shopPhotoArray, dbShopPhoto)) {
//				attachFileService.deleteFile(dbShopPhoto);
//			}
//		}
    }
    
	@Override
	@Transactional(rollbackFor=Exception.class)
	@CacheEvict(cacheNames = "shop_detail", key = "#shopId")
	public void deleteShopDetailByShopId(Long shopId) {
		shopDetailMapper.deleteById(shopId);
	}

	@Override
	@Cacheable(cacheNames = "shop_detail", key = "#shopId")
	public ShopDetail getShopDetailByShopId(Long shopId) {
		return shopDetailMapper.selectById(shopId);
	}

	@Override
	@CacheEvict(cacheNames = "shop_detail", key = "#shopId")
	public void removeShopDetailCacheByShopId(Long shopId) {
	}
}

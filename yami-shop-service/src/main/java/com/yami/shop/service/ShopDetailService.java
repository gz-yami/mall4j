/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.ShopDetail;

/**
 *
 * @author lgh on 2018/08/29.
 */
public interface ShopDetailService extends IService<ShopDetail> {

	void updateShopDetail(ShopDetail shopDetail,ShopDetail dbShopDetail);

	void deleteShopDetailByShopId(Long id);

	/**
	 * 根据店铺id获取店铺信息
	 * @param shopId
	 * @return
	 */
	ShopDetail getShopDetailByShopId(Long shopId);

	void removeShopDetailCacheByShopId(Long shopId);
}

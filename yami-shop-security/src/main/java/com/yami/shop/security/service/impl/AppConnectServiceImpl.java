/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.security.dao.AppConnectMapper;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.AppConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



/**
 *
 * @author lgh on 2018/09/07.
 */
@Service
public class AppConnectServiceImpl extends ServiceImpl<AppConnectMapper, AppConnect> implements AppConnectService {

    @Autowired
    private AppConnectMapper appConnectMapper;

	/**
	 * YamiUserServiceImpl#insertUserIfNecessary 将会清楚该缓存信息
	 * @param bizUserId
	 * @param app
	 * @return
	 */
	@Override
	@Cacheable(cacheNames = "AppConnect", key = "#app.value() + ':' + #bizUserId")
	public AppConnect getByBizUserId(String bizUserId, App app) {
		return appConnectMapper.getByBizUserId(bizUserId, app.value());
	}

}

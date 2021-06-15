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

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.security.dao.AppConnectMapper;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.AppConnectService;
import com.yami.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;


/**
 *
 * @author lgh on 2018/09/07.
 */
@Service
public class AppConnectServiceImpl extends ServiceImpl<AppConnectMapper, AppConnect> implements AppConnectService {

    @Autowired
    private AppConnectMapper appConnectMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User registerOrBindUser(User user, AppConnect appConnect, Integer appId) {
		if (StrUtil.isBlank(user.getUserId())) {
			if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUserMobile, user.getUserMobile())) > 0) {
				// 该电话号码已存在
				throw new YamiShopBindException("该电话号码已存在");
			}
			String userId = IdUtil.simpleUUID();
			user.setUserId(userId);
			userMapper.insert(user);
		} else {
			if (appConnect != null&& StrUtil.isBlank(user.getPic())) {
				User userParam = new User();
				userParam.setUserId(user.getUserId());
				userParam.setModifyTime(new Date());
				userParam.setPic(appConnect.getImageUrl());
				userService.updateById(userParam);
			}
		}
		if (appConnect == null) {
			// 避免重复插入数据
			if (appConnectMapper.getByBizUserId(user.getUserId(), appId) != null) {
				return user;
			}
			appConnect = new AppConnect();
			appConnect.setUserId(user.getUserId());
			appConnect.setNickName(user.getNickName());
			appConnect.setImageUrl(user.getPic());

			// 0表示是系统的用户，不是第三方的
			appConnect.setAppId(appId);
			appConnectMapper.insert(appConnect);
		} else if (StrUtil.isBlank(appConnect.getUserId()) || Objects.isNull(appId)) {
			appConnect.setAppId(appId);
			appConnect.setUserId(user.getUserId());
			appConnect.setUserId(user.getUserId());
			appConnectMapper.updateById(appConnect);
		}

		return user;
	}
}

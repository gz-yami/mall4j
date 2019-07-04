/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户详细信息
 */
@Getter
public class YamiUser extends User {

	/**
	 * 用户ID
	 */
	private String userId;

	private String bizUserId;

	@Setter
	private String pic;

	@Setter
	private String name;

	@Setter
	private boolean debugger;

	public YamiUser(String userId, String bizUserId, Integer appId, boolean enabled) {
		super(appId + StrUtil.COLON + bizUserId, "", enabled,true, true, true , Collections.emptyList());
		this.userId = userId;
		this.bizUserId = bizUserId;
	}
}

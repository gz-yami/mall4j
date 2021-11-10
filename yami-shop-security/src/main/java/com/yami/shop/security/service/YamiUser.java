/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户详细信息
 */
@Data
public class YamiUser implements UserDetails {

	/**
	 * 用户ID
	 */
	private String userId;

	private String bizUserId;

	private String pic;

	private String name;

	private boolean debugger;

	private String password;

	private Integer appType;

	private boolean enabled;

	/**
	 * 自提点Id
	 */
	private Long stationId;

	/**
	 * 店铺Id
	 */
	private Long shopId;
	/**
	 * 小程序session_key
	 */
	private String sessionKey;

	public YamiUser() {

	}

	public YamiUser(String userId, String bizUserId, Integer appType, boolean enabled) {
		this.userId = userId;
		this.bizUserId = bizUserId;
		this.appType = appType;
		this.enabled = enabled;
	}

	public YamiUser(String userId, String password, boolean enabled) {
		this.userId = userId;
		this.password = password;
		this.enabled = enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return StrUtil.isBlank(userId)? bizUserId: userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}

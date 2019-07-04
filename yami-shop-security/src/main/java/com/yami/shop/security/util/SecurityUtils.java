/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.util;


import com.yami.shop.security.exception.UnauthorizedExceptionBase;
import com.yami.shop.security.service.YamiSysUser;
import com.yami.shop.security.service.YamiUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public YamiUser getUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof YamiUser) {
			return (YamiUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取普通用户信息");
	}

	/**
	 * 获取系统用户
	 */
	public YamiSysUser getSysUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof YamiSysUser) {
			return (YamiSysUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取系统用户信息");
	}
}

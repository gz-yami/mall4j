/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.controller;


import java.awt.*;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.yami.shop.security.constants.SecurityConstants;

import com.yami.shop.security.service.YamiSysUser;
import com.yami.shop.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.common.util.SimpleCaptcha;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 登录相关
 * @author lgh
 */
@Controller
@AllArgsConstructor
@ApiIgnore
public class SysLoginController {

	private final CacheManager cacheManager;


	@GetMapping("/captcha.jpg")
	public void login(HttpServletResponse response,String uuid) {
		//定义图形验证码的长、宽、验证码字符数、干扰元素个数
		SimpleCaptcha simpleCaptcha = new SimpleCaptcha(200, 50, 4, 20);
		try {
			simpleCaptcha.write(response.getOutputStream());
			RedisUtil.set(SecurityConstants.SPRING_SECURITY_RESTFUL_IMAGE_CODE+uuid, simpleCaptcha.getCode(), 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出
	 */
	@PostMapping(value = "/sys/logout")
	public ResponseEntity<String> logout() {
		Cache cache = cacheManager.getCache("yami_sys_user");
		if (cache != null) {
			cache.evict(SecurityUtils.getSysUser().getUsername());
		}
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().build();
	}

}

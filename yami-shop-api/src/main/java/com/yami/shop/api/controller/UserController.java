/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.yami.shop.bean.app.dto.UserDto;
import com.yami.shop.bean.app.param.UserInfoParam;
import com.yami.shop.bean.model.User;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import cn.hutool.core.bean.BeanUtil;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import java.util.Date;
/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/user")
@Tag(name = "用户接口")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	
	/**
	 * 查看用户接口
	 */
	@GetMapping("/userInfo")
	@Operation(summary = "查看用户信息" , description = "根据用户ID（userId）获取用户信息")
	public ServerResponseEntity<UserDto> userInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
		return ServerResponseEntity.success(userDto);
	}

	@PutMapping("/setUserInfo")
	@Operation(summary = "设置用户信息" , description = "设置用户信息")
	public ServerResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = new User();
		user.setUserId(userId);
		user.setPic(userInfoParam.getAvatarUrl());
		user.setNickName(userInfoParam.getNickName());
		userService.updateById(user);
		return ServerResponseEntity.success();
	}

	@PutMapping("/updatePwd")
	@Operation(summary = "修改密码" , description = "登录用户修改自己的密码，需提供原密码")
	public ServerResponseEntity<Void> updatePwd(@Valid @RequestBody UserRegisterParam userPwdUpdateParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		if (user == null) {
			throw new YamiShopBindException("无法获取用户信息");
		}
		String oldPassword = passwordManager.decryptPassword(userPwdUpdateParam.getOldPassWord());
		if (StrUtil.isBlank(oldPassword) || !passwordEncoder.matches(oldPassword, user.getLoginPassword())) {
			throw new YamiShopBindException("原密码不正确");
		}
		String decryptPassword = passwordManager.decryptPassword(userPwdUpdateParam.getPassWord());
		if (StrUtil.isBlank(decryptPassword)) {
			throw new YamiShopBindException("新密码不能为空");
		}
		if (passwordEncoder.matches(decryptPassword, user.getLoginPassword())) {
			throw new YamiShopBindException("新密码不能与原密码相同");
		}
		user.setModifyTime(new Date());
		user.setLoginPassword(passwordEncoder.encode(decryptPassword));
		userService.updateById(user);
		tokenStore.deleteAllToken(String.valueOf(SysTypeEnum.ORDINARY.value()), userId);
		return ServerResponseEntity.success();
	}
}

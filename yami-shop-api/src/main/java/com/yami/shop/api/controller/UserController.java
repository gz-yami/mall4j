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
}

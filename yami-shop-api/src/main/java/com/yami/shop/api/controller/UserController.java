/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.util.CacheManagerUtil;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yami.shop.bean.app.dto.UserDto;
import com.yami.shop.bean.app.param.UserInfoParam;
import com.yami.shop.bean.model.User;
import com.yami.shop.service.UserService;

import cn.hutool.extra.emoji.EmojiUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

@RestController
@RequestMapping("/p/user")
@Api(tags="用户接口")
@AllArgsConstructor
public class UserController {

	private final UserService userService;
    
	private final MapperFacade mapperFacade;

	private final CacheManagerUtil cacheManagerUtil;
    /**
     * 查看用户接口
     */
    @GetMapping("/userInfo")
    @ApiOperation(value="查看用户信息", notes="根据用户ID（userId）获取用户信息")
    public ResponseEntity<UserDto> userInfo() {
    	String userId = SecurityUtils.getUser().getUserId();
    	User user = userService.getById(userId);
    	UserDto userDto = mapperFacade.map(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }
    
    @PutMapping("/setUserInfo")
    @ApiOperation(value="设置用户信息", notes="设置用户信息")
    public ResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
    	String userId = SecurityUtils.getUser().getUserId();
    	User user = new User();
    	user.setUserId(userId);
    	user.setPic(userInfoParam.getAvatarUrl());
    	user.setNickName(EmojiUtil.toAlias(userInfoParam.getNickName()));
    	userService.updateById(user);
		String cacheKey = App.MINI.value() + StrUtil.COLON + SecurityUtils.getUser().getBizUserId();
		cacheManagerUtil.evictCache("yami_user", cacheKey);
        return ResponseEntity.ok(null);
    }
}

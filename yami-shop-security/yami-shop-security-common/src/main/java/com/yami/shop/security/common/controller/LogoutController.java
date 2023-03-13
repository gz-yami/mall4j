/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.controller;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.manager.TokenStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@RestController
@Tag(name = "注销")
public class LogoutController {

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/logOut")
    @Operation(summary = "退出登陆" , description = "点击退出登陆，清除token，清除菜单缓存")
    public ServerResponseEntity<Void> logOut(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            return ServerResponseEntity.success();
        }
        // 删除该用户在该系统当前的token
        tokenStore.deleteCurrentToken(accessToken);
        return ServerResponseEntity.success();
    }
}

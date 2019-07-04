/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.permission;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 接口权限判断工具
 */
@Slf4j
@Component("pms")
public class PermissionService {
    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permission 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
    }
}
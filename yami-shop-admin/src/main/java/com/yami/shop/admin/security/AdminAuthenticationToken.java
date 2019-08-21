/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.security;

import com.yami.shop.security.token.MyAuthenticationToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 系统用户账号密码登陆
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminAuthenticationToken extends MyAuthenticationToken {

    private String sessionUUID;

    private String imageCode;

    public AdminAuthenticationToken(UserDetails principal, Object credentials) {
        super(principal, credentials, principal.getAuthorities());
    }


}

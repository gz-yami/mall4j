/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.token;

import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 二维码Token
 */
@NoArgsConstructor
public class MpAuthenticationToken extends MyAuthenticationToken {


    public MpAuthenticationToken(UserDetails principal, Object credentials) {
        super(principal, credentials, principal.getAuthorities());
    }
}

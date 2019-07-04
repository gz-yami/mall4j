/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.exception;

public class UsernameNotFoundException extends YamiAuth2Exception {

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "username_not_found";
    }
}

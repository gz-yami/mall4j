package com.yami.shop.admin.security;

import com.yami.shop.common.util.Json;
import com.yami.shop.security.provider.AuthenticationTokenParser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * AdminAuthenticationTokenParser
 *
 * @author hanfeng
 * @date 2019-08-21
 */
@Component
public class AdminAuthenticationTokenParser implements AuthenticationTokenParser {
    @Override
    public AbstractAuthenticationToken parse(String authenticationTokenStr) {
        AdminAuthenticationToken authRequest = Json.parseObject(authenticationTokenStr, AdminAuthenticationToken.class);
        return authRequest;
    }
}

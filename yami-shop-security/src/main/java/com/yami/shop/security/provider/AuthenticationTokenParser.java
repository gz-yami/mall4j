package com.yami.shop.security.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * AuthenticationTokenParser
 *
 * @author hanfeng
 * @date 2019-08-21
 */
public interface AuthenticationTokenParser {
    AbstractAuthenticationToken parse(String authenticationTokenStr);
}

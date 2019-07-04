/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.provider;

import com.yami.shop.security.enums.App;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义 AuthenticationProvider， 以使用自定义的 MyAuthenticationToken
 * @author LGH
 */
@Slf4j
public abstract class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider, InitializingBean {



    protected abstract void additionalAuthenticationChecks(UserDetails var1, Authentication var2) throws AuthenticationException;

    @Override
    public final void afterPropertiesSet() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() == null?"NONE_PROVIDED":authentication.getName();
        UserDetails user;

        try {
            user = this.retrieveUser(username, authentication);
        } catch (UsernameNotFoundException var6) {
            log.debug("User \'" + username + "\' not found");

            throw var6;
        }

        this.additionalAuthenticationChecks(user, authentication);

        return this.createSuccessAuthentication(authentication, user);
    }

    protected abstract Authentication createSuccessAuthentication(Authentication authentication, UserDetails user);

    protected abstract App getAppInfo();

    protected abstract UserDetails retrieveUser(String username, Authentication authentication) throws AuthenticationException;



}

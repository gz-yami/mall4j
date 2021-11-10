/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义AbstractAuthenticationToken，
 * @author SJL
 */
@Getter
@Setter
public class AuthenticationToken implements Authentication,
        CredentialsContainer {

    private Collection<GrantedAuthority> authorities;
    private UserDetails details;

    /**
     * 用户名
     */
    protected String principal;

    /**
     * 密码
     */
    protected Object credentials;

    /**
     * uuid，现在用于验证码
     */
    private String sessionUUID;

    private boolean authenticated = false;

    public void setDetails(UserDetails details) {
        this.details = details;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return details.getAuthorities();
    }


    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    @Override
    public void eraseCredentials() {
        eraseSecret(getCredentials());
        eraseSecret(getPrincipal());
        eraseSecret(details);
    }

    private void eraseSecret(Object secret) {
        if (secret instanceof CredentialsContainer) {
            ((CredentialsContainer) secret).eraseCredentials();
        }
    }

}

/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.security;


import cn.binarywang.wx.miniapp.api.WxMaService;
import com.yami.shop.security.filter.LoginAuthenticationFilter;
import com.yami.shop.security.handler.LoginAuthFailedHandler;
import com.yami.shop.security.handler.LoginAuthSuccessHandler;
import com.yami.shop.security.provider.MiniAppAuthenticationProvider;
import com.yami.shop.security.service.YamiUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 */
@Configuration
@Order(90)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自动注入UserDetailsService
     */
    @Autowired
    private YamiUserDetailsService yamiUserDetailsService;
    @Autowired
    private LoginAuthSuccessHandler loginAuthSuccessHandler;
    @Autowired
    private LoginAuthFailedHandler loginAuthFailedHandler;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private MiniAppAuthenticationProvider miniAppAuthenticationProvider;

    @Override
    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    /**
     * 用户验证
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(miniAppAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter() {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(loginAuthSuccessHandler);
        filter.setAuthenticationFailureHandler(loginAuthFailedHandler);
        return filter;
    }

}

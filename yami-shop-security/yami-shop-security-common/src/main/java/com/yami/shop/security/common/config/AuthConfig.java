/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.config;

import cn.hutool.core.util.ArrayUtil;
import com.yami.shop.security.common.adapter.AuthConfigAdapter;
import com.yami.shop.security.common.adapter.DefaultAuthConfigAdapter;
import com.yami.shop.security.common.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.servlet.DispatcherType;

/**
 * 授权配置
 *
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class AuthConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    @ConditionalOnMissingBean
    public AuthConfigAdapter authConfigAdapter() {
        return new DefaultAuthConfigAdapter();
    }


    @Bean
    @Lazy
    public FilterRegistrationBean<AuthFilter> filterRegistration(AuthConfigAdapter authConfigAdapter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        // 添加过滤器
        registration.setFilter(authFilter);
        // 设置过滤路径，/*所有路径
        registration.addUrlPatterns(ArrayUtil.toArray(authConfigAdapter.pathPatterns(), String.class));
        registration.setName("authFilter");
        // 设置优先级
        registration.setOrder(0);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

}

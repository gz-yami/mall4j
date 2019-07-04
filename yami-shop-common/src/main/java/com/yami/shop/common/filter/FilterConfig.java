/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @author lgh
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        //添加过滤器
        registration.setFilter(new XssFilter());
        //设置过滤路径，/*所有路径
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        //设置优先级
        registration.setOrder(Integer.MAX_VALUE);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.config;

import cn.hutool.core.lang.Snowflake;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lanhai
 */
@Configuration
@AllArgsConstructor
public class AdminBeanConfig {

    private final AdminConfig adminConfig;

    @Bean
    public Snowflake snowflake() {
    	return new Snowflake(adminConfig.getWorkerId(), adminConfig.getDatacenterId());
    }
}

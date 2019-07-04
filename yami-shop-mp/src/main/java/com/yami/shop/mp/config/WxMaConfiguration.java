/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.mp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import com.yami.shop.mp.component.WxMaInRedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置文件
 * @author LGH
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass(WxMaService.class)
public class WxMaConfiguration {


    private final WxMaInRedisConfig wxMaInRedisConfig;

    @Bean
    public WxMaService wxMaService() {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(wxMaInRedisConfig);
        return service;
    }


}

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

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.yami.shop.mp.config.bean.WxMiniApp;
import com.yami.shop.mp.config.bean.WxMp;
import com.yami.shop.mp.config.bean.WxPay;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 微信公众号配置文件
 *
 * @author LGH
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(WxPayService.class)
public class WxPayConfiguration {

    private final WxMp wxMp;

    private final WxMiniApp wxMiniApp;

    private final WxPay wxPay;

    @Value("${spring.profiles.active}")
    private String profile;


    @Bean
    public WxPayService wxMiniPayService() {
        return getWxMpPayServiceByAppId(wxMiniApp.getAppid());
    }

    @Bean
    public WxPayService wxMpPayService() {
        return getWxMpPayServiceByAppId(wxMp.getAppid());
    }


    private WxPayService getWxMpPayServiceByAppId(String appid) {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appid);
        payConfig.setMchId(wxPay.getMchId());
        payConfig.setMchKey(wxPay.getMchKey());
        payConfig.setKeyPath(wxPay.getKeyPath());
        payConfig.setSignType(WxPayConstants.SignType.MD5);

        WxPayService wxPayService = new WxPayServiceImpl();

//      打开下面的代码，开启沙箱模式
//        if (Objects.equals(profile, "dev")) {
//            String sandboxSignKey = null;
//            try {
//                wxPayService.setConfig(payConfig);
//                sandboxSignKey = wxPayService.getSandboxSignKey();
//            } catch (WxPayException e) {
//                e.printStackTrace();
//            }
//            payConfig.setUseSandboxEnv(true);
//            payConfig.setMchKey(sandboxSignKey);
//        }

        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}

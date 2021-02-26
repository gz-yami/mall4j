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
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.yami.shop.common.util.Json;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.service.YamiUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 账号密码登录
 */
@Component
public class WebLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final YamiUserDetailsService yamiUserDetailsService;

    private final WxMaService wxMaService;

    @Autowired
    public WebLoginAuthenticationFilter(YamiUserDetailsService yamiUserDetailsService, WxMaService wxMaService) {
        super("/webLogin");
        this.yamiUserDetailsService = yamiUserDetailsService;
        this.wxMaService = wxMaService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!ServletUtil.METHOD_POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String requestBody = getStringFromStream(request);

        if (StrUtil.isBlank(requestBody)) {
            throw new AuthenticationServiceException("无法获取输入信息");
        }
        WebAuthenticationToken authentication  =  Json.parseObject(requestBody, WebAuthenticationToken.class);
        String userMail = String.valueOf(authentication.getPrincipal());
        String loginPassword = String.valueOf(authentication.getCredentials());

        YamiUser loadedUser = null;

//        WxMaJscode2SessionResult session = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(App.H5.value());
        loadedUser = yamiUserDetailsService.loadUserByUserMail(userMail,loginPassword);
//        try {
//
////            session = wxMaService.getUserService().getSessionInfo(code);
//
//            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI,session.getOpenid());
//        } catch (WxErrorException e) {
//            throw new WxErrorExceptionBase(e.getMessage());
//        } catch (UsernameNotFoundExceptionBase var6) {
//            if (session == null) {
//                throw new WxErrorExceptionBase("无法获取用户登陆信息");
//            }
//            appConnect.setBizUserId(session.getOpenid());
//            appConnect.setBizUnionid(session.getUnionid());
//            yamiUserDetailsService.insertUserIfNecessary(appConnect);
//        }

//        if (loadedUser == null) {
//            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI, appConnect.getBizUserId());
//        }
//        MiniAppAuthenticationToken result = new MiniAppAuthenticationToken(loadedUser, authentication.getCredentials());
        WebAuthenticationToken result = new WebAuthenticationToken(loadedUser, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }


    private String getStringFromStream(HttpServletRequest req) {
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            return new String(bytes, 0, nTotalRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
}

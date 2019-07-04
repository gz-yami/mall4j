/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.yami.shop.common.util.Json;
import com.yami.shop.security.constants.SecurityConstants;
import com.yami.shop.security.exception.UnknownGrantTypeExceptionBase;
import com.yami.shop.security.token.AdminAuthenticationToken;
import com.yami.shop.security.token.MiniAppAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义登陆filter，新增登陆方式：验证码、二维码扫码、账号密码；
 * 小程序登陆：此时principal为code
 *       post:http://127.0.0.1:8086/login?grant_type=mini_app
 *       {principal:code}
 * 管理员登陆：
 *       post: http://127.0.0.1:8086/login?grant_type=admin
 *       {principal:username,credentials:password}
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!ServletUtil.METHOD_POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String type = obtainParameter(request, OAuth2Utils.GRANT_TYPE);

        AbstractAuthenticationToken authRequest = null;

        String requestBody = getStringFromStream(request);

        if (StrUtil.isBlank(requestBody)) {
            throw new AuthenticationServiceException("无法获取输入信息");
        }

        // 小程序通过code登陆
        if(SecurityConstants.SPRING_SECURITY_RESTFUL_TYPE_MINI_APP.equals(type)){
            authRequest = Json.parseObject(requestBody, MiniAppAuthenticationToken.class);
        }


        // 账号密码登陆
        else if (SecurityConstants.SPRING_SECURITY_RESTFUL_TYPE_ADMIN.equals(type)) {
            authRequest = Json.parseObject(requestBody, AdminAuthenticationToken.class);
        }

        if (authRequest == null) {
            throw new UnknownGrantTypeExceptionBase("未知的grant_type");
        }

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        String result =  request.getParameter(parameter);
        return result == null ? "" : result;
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
}

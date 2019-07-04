/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.handler;

import cn.hutool.core.util.CharsetUtil;
import com.yami.shop.security.exception.BaseYamiAuth2Exception;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登陆失败处理
 */
@Component
@Slf4j
public class LoginAuthFailedHandler implements AuthenticationFailureHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) {

        if (!(exception instanceof BaseYamiAuth2Exception)) {
            return;
        }

        BaseYamiAuth2Exception auth2Exception = (BaseYamiAuth2Exception) exception;

        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(auth2Exception.getHttpErrorCode());
        PrintWriter printWriter = response.getWriter();
        printWriter.append(auth2Exception.getMessage());
    }

}
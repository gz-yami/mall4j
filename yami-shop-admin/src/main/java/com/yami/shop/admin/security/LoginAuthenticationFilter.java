/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.security.constants.SecurityConstants;
import com.yami.shop.security.exception.BadCredentialsExceptionBase;
import com.yami.shop.security.exception.ImageCodeNotMatchExceptionBase;
import com.yami.shop.security.exception.UsernameNotFoundExceptionBase;
import com.yami.shop.security.provider.AuthenticationTokenParser;
import com.yami.shop.security.service.YamiUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 管理员登陆：
 *       post: http://127.0.0.1:8085/login
 *       {principal:username,credentials:password}
 */
@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private YamiUserDetailsService yamiUserDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginAuthenticationFilter(YamiUserDetailsService yamiUserDetailsService, PasswordEncoder passwordEncoder) {
        super("/login");
        this.yamiUserDetailsService = yamiUserDetailsService;
        this.passwordEncoder = passwordEncoder;
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
        AdminAuthenticationToken adminAuthenticationToken  =  Json.parseObject(requestBody, AdminAuthenticationToken.class);


        String username = adminAuthenticationToken.getPrincipal() == null?"NONE_PROVIDED":adminAuthenticationToken.getName();


        String kaptchaKey = SecurityConstants.SPRING_SECURITY_RESTFUL_IMAGE_CODE + adminAuthenticationToken.getSessionUUID();

        String kaptcha = RedisUtil.get(kaptchaKey);

        RedisUtil.del(kaptchaKey);

        if(StrUtil.isBlank(adminAuthenticationToken.getImageCode()) || !adminAuthenticationToken.getImageCode().equalsIgnoreCase(kaptcha)){
            throw new ImageCodeNotMatchExceptionBase("验证码有误");
        }

        UserDetails user;
        try {
            user = yamiUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundExceptionBase var6) {
            throw new UsernameNotFoundExceptionBase("账号或密码不正确");
        }

        String encodedPassword = user.getPassword();
        String rawPassword = adminAuthenticationToken.getCredentials().toString();

        // 密码不正确
        if (!passwordEncoder.matches(rawPassword,encodedPassword)){
            throw new BadCredentialsExceptionBase("账号或密码不正确");
        }

        if (!user.isEnabled()) {
            throw new UsernameNotFoundExceptionBase("账号已被锁定,请联系管理员");
        }
        AdminAuthenticationToken result = new AdminAuthenticationToken(user, adminAuthenticationToken.getCredentials());
        result.setDetails(adminAuthenticationToken.getDetails());
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

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
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.security.constants.SecurityConstants;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.exception.BadCredentialsExceptionBase;
import com.yami.shop.security.exception.ImageCodeNotMatchExceptionBase;
import com.yami.shop.security.exception.UsernameNotFoundExceptionBase;
import com.yami.shop.security.exception.BaseYamiAuth2Exception;
import com.yami.shop.security.provider.AbstractUserDetailsAuthenticationProvider;
import com.yami.shop.security.service.YamiUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 后台管理员账号密码登陆
 * @author LGH
 */
@Component
@AllArgsConstructor
public class AdminAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final YamiUserDetailsService yamiUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) throws BaseYamiAuth2Exception {
        UserDetails user;
        try {
            user = yamiUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundExceptionBase var6) {
            throw new UsernameNotFoundExceptionBase("账号或密码不正确");
        }
        if (!user.isEnabled()) {
            throw new UsernameNotFoundExceptionBase("账号已被锁定,请联系管理员");
        }
        return user;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails sysUser, Authentication authentication) throws BaseYamiAuth2Exception {
        AdminAuthenticationToken adminAuthenticationToken = (AdminAuthenticationToken) authentication;

        String kaptchaKey = SecurityConstants.SPRING_SECURITY_RESTFUL_IMAGE_CODE + adminAuthenticationToken.getSessionUUID();

        String kaptcha = RedisUtil.get(kaptchaKey);

        RedisUtil.del(kaptchaKey);

        if(StrUtil.isBlank(adminAuthenticationToken.getImageCode()) || !adminAuthenticationToken.getImageCode().equalsIgnoreCase(kaptcha)){
            throw new ImageCodeNotMatchExceptionBase("验证码有误");
        }



        String encodedPassword = sysUser.getPassword();
        String rawPassword = authentication.getCredentials().toString();

        // 密码不正确
        if (!passwordEncoder.matches(rawPassword,encodedPassword)){
            throw new BadCredentialsExceptionBase("账号或密码不正确");
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        AdminAuthenticationToken result = new AdminAuthenticationToken(user, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return AdminAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected App getAppInfo() {
        return null;
    }

}

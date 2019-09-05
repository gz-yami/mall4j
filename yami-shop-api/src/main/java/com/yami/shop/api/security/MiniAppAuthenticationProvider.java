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
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.BooleanUtil;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.exception.UsernameNotFoundExceptionBase;
import com.yami.shop.security.exception.WxErrorExceptionBase;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.provider.AbstractUserDetailsAuthenticationProvider;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.service.YamiUserDetailsService;
import com.yami.shop.security.token.MyAuthenticationToken;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 小程序登陆
 * @author LGH
 */
@Component
@AllArgsConstructor
public class MiniAppAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final YamiUserDetailsService yamiUserDetailsService;

    private final WxMaService wxMaService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {

    }

    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        MiniAppAuthenticationToken result = new MiniAppAuthenticationToken(user, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String code, Authentication authentication) throws AuthenticationException {
        YamiUser loadedUser = null;
        // 如果使用debugger 模式，则返回debugger的用户
        if (BooleanUtil.isTrue(((MyAuthenticationToken)authentication).getDebugger())) {
            loadedUser = new YamiUser("1" , "debuggerOpenId1" ,  this.getAppInfo().value(), true);
            loadedUser.setDebugger(true);
            return loadedUser;
        }

        WxMaJscode2SessionResult session = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(this.getAppInfo().value());
        try {

            session = wxMaService.getUserService().getSessionInfo(code);

            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(),session.getOpenid());
        } catch (WxErrorException e) {
            throw new WxErrorExceptionBase(e.getMessage());
        } catch (UsernameNotFoundExceptionBase var6) {
            if (session == null) {
                throw new WxErrorExceptionBase("无法获取用户登陆信息");
            }
            appConnect.setBizUserId(session.getOpenid());
            appConnect.setBizUnionid(session.getUnionid());
            yamiUserDetailsService.insertUserIfNecessary(appConnect);
        }

        if (loadedUser == null) {
            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(), appConnect.getBizUserId());
        }
        return loadedUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MiniAppAuthenticationToken.class.isAssignableFrom(authentication);
    }


    @Override
    protected App getAppInfo() {
        return App.MINI;
    }
}

/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.provider;



import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.exception.UsernameNotFoundExceptionBase;
import com.yami.shop.security.exception.WxErrorExceptionBase;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.service.YamiUserDetailsService;
import com.yami.shop.security.token.MpAuthenticationToken;
import com.yami.shop.security.token.MyAuthenticationToken;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 微信公众号登陆
 * @author LGH
 */
//@Component
@AllArgsConstructor
public class MpAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final YamiUserDetailsService yamiUserDetailsService;

    private final WxMpService wxMpService;


    @Override
    protected void additionalAuthenticationChecks(UserDetails var1, Authentication authentication) throws AuthenticationException {

    }

    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        MpAuthenticationToken result = new MpAuthenticationToken(user, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String code, Authentication authentication) throws AuthenticationException {
        YamiUser loadedUser = null;
        // 如果使用debugger 模式，则返回debugger的用户
        if (BooleanUtil.isTrue(((MyAuthenticationToken)authentication).getDebugger())) {
            loadedUser = new YamiUser("1" , "debuggerOpenId" ,  this.getAppInfo().value(), true);
            loadedUser.setDebugger(true);
            return loadedUser;
        }

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(this.getAppInfo().value());

        try {

            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(),wxMpOAuth2AccessToken.getOpenId());

        } catch (WxErrorException e) {
            throw new WxErrorExceptionBase(e.getMessage());
        } catch (UsernameNotFoundExceptionBase var6) {
            WxMpUser wxMpUser = null;
            try {
                wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            } catch (WxErrorException e) {
                throw new WxErrorExceptionBase(e.getMessage());
            }
            appConnect.setNickName(EmojiUtil.toAlias(StrUtil.isBlank(wxMpUser.getNickname())? "": wxMpUser.getNickname()));
            appConnect.setImageUrl(wxMpUser.getHeadImgUrl());
            appConnect.setBizUserId(wxMpUser.getOpenId());
            appConnect.setBizUnionid(wxMpUser.getUnionId());
            yamiUserDetailsService.insertUserIfNecessary(appConnect);

        }

        if (loadedUser == null) {
            loadedUser = yamiUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(), appConnect.getBizUserId());
        }
        return loadedUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MpAuthenticationToken.class.isAssignableFrom(authentication);
    }


    @Override
    protected App getAppInfo() {
        return App.MP;
    }
}

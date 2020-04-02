package com.yami.shop.admin.security;

import com.yami.shop.security.service.YamiSysUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强
 * @author LGH
 */
@Component
public class AdminTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        YamiSysUser yamiSysUser = (YamiSysUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("shopId", yamiSysUser.getShopId());
        additionalInfo.put("userId", yamiSysUser.getUserId());
        additionalInfo.put("authorities", yamiSysUser.getAuthorities());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}

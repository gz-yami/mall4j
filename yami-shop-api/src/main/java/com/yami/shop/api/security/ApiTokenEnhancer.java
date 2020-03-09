package com.yami.shop.api.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.yami.shop.security.service.YamiUser;
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
public class ApiTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>(8);
        YamiUser yamiUser = (YamiUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("userId", yamiUser.getUserId());
        additionalInfo.put("nickName", EmojiUtil.toUnicode(StrUtil.isBlank(yamiUser.getName())? "" : yamiUser.getName()));
        additionalInfo.put("pic",yamiUser.getPic());
        additionalInfo.put("enabled",yamiUser.isEnabled());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}

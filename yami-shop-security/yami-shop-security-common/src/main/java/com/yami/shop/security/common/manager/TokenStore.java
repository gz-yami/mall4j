/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.manager;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yami.shop.common.constants.OauthCacheNames;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.security.common.bo.TokenInfoBO;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.vo.TokenInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token管理 1. 登陆返回token 2. 刷新token 3. 清除用户过去token 4. 校验token
 *
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
@Component
public class TokenStore {

    private static final Logger logger = LoggerFactory.getLogger(TokenStore.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenStore(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 以Sa-Token技术生成token，并返回token信息
     * @param userInfoInToken
     * @return
     */
    public TokenInfoBO storeAccessSaToken(UserInfoInTokenBO userInfoInToken) {
        // token生成
        int timeoutSecond = getExpiresIn(userInfoInToken.getSysType());
        String uid = this.getUid(userInfoInToken.getSysType().toString(), userInfoInToken.getUserId());
        StpUtil.login(uid, timeoutSecond);
        String token = StpUtil.getTokenValue();
        // 用户信息存入缓存
        String keyName = OauthCacheNames.USER_INFO + token;
        redisTemplate.delete(keyName);
        redisTemplate.opsForValue().set(
                keyName,
                JSON.toJSONString(userInfoInToken),
                timeoutSecond,
                TimeUnit.SECONDS
        );
        // 数据封装返回(token不用加密)
        TokenInfoBO tokenInfoBO = new TokenInfoBO();
        tokenInfoBO.setUserInfoInToken(userInfoInToken);
        tokenInfoBO.setExpiresIn(timeoutSecond);
        tokenInfoBO.setAccessToken(token);
        tokenInfoBO.setRefreshToken(token);
        return tokenInfoBO;
    }

    /**
     * 计算过期时间（单位:秒）
     * @param sysType
     * @return
     */
    private int getExpiresIn(int sysType) {
        // 3600秒
        int expiresIn = 3600;
        // 普通用户token过期时间 1小时
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        // 系统管理员的token过期时间 2小时
        if (Objects.equals(sysType, SysTypeEnum.ADMIN.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }

    /**
     * 根据accessToken 获取用户信息
     * @param accessToken accessToken
     * @param needDecrypt 是否需要解密
     * @return 用户信息
     */
    public UserInfoInTokenBO getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
        if (StrUtil.isBlank(accessToken)) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"accessToken is blank");
        }
        String keyName = OauthCacheNames.USER_INFO + accessToken;
        Object redisCache = redisTemplate.opsForValue().get(keyName);
        if (redisCache == null) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"登录过期，请重新登录");
        }
        return JSON.parseObject(redisCache.toString(), UserInfoInTokenBO.class);
    }

    /**
     * 刷新token，并返回新的token
     * @param refreshToken
     * @return
     */
    public TokenInfoBO refreshToken(String refreshToken) {
        if (StrUtil.isBlank(refreshToken)) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"refreshToken is blank");
        }
        // 删除旧token
        UserInfoInTokenBO userInfoInTokenBO = getUserInfoByAccessToken(refreshToken, false);
        this.deleteCurrentToken(refreshToken);
        // 保存一份新的token
        return storeAccessSaToken(userInfoInTokenBO);
    }

    /**
     * 删除指定用户的全部的token
     */
    public void deleteAllToken(String sysType, String userId) {
        // 删除用户缓存
        String uid = this.getUid(sysType, userId);
        List<String> tokens = StpUtil.getTokenValueListByLoginId(uid);
        if (!CollectionUtils.isEmpty(tokens)) {
            List<String> keyNames = new ArrayList<>();
            for (String token : tokens) {
                keyNames.add(OauthCacheNames.USER_INFO + token);
            }
            redisTemplate.delete(keyNames);
        }
        // 移除token
        StpUtil.logout(userId);
    }

    /**
     * 生成token，并返回token展示信息
     * @param userInfoInToken
     * @return
     */
    public TokenInfoVO storeAndGetVo(UserInfoInTokenBO userInfoInToken) {
        if (!userInfoInToken.getEnabled()){
            // 用户已禁用，请联系客服
            throw new YamiShopBindException("yami.user.disabled");
        }
        TokenInfoBO tokenInfoBO = storeAccessSaToken(userInfoInToken);
        // 数据封装返回
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
        return tokenInfoVO;
    }

    /**
     * 删除当前登录的token
     * @param accessToken 令牌
     */
    public void deleteCurrentToken(String accessToken) {
        // 删除用户缓存
        String keyName = OauthCacheNames.USER_INFO + accessToken;
        redisTemplate.delete(keyName);
        // 移除token
        StpUtil.logoutByTokenValue(accessToken);
    }

    /**
     * 生成各系统唯一uid
     * @param sysType 系统类型
     * @param userId 用户id
     * @return
     */
    private String getUid(String sysType, String userId) {
        return sysType + ":" + userId;
    }
}

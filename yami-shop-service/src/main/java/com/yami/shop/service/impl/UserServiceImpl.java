/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.bean.vo.UserVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author lgh on 2018/09/11.
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    @Cacheable(cacheNames = "user", key = "#userId")
    public User getUserByUserId(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Boolean insertUser(UserRegisterParam uParam) {
        User mail = userMapper.getUserByUserMail(uParam.getUserMail());
        if (mail != null) {
            throw new YamiShopBindException("账号已存在");
        }
        Date now = new Date();
        User user = new User();
        user.setUserId(IdUtil.simpleUUID());
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        user.setNickName(uParam.getUserMail());
        user.setUserMail(uParam.getUserMail());
        user.setLoginPassword(uParam.getPassword());
        return userMapper.insert(user) == 1;
    }
    /**
     * 看看有没有校验验证码成功的标识
     * @param userRegisterParam
     * @param checkRegisterSmsFlag
     */
    @Override
    public void validate(UserRegisterParam userRegisterParam, String checkRegisterSmsFlag) {
        if (StrUtil.isBlank(userRegisterParam.getCheckRegisterSmsFlag())) {
            // 验证码已过期，请重新发送验证码校验
            throw new YamiShopBindException("验证码已过期，请重新发送验证码校验");
        } else {
            String checkRegisterSmsFlagMobile = RedisUtil.get(checkRegisterSmsFlag);
            if (!Objects.equals(checkRegisterSmsFlagMobile, userRegisterParam.getMobile())) {
                // 验证码已过期，请重新发送验证码校验
                throw new YamiShopBindException("验证码已过期，请重新发送验证码校验");
            }
        }
    }
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.admin.dto.CaptchaAuthenticationDTO;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordCheckManager;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.sys.constant.Constant;
import com.yami.shop.sys.model.SysMenu;
import com.yami.shop.sys.model.SysUser;
import com.yami.shop.sys.service.SysMenuService;
import com.yami.shop.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Tag(name = "登录")
public class AdminLoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PasswordManager passwordManager;

    @PostMapping("/adminLogin")
    @Operation(summary = "账号密码 + 验证码登录(用于后台登录)" , description = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> login(
            @Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登陆后台登录需要再校验一遍验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            return ServerResponseEntity.showFailMsg("验证码有误或已过期");
        }

        SysUser sysUser = sysUserService.getByUserName(captchaAuthenticationDTO.getUserName());
        if (sysUser == null) {
            throw new YamiShopBindException("账号或密码不正确");
        }

        // 半小时内密码输入错误十次，已限制登录30分钟
        String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());
        passwordCheckManager.checkPassword(SysTypeEnum.ADMIN,captchaAuthenticationDTO.getUserName(), decryptPassword, sysUser.getPassword());

        // 不是店铺超级管理员，并且是禁用状态，无法登录
        if (Objects.equals(sysUser.getStatus(),0)) {
            // 未找到此用户信息
            throw new YamiShopBindException("未找到此用户信息");
        }

        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(String.valueOf(sysUser.getUserId()));
        userInfoInToken.setSysType(SysTypeEnum.ADMIN.value());
        userInfoInToken.setEnabled(sysUser.getStatus() == 1);
        userInfoInToken.setPerms(getUserPermissions(sysUser.getUserId()));
        userInfoInToken.setNickName(sysUser.getUsername());
        userInfoInToken.setShopId(sysUser.getShopId());
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        return ServerResponseEntity.success(tokenInfoVO);
    }


    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN_ID){
            List<SysMenu> menuList = sysMenuService.list(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        }else{
            permsList = sysUserService.queryAllPerms(userId);
        }
        return permsList.stream().flatMap((perms)->{
                    if (StrUtil.isBlank(perms)) {
                        return null;
                    }
                    return Arrays.stream(perms.trim().split(StrUtil.COMMA));
                }
        ).collect(Collectors.toSet());
    }
}

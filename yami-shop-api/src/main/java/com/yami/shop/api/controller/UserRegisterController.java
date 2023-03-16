package com.yami.shop.api.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

/**
 * 用户信息
 *
 * @author SJL
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户注册相关接口")
@AllArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenStore tokenStore;

    private final PasswordManager passwordManager;

    @PostMapping("/register")
    @Operation(summary = "注册" , description = "用户注册或绑定手机号接口")
    public ServerResponseEntity<TokenInfoVO> register(@Valid @RequestBody UserRegisterParam userRegisterParam) {
        if (StrUtil.isBlank(userRegisterParam.getNickName())) {
            userRegisterParam.setNickName(userRegisterParam.getUserName());
        }
        // 正在进行申请注册
        if (userService.count(new LambdaQueryWrapper<User>().eq(User::getNickName, userRegisterParam.getNickName())) > 0) {
            // 该用户名已注册，无法重新注册
            throw new YamiShopBindException("该用户名已注册，无法重新注册");
        }
        Date now = new Date();
        User user = new User();
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        user.setNickName(userRegisterParam.getNickName());
        user.setUserMail(userRegisterParam.getUserMail());
        String decryptPassword = passwordManager.decryptPassword(userRegisterParam.getPassWord());
        user.setLoginPassword(passwordEncoder.encode(decryptPassword));
        String userId = IdUtil.simpleUUID();
        user.setUserId(userId);
        userService.save(user);
        // 2. 登录
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUserId(user.getUserId());
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);
        userInfoInTokenBO.setEnabled(true);
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }


    @PutMapping("/updatePwd")
    @Operation(summary = "修改密码" , description = "修改密码")
    public ServerResponseEntity<Void> updatePwd(@Valid @RequestBody UserRegisterParam userPwdUpdateParam) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getNickName, userPwdUpdateParam.getNickName()));
        if (user == null) {
            // 无法获取用户信息
            throw new YamiShopBindException("无法获取用户信息");
        }
        String decryptPassword = passwordManager.decryptPassword(userPwdUpdateParam.getPassWord());
        if (StrUtil.isBlank(decryptPassword)) {
            // 新密码不能为空
            throw new YamiShopBindException("新密码不能为空");
        }
        String password = passwordEncoder.encode(decryptPassword);
        if (StrUtil.equals(password, user.getLoginPassword())) {
            // 新密码不能与原密码相同
            throw new YamiShopBindException("新密码不能与原密码相同");
        }
        user.setModifyTime(new Date());
        user.setLoginPassword(password);
        userService.updateById(user);
        return ServerResponseEntity.success();
    }
}

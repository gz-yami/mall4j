package com.yami.shop.api.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.api.security.AuthenticationToken;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.IPHelper;
import com.yami.shop.security.handler.LoginAuthSuccessHandler;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.YamiUserDetailsService;
import com.yami.shop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

/**
 * 用户信息
 *
 * @author SJL
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户注册相关接口")
@AllArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "用户注册或绑定手机号接口")
    public ResponseEntity<Boolean> register(@Valid @RequestBody UserRegisterParam userRegisterParam) {
        userRegisterParam.setPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        return ResponseEntity.ok(userService.insertUser(userRegisterParam));
    }


    @PutMapping("/updatePwd")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public ResponseEntity<Void> updatePwd(@Valid @RequestBody UserRegisterParam userRegisterParam) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, userRegisterParam.getUserMail()));
        if (user == null) {
            // 无法获取用户信息
            throw new YamiShopBindException("yami.user.no.exist");
        }
        if (StrUtil.isBlank(userRegisterParam.getPassword())) {
            // 新密码不能为空
            throw new YamiShopBindException("yami.user.password.no.exist");
        }
        if (StrUtil.equals(passwordEncoder.encode(userRegisterParam.getPassword()), user.getLoginPassword())) {
            // 新密码不能与原密码相同
            throw new YamiShopBindException("yami.user.password.check");
        }
        user.setModifyTime(new Date());
        user.setLoginPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        userService.updateById(user);
        return ResponseEntity.ok().build();
    }
}

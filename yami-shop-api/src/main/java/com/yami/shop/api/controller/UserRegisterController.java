package com.yami.shop.api.controller;


import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.api.security.AuthenticationToken;
import com.yami.shop.bean.enums.SmsType;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.IPHelper;
import com.yami.shop.common.util.PrincipalUtil;
import com.yami.shop.mp.config.WxMaConfiguration;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.handler.LoginAuthSuccessHandler;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.AppConnectService;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.service.YamiUserDetailsService;
import com.yami.shop.security.util.SecurityUtils;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

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

    private final SmsLogService smsLogService;

    private final AppConnectService appConnectService;

    private final LoginAuthSuccessHandler loginAuthSuccessHandler;

    private final WxMaConfiguration wxConfig;

    private final YamiUserDetailsService yamiUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public static final String CHECK_REGISTER_SMS_FLAG = "checkRegisterSmsFlag";

    public static final String CHECK_UPDATE_PWD_SMS_FLAG = "updatePwdSmsFlag";

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
            throw new YamiShopBindException("无法获取用户信息");
        }
        if (StrUtil.isBlank(userRegisterParam.getPassword())) {
            // 新密码不能为空
            throw new YamiShopBindException("新密码不能为空");
        }
        if (StrUtil.equals(passwordEncoder.encode(userRegisterParam.getPassword()), user.getLoginPassword())) {
            // 新密码不能与原密码相同
            throw new YamiShopBindException("新密码不能与原密码相同");
        }
        user.setModifyTime(new Date());
        user.setLoginPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
        userService.updateById(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/registerOrBindUser")
    @ApiOperation(value="注册或绑定手机号", notes="用户注册或绑定手机号接口")
    public ResponseEntity<Void> register(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody UserRegisterParam userRegisterParam) {

        String mobile = userRegisterParam.getMobile();
        AppConnect appConnect = null;
        User user = null;
        String bizUserId = null;
        boolean isWxAppId = Objects.equals(userRegisterParam.getAppType(), App.MINI.value()) || Objects.equals(userRegisterParam.getAppType(), App.MP.value());
        if(isWxAppId) {
            bizUserId = SecurityUtils.getUser().getBizUserId();
        }

        // 正在进行注册，通过验证码校验
        if (Objects.equals(userRegisterParam.getRegisterOrBind(), 1)) {

            // 看看有没有校验验证码成功的标识
            userService.validate(userRegisterParam, CHECK_REGISTER_SMS_FLAG + userRegisterParam.getCheckRegisterSmsFlag());
            // 正在进行申请注册
            if (userService.count(new LambdaQueryWrapper<User>().eq(User::getUserMobile,userRegisterParam.getMobile())) > 0) {
                // 手机号已存在，无法注册
                throw new YamiShopBindException("yami.user.phone.exist");
            }
        }
        // 小程序注册/绑定手机号
        else {
            YamiUser yamiUser =  SecurityUtils.getUser();

            appConnect = appConnectService.getByBizUserId(yamiUser.getBizUserId(), App.instance(yamiUser.getAppType()));
            // 通过微信手机号校验
            if (Objects.equals(2, userRegisterParam.getValidateType())) {
                try {
                    WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxConfig.wxMaService().getUserService().getPhoneNoInfo(yamiUser.getSessionKey(), userRegisterParam.getEncryptedData(), userRegisterParam.getIvStr());
                    mobile = wxMaPhoneNumberInfo.getPhoneNumber();

                } catch (Exception e) {
                    // 授权失败，请重新授权
                    throw new YamiShopBindException(" 授权失败，请重新授权");
                }
                if (StrUtil.isBlank(mobile)) {
                    // 无法获取用户手机号信息
                    throw new YamiShopBindException("无法获取用户手机号信息");
                }
                user = yamiUserDetailsService.loadUserByMobileOrUserName(mobile, 0);
            }
            // 通过账号密码校验
            else if (Objects.equals(3, userRegisterParam.getValidateType())) {
                user = yamiUserDetailsService.loadUserByMobileOrUserName(mobile, 0);
                if (user == null) {
                    // 账号或密码不正确
                    throw new YamiShopBindException("yami.user.account.error");
                }
                String encodedPassword = user.getLoginPassword();
                String rawPassword = userRegisterParam.getPassword();
                // 密码不正确
                if (StrUtil.isBlank(encodedPassword) || !passwordEncoder.matches(rawPassword,encodedPassword)){
                    // 账号或密码不正确
                    throw new YamiShopBindException("yami.user.account.error");
                }
            }
            // 通过验证码校验
            else {
                if (!smsLogService.checkValidCode(userRegisterParam.getMobile(), userRegisterParam.getValidCode(), SmsType.VALID)){
                    // 验证码有误或已过期
                    throw new YamiShopBindException("yami.user.code.error");
                }
            }
        }

        Date now = new Date();

        // 尝试用手机号获取用户信息
        if (user == null && StrUtil.isNotBlank(mobile)) {
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile,mobile));
        }

        // 新建用户
        if (user == null) {
            user = new User();
            if (StrUtil.isBlank(userRegisterParam.getUserName())) {
                userRegisterParam.setUserName(mobile);
            }

            // 如果有用户名,就判断用户名格式是否正确
            if (!PrincipalUtil.isUserName(userRegisterParam.getUserName())) {
                throw new YamiShopBindException("用户名应由4-16位数字字母下划线组成");
            }

            user.setModifyTime(now);
            user.setUserRegtime(now);
            user.setUserRegip(IPHelper.getIpAddr());
            user.setStatus(1);

            user.setPic(userRegisterParam.getImg());
            user.setUserMobile(mobile);
            if (StrUtil.isNotBlank(userRegisterParam.getPassword())) {
                user.setLoginPassword(passwordEncoder.encode(userRegisterParam.getPassword()));
            }
            // 用户名就是默认的昵称
            user.setNickName(StrUtil.isBlank(userRegisterParam.getNickName())? userRegisterParam.getUserName(): userRegisterParam.getNickName());
//		} else  {
//			String userId = user.getUserId();
//			// 绑定账号
//			if (Objects.equals(userRegisterParam.getRegisterOrBind(),2)) {
//				int count = appConnectService.count(new LambdaQueryWrapper<AppConnect>().eq(AppConnect::getUserId, userId).eq(AppConnect::getAppId, userRegisterParam.getAppType()));
//				if (count > 0) {
//					throw new YamiShopBindException("该账号已被绑定，请换个账号试试");
//				}
//			}

        }
        if(Objects.nonNull(bizUserId)){
            appConnect = new AppConnect();
            appConnect.setBizUserId(bizUserId);
        }

        appConnectService.registerOrBindUser(user, appConnect, userRegisterParam.getAppType());


        //进行授权登录
        UserDetails userDetails = yamiUserDetailsService.getYamiUser(userRegisterParam.getAppType(),user, bizUserId);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setPrincipal(user.getUserMobile());
        authenticationToken.setCredentials(user.getLoginPassword());
        authenticationToken.setPrincipal(userDetails.getUsername());
        authenticationToken.setDetails(userDetails);
        authenticationToken.setAuthenticated(true);
        loginAuthSuccessHandler.onAuthenticationSuccess(request,response,authenticationToken);

        return ResponseEntity.ok().build();
    }

}

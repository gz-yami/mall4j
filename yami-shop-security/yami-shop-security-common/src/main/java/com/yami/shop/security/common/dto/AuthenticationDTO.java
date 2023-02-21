/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用于登陆传递账号密码
 *
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@Data
public class AuthenticationDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "userName不能为空")
    @Schema(description = "用户名/邮箱/手机号" , required = true)
    protected String userName;

    /**
     * 密码
     */
    @NotBlank(message = "passWord不能为空")
    @Schema(description = "一般用作密码" , required = true)
    protected String passWord;

}

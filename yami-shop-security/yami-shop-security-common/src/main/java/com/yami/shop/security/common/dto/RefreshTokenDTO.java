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

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新token
 *
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
public class RefreshTokenDTO {

    /**
     * refreshToken
     */
    @NotBlank(message = "refreshToken不能为空")
    @Schema(description = "refreshToken" , required = true)
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenDTO{" + "refreshToken='" + refreshToken + '\'' + '}';
    }

}

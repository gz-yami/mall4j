/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.controller;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@RestController
@RequestMapping("/captcha")
@Api(tags = "验证码")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping({ "/get" })
    public ResponseEntity<ResponseModel> get(@RequestBody CaptchaVO captchaVO) {
        return ResponseEntity.ok(captchaService.get(captchaVO));
    }

    @PostMapping({ "/check" })
    public ResponseEntity<ResponseModel> check(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel;
        try {
            responseModel = captchaService.check(captchaVO);
        }catch (Exception e) {
            return ResponseEntity.ok(ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR));
        }
        return ResponseEntity.ok(responseModel);
    }

}

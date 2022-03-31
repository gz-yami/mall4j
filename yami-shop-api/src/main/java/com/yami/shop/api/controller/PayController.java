/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.yami.shop.bean.app.param.PayParam;
import com.yami.shop.bean.pay.PayInfoDto;
import com.yami.shop.security.api.model.YamiUser;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p/order")
@Api(tags = "订单接口")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<WxPayMpOrderResult> pay(@RequestBody PayParam payParam) {
        YamiUser user = SecurityUtils.getUser();
        String userId = user.getUserId();


        PayInfoDto payInfo = payService.pay(userId, payParam);
        payService.paySuccess(payInfo.getPayNo(), "");
        return ResponseEntity.ok().build();
    }

    /**
     * 普通支付接口
     */
    @PostMapping("/normalPay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<Boolean> normalPay(@RequestBody PayParam payParam) {

        YamiUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        PayInfoDto pay = payService.pay(userId, payParam);

        // 根据内部订单号更新order settlement
        payService.paySuccess(pay.getPayNo(), "");

        return ResponseEntity.ok(true);
    }
}

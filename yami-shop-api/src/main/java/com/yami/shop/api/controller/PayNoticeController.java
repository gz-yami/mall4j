/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yami.shop.service.PayService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/notice/pay")
@AllArgsConstructor
public class PayNoticeController {

    /**
     * 小程序支付
     */
    private final WxPayService wxMiniPayService;

    private final PayService payService;


    @RequestMapping("/order")
    public ResponseEntity<Void> submit(@RequestBody String xmlData) throws WxPayException {
        WxPayOrderNotifyResult parseOrderNotifyResult = wxMiniPayService.parseOrderNotifyResult(xmlData);

        String payNo = parseOrderNotifyResult.getOutTradeNo();
        String bizPayNo = parseOrderNotifyResult.getTransactionId();

        // 根据内部订单号更新order settlement
        payService.paySuccess(payNo, bizPayNo);


        return ResponseEntity.ok().build();
    }
}
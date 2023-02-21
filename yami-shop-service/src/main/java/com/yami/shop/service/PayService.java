/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.yami.shop.bean.app.param.PayParam;
import com.yami.shop.bean.pay.PayInfoDto;

import java.util.List;

/**
 * @author lgh on 2018/09/15.
 */
public interface PayService {

    /**
     * 支付
     * @param userId
     * @param payParam
     * @return
     */
    PayInfoDto pay(String userId, PayParam payParam);

    /**
     * 支付成功
     * @param payNo
     * @param bizPayNo
     * @return
     */
    List<String> paySuccess(String payNo, String bizPayNo);

}

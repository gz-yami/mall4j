/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.model.AppConnect;

/**
 *
 * @author lgh on 2018/09/07.
 */
public interface AppConnectService extends IService<AppConnect> {

	AppConnect getByBizUserId(String bizUserId, App app);
}

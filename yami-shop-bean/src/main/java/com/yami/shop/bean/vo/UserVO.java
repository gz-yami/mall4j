/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.vo;

import lombok.Data;
/**
 * @author lanhai
 */
@Data
public class UserVO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    private String userMobile;

    /**
     * 用户头像
     */
    private String pic;
}

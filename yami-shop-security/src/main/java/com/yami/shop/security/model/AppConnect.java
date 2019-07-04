/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.model;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tz_app_connect")
public class AppConnect {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 本系统userId
     */

    private String userId;

    /**
     * 第三方系统id 1：微信小程序
     */

    private Integer appId;

    /**
     * 第三方系统昵称
     */

    private String nickName;

    /**
     * 第三方系统头像
     */

    private String imageUrl;

    /**
     * 第三方系统userid
     */

    private String bizUserId;

    /**
     * 第三方系统unionid
     */

    private String bizUnionid;

}
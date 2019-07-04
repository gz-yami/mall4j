/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tz_login_hist")
public class LoginHist implements Serializable {
    /**
     * ID
     */
    @TableId

    private Long id;

    /**
     * 地区
     */
    private String area;

    /**
     * 国家
     */
    private String country;

    /**
     * 用户id
     */

    private String userId;

    /**
     * IP
     */
    private String ip;

    /**
     * 时间
     */

    private Date loginTime;

}
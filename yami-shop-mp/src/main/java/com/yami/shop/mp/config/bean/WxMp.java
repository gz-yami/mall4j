/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.mp.config.bean;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:mp.properties")
@ConfigurationProperties(prefix = "mp")
public class WxMp {
    /**
     * 设置微信公众号的appid
     */
    private String appid;

    /**
     * 设置微信公众号的Secret
     */
    private String secret;
    
    /**
     * 微信公众号消息加解密token
     */
    private String token;
    
    /**
     * 微信公众号消息加解密aesKey
     */
    private String aesKey;
}

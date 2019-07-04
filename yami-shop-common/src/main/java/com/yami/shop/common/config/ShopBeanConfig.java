/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.config;

import cn.hutool.crypto.symmetric.AES;
import com.yami.shop.common.bean.ALiDaYu;
import com.yami.shop.common.bean.Qiniu;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ShopBeanConfig {

	private final ShopBasicConfig shopBasicConfig;

    @Bean
    public Qiniu qiniu() {
    	return shopBasicConfig.getQiniu();
    }

    @Bean
    public AES tokenAes() {
    	return new AES(shopBasicConfig.getTokenAesKey().getBytes());
    }

    @Bean
    public ALiDaYu aLiDaYu () {
    	return shopBasicConfig.getALiDaYu();
    }
}

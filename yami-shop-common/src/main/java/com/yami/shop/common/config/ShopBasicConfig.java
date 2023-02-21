/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.config;

import com.yami.shop.common.bean.AliDaYu;
import com.yami.shop.common.bean.Qiniu;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * 商城配置文件
 * @author lgh
 */
@Data
@Component
@PropertySource("classpath:shop.properties")
@ConfigurationProperties(prefix = "shop")
public class ShopBasicConfig {

	/**
	 * 七牛云的配置信息
	 */
	private Qiniu qiniu;

	/**
	 * 阿里大于短信平台
	 */
	private AliDaYu aLiDaYu;

	/**
	 * 用于加解密token的密钥
	 */
	private String tokenAesKey;

}

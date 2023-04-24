/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.manager;

import cn.hutool.crypto.symmetric.AES;
import com.yami.shop.common.exception.YamiShopBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author 菠萝凤梨
 * @date 2022/1/19 16:02
 */
@Component
public class PasswordManager {
    private static final Logger logger = LoggerFactory.getLogger(PasswordManager.class);

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.password.signKey:-mall4j-password}")
    public String passwordSignKey;

    public String decryptPassword(String data) {
        // 在使用oracle的JDK时，JAR包必须签署特殊的证书才能使用。
        // 解决方案 1.使用openJDK或者非oracle的JDK（建议） 2.添加证书
        // hutool的aes报错可以打开下面那段代码
        // SecureUtil.disableBouncyCastle();
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptPassword;
        try {
            decryptStr = aes.decryptStr(data);
            decryptPassword = decryptStr.substring(13);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new YamiShopBindException("AES解密错误", e);
        }
        return decryptPassword;
    }
}

package com.yami.shop.common.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 正则表达式工具
 * @author LGH
 */
public class PrincipalUtil {

    /**
     * 以1开头，后面跟10位数
     */
    public static final String MOBILE_REGEXP = "1[0-9]{10}";

    /**
     * 数字字母下划线 4-16位
     */
    public static final String USER_NAME_REGEXP = "([a-zA-Z0-9_]{4,16})";

    public static boolean isMobile(String value) {
        if(StrUtil.isBlank(value)) {
            return false;
        }
        return Pattern.matches(MOBILE_REGEXP, value);
    }

    public static boolean isUserName(String value) {
        if(StrUtil.isBlank(value)) {
            return false;
        }
        return Pattern.matches(USER_NAME_REGEXP, value);
    }
}

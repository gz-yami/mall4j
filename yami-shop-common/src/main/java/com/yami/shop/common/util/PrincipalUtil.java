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

    /**
     * 由简单的字母数字拼接而成的字符串 不含有下划线，大写字母
     */
    public static final String SIMPLE_CHAR_REGEXP = "([a-z0-9]+)";

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

    public static boolean isMatching(String regexp, String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        return Pattern.matches(regexp, value);
    }

    /**
     * 是否是由简单的字母数字拼接而成的字符串
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isSimpleChar(String value) {
        return isMatching(SIMPLE_CHAR_REGEXP, value);
    }
}

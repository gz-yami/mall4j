/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 进制转换工具，最大支持十进制和DICT.length()进制的转换
 * 1、将十进制的数字转换为指定进制的字符串；
 * 2、将其它进制的数字（字符串形式）转换为十进制的数字
 * @author xuliugen
 * @date 2018/04/23
 */
@Component
public class IdUtil {

    @Autowired
    private Snowflake snowflake;

    private static final String DICT = "0123456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    private static final int SEED = DICT.length();

    private static final int ID_MIN_LENGTH = 6;
    /**
     * 数字->字符映射
     */
    private static final char[] CHARS = DICT.toCharArray();
    /**
     * 字符->数字映射
     */
    private static final Map<Character, Integer> NUMBERS = new HashMap<>();

    static {
        int len = CHARS.length;
        for (int i = 0; i<len; i++) {
            NUMBERS.put(CHARS[i], i);
        }
    }

    /**
     * 根据从数据库中返回的记录ID生成对应的短网址编码
     * @param id (1-56.8billion)
     * @return
     */
    public static String encode(long id) {
        StringBuilder shortUrl = new StringBuilder();
        while (id > 0) {
            int r = (int) (id % SEED);
            shortUrl.insert(0, CHARS[r]);
            id = id / SEED;
        }
        int len = shortUrl.length();
        while (len < ID_MIN_LENGTH) {
            shortUrl.insert(0, CHARS[0]);
            len++;
        }
        return shortUrl.toString();
    }

    /**
     * 根据获得的短网址编码解析出数据库中对应的记录ID
     * @param key 短网址 eg. RwTji8, GijT7Y等等
     * @return
     */
    public static long decode(String key) {
        char[] shorts = key.toCharArray();
        int len = shorts.length;
        long id = 0L;
        for (int i = 0; i < len; i++) {
            id = id + (long) (NUMBERS.get(shorts[i]) * Math.pow(SEED, len-i-1));
        }
        return id;
    }


    public String nextShortId() {
        return encode(snowflake.nextId());
    }
}

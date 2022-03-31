/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.enums;

/**
 * @author lh
 */
public enum SendType {
    /**
     * 用户注册验证码
     */
    REGISTER(12, 1,"用户注册验证码"),
    /**
     * 发送登录验证码
     */
    LOGIN(13, 1,"发送登录验证码"),
    /**
     * 修改密码验证码
     */
    UPDATE_PASSWORD(14, 1,"修改密码验证码"),
    /**
     * 身份验证验证码
     */
    VALID(15, 1,"身份验证验证码")
    ;

    private Integer value;
    /**
     * 1为全部平台发送的消息，2为根据情况
     */
    private Integer type;
    private String desc;
    SendType(Integer value, Integer type, String desc) {
        this.value = value;
        this.type = type;
        this.desc = desc;
    }
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SendType instance(Integer value) {
        SendType[] enums = values();
        for (SendType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }
}

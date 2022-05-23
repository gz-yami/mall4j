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
 * 地区层级
 * @author cl
 */
public enum AreaLevelEnum {


    /**
     * 第一层
     */
    FIRST_LEVEL(1),

    /**
     * 第二层
     */
    SECOND_LEVEL(2),

    /**
     * 第三层
     */
    THIRD_LEVEL(3)

    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    AreaLevelEnum(Integer num) {
        this.num = num;
    }

    public static AreaLevelEnum instance(Integer value) {
        AreaLevelEnum[] enums = values();
        for (AreaLevelEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}

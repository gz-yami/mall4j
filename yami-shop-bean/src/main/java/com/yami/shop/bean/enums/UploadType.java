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
 * @author lanhai
 * 文件上传方式 1.本地文件上传 2.七牛云
 */
public enum UploadType {

    /**
     * 本地文件上传
     */
    LOCAL(1),

    /**
     * 七牛云
     */
    QINIU(2);

    private Integer num;

    public Integer value() {
        return num;
    }

    UploadType(Integer num) {
        this.num = num;
    }

    public static UploadType instance(Integer value) {
        UploadType[] enums = values();
        for (UploadType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}

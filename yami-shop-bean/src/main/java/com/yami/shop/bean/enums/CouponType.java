/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.enums;


/**
 * 优惠券类型 1:代金券 2:折扣券 3:兑换券
 * @author LGH
 */
public enum CouponType {

    /**
     * 代金券
     */
    C2M(1),

    /**
     * 折扣券
     */
    C2D(2),

    /**
     * 兑换券
     */
    C2P(3)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    CouponType(Integer num){
        this.num = num;
    }

    public static CouponType instance(Integer value) {
        CouponType[] enums = values();
        for (CouponType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}

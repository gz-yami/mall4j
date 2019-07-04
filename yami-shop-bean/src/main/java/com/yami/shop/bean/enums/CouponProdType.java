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
 * 优惠券适用商品类型
 * @author LGH
 */
public enum  CouponProdType {

    /**
     * 0全部商品参与
     */
    ALL(0),

    /**
     * 1指定商品参与
     */
    PROD_IN(1),

    /**
     * 2指定商品不参与
     */
    PROD_NO_IN(2)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    CouponProdType(Integer num){
        this.num = num;
    }

    public static CouponProdType instance(Integer value) {
        CouponProdType[] enums = values();
        for (CouponProdType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author lanhai
 */
@Data
public class ServerResponse<T> implements Serializable {


    private int code;

    private String msg;

    private T obj;

    public boolean isSuccess(){
        return Objects.equals(ResponseCode.SUCCESS, this.code);
    }


}

/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.task;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestTask {

    public void test1(){
        System.out.println(DateUtil.now() + ": test1 running.................");

    }

    public void test2(String param){
        System.out.println(DateUtil.now() + ": test2 running.................");
    }
}

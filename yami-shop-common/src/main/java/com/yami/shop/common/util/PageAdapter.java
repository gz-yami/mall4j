/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PageAdapter{

    private int begin;

    private int end;

    public PageAdapter(Page page) {
        int[] startEnd = PageUtil.transToStartEnd((int) page.getCurrent(), (int) page.getSize());
        this.begin = startEnd[0];
        this.end = startEnd[1];
    }
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.dto.HotSearchDto;
import com.yami.shop.bean.model.HotSearch;

import java.util.List;

/**
 *
 * @author lgh on 2019/03/27.
 */
public interface HotSearchService extends IService<HotSearch> {

    /**
     * 根据店铺id获取热搜列表
     * @param shopId
     * @return
     */
    List<HotSearchDto> getHotSearchDtoByShopId(Long shopId);

    /**
     * 根据店铺id删除热搜缓存
     * @param shopId
     */
    void removeHotSearchDtoCacheByShopId(Long shopId);
}

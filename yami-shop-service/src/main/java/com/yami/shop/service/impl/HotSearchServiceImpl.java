/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.dto.HotSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.HotSearch;
import com.yami.shop.dao.HotSearchMapper;

import com.yami.shop.service.HotSearchService;

import java.util.List;

/**
 *
 * @author lgh on 2019/03/27.
 */
@Service
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements HotSearchService {

    @Autowired
    private HotSearchMapper hotSearchMapper;

    @Override
    @Cacheable(cacheNames = "HotSearchDto", key = "#shopId")
    public List<HotSearchDto> getHotSearchDtoByShopId(Long shopId) {
        return hotSearchMapper.getHotSearchDtoByShopId(shopId);
    }

    @Override
    @CacheEvict(cacheNames = "HotSearchDto", key = "#shopId")
    public void removeHotSearchDtoCacheByShopId(Long shopId) {

    }
}

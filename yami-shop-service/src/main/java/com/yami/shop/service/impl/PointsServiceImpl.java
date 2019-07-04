/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.Points;
import com.yami.shop.dao.PointsMapper;

import com.yami.shop.service.PointsService;

import java.util.List;

/**
 *
 * @author lgh on 2019/04/10.
 */
@Service
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {

    @Autowired
    private PointsMapper pointsMapper;

    @Override
    public IPage<Points> getPointsAndSysUserPage(Page page,Points points) {
        return pointsMapper.getPointsAndSysUserPage(page,points);
    }

    @Override
    public List<Points> getNameAndId(Points points) {
        return pointsMapper.getNameAndId(points);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        pointsMapper.deleteByIds(ids);
    }
}

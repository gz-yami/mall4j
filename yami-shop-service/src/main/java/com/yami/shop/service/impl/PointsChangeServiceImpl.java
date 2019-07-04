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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.PointsChange;
import com.yami.shop.dao.PointsChangeMapper;

import com.yami.shop.service.PointsChangeService;

/**
 *
 * @author lgh on 2019/04/10.
 */
@Service
public class PointsChangeServiceImpl extends ServiceImpl<PointsChangeMapper, PointsChange> implements PointsChangeService {

    @Autowired
    private PointsChangeMapper pointsChangeMapper;

}

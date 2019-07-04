/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.model.Points;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PointsMapper extends BaseMapper<Points> {
    IPage<Points> getPointsAndSysUserPage(Page page, @Param("points") Points points);

    List<Points> getNameAndId(@Param("points") Points points);

    void deleteByIds(@Param("ids") List<Long> ids);
}
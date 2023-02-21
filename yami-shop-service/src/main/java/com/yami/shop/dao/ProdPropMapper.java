/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.model.ProdProp;
import com.yami.shop.common.util.PageAdapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface ProdPropMapper extends BaseMapper<ProdProp> {

    /**
     * 根据参数获取商品属性列表
     * @param adapter
     * @param prodProp
     * @return
     */
    List<ProdProp> listPropAndValue(@Param("adapter") PageAdapter adapter, @Param("prodProp") ProdProp prodProp);

    /**
     * 根据参数计算商品属性
     * @param prodProp
     * @return
     */
    long countPropAndValue(@Param("prodProp") ProdProp prodProp);

    /**
     * 删除商品属性
     * @param propId
     * @param rule
     * @param shopId
     * @return
     */
    int deleteByPropId(@Param("propId") Long propId, @Param("rule") Integer rule, @Param("shopId") Long shopId);

    /**
     * 根据店铺id和属性名称获取商品属性
     * @param propName
     * @param shopId
     * @param rule
     * @return
     */
    ProdProp getProdPropByPropNameAndShopId(@Param("propName") String propName, @Param("shopId") Long shopId, @Param("rule") Integer rule);
}

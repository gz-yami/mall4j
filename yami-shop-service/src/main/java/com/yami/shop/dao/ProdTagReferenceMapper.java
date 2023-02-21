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
import com.yami.shop.bean.model.ProdTagReference;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分组标签引用
 *
 * @author hzm
 * @date 2019-04-18 16:28:01
 */
public interface ProdTagReferenceMapper extends BaseMapper<ProdTagReference> {
    /**
     * 插入标签
     * @param shopId
     * @param prodId
     * @param tagList
     */
    void insertBatch(@Param("shopId") Long shopId, @Param("prodId") Long prodId, @Param("tagList") List<Long> tagList);

    /**
     * 根据属性获取标签id列表
     * @param prodId
     * @return
     */
    List<Long> listTagIdByProdId(@Param("prodId") Long prodId);

}

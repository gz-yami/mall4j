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
import com.yami.shop.bean.model.ProdTagReference;

import java.util.List;

/**
 * 分组标签引用
 *
 * @author hzm
 * @date 2019-04-18 16:28:01
 */
public interface ProdTagReferenceService extends IService<ProdTagReference> {
    /**
     * 根据id获取标签id列表
     * @param prodId
     * @return
     */
    List<Long> listTagIdByProdId(Long prodId);
}

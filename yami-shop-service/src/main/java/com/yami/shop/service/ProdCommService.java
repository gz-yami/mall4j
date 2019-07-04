/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.ProdCommDataDto;
import com.yami.shop.bean.app.dto.ProdCommDto;
import com.yami.shop.bean.model.ProdComm;

import java.util.List;


/**
 * 商品评论
 *
 * @author xwc
 * @date 2019-04-19 10:43:57
 */
public interface ProdCommService extends IService<ProdComm> {
    ProdCommDataDto getProdCommDataByProdId(Long prodId, String userId);

    IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page,String userId);

    IPage<ProdCommDto> getProdCommDtoPageByProdId(Page page, Long prodId, Integer evaluate, String userId);

    IPage<ProdComm> getProdCommPage(Page page,ProdComm prodComm);

}

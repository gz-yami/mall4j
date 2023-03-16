/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.yami.shop.bean.app.dto.IndexImgDto;
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.service.IndexImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lanhai
 */
@RestController
@Tag(name = "首页轮播图接口")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    /**
     * 首页轮播图接口
     */
    @GetMapping("/indexImgs")
    @Operation(summary = "首页轮播图" , description = "获取首页轮播图列表信息")
    public ServerResponseEntity<List<IndexImgDto>> indexImgs() {
        List<IndexImg> indexImgList = indexImgService.listIndexImg();
        List<IndexImgDto> indexImgDtos = BeanUtil.copyToList(indexImgList, IndexImgDto.class);
        return ServerResponseEntity.success(indexImgDtos);
    }
}

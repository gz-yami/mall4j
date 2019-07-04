/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.ProdTagDto;
import com.yami.shop.bean.model.ProdTag;
import com.yami.shop.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prod/tag")
@Api(tags = "商品分组标签接口")
@AllArgsConstructor
public class ProdTagController {

    private ProdTagService prodTagService;

    private MapperFacade mapperFacade;

    /**
     * 商品分组标签列表接口
     */
    @GetMapping("/prodTagList")
    @ApiOperation(value = "商品分组标签列表", notes = "获取所有的商品分组列表")
    public ResponseEntity<List<ProdTagDto>> getProdTagList() {
        List<ProdTag> prodTagList = prodTagService.listProdTag();
        List<ProdTagDto> prodTagDtoList = mapperFacade.mapAsList(prodTagList, ProdTagDto.class);
        return ResponseEntity.ok(prodTagDtoList);
    }

}

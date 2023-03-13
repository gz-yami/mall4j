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


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.bean.dto.HotSearchDto;
import com.yami.shop.bean.dto.SearchProdDto;
import com.yami.shop.service.HotSearchService;
import com.yami.shop.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/search")
@Tag(name = "搜索接口")
@AllArgsConstructor
public class SearchController {

    private final HotSearchService hotSearchService;

    private final ProductService productService;

    @GetMapping("/hotSearchByShopId")
    @Operation(summary = "查看店铺热搜" , description = "根据店铺id,热搜数量获取热搜")
    @Parameters({
            @Parameter(name = "shopId", description = "店铺id" , required = true),
            @Parameter(name = "number", description = "取数" , required = true),
            @Parameter(name = "sort", description = "是否按照顺序(0 否 1是)"),
    })
    public ServerResponseEntity<List<HotSearchDto>> hotSearchByShopId(Long shopId,Integer number,Integer sort) {
        List<HotSearchDto> list = hotSearchService.getHotSearchDtoByShopId(shopId);

        return getListResponseEntity(number, sort, list);
    }

    @GetMapping("/hotSearch")
    @Operation(summary = "查看全局热搜" , description = "根据店铺id,热搜数量获取热搜")
    @Parameters({
            @Parameter(name = "number", description = "取数" , required = true),
            @Parameter(name = "sort", description = "是否按照顺序(0 否 1是)", required = false ),
    })
    public ServerResponseEntity<List<HotSearchDto>> hotSearch(Integer number,Integer sort) {
        List<HotSearchDto> list = hotSearchService.getHotSearchDtoByShopId(0L);
        return getListResponseEntity(number, sort, list);
    }

    private ServerResponseEntity<List<HotSearchDto>> getListResponseEntity(Integer number, Integer sort, List<HotSearchDto> list) {
        if(sort == null || sort == 0){
            Collections.shuffle(list);
        }
        if(!CollectionUtil.isNotEmpty(list) || list.size()< number){
            return ServerResponseEntity.success(list);
        }
        return ServerResponseEntity.success(list.subList(0, number));
    }

    @GetMapping("/searchProdPage")
    @Operation(summary = "分页排序搜索商品" , description = "根据商品名搜索")
    @Parameters({
            @Parameter(name = "prodName", description = "商品名" , required = true),
            @Parameter(name = "sort", description = "排序(0 默认排序 1销量排序 2价格排序)"),
            @Parameter(name = "orderBy", description = "排序(0升序 1降序)"),
            @Parameter(name = "shopId", description = "店铺id" , required = true),
    })
    public ServerResponseEntity<IPage<SearchProdDto>> searchProdPage(PageParam page, String prodName, Integer sort, Integer orderBy, Long shopId) {

        return ServerResponseEntity.success(productService.getSearchProdDtoPageByProdName(page,prodName,sort,orderBy));
    }




}

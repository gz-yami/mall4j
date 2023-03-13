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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.SkuDto;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.service.SkuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/sku")
@Tag(name = "sku规格接口")
@AllArgsConstructor
public class SkuController {

    private final SkuService skuService;

    private final MapperFacade mapperFacade;

    @GetMapping("/getSkuList")
    @Operation(summary = "通过prodId获取商品全部规格列表" , description = "通过prodId获取商品全部规格列表")
    @Parameter(name = "prodId", description = "商品id" )
    public ServerResponseEntity<List<SkuDto>> getSkuListByProdId(Long prodId) {
        List<Sku> skus = skuService.list(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getStatus, 1)
                .eq(Sku::getIsDelete, 0)
                .eq(Sku::getProdId, prodId)
        );
        List<SkuDto> skuDtoList = mapperFacade.mapAsList(skus, SkuDto.class);
        return ServerResponseEntity.success(skuDtoList);
    }
}

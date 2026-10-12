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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.ProdCommDataDto;
import com.yami.shop.bean.app.dto.ProdCommDto;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ProdCommService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 公开评论查询（无需登录）
 * @author lanhai
 */
@RestController
@RequestMapping("/prodComm")
@Tag(name = "评论接口")
@AllArgsConstructor
public class ProdCommController {

    private final ProdCommService prodCommService;

    @GetMapping("/prodCommData")
    @Operation(summary = "返回商品评论数据(好评率 好评数量 中评数 差评数)" , description = "根据商品id获取")
    public ServerResponseEntity<ProdCommDataDto> getProdCommData(Long prodId) {
        return ServerResponseEntity.success(prodCommService.getProdCommDataByProdId(prodId));
    }

    @GetMapping("/prodCommPageByProd")
    @Operation(summary = "根据商品返回评论分页数据" , description = "传入商品id和页码")
    @Parameters({
            @Parameter(name = "prodId", description = "商品id" , required = true),
            @Parameter(name = "evaluate", description = "-1或null 全部，0好评 1中评 2差评 3有图" , required = true),
    })
    public ServerResponseEntity<IPage<ProdCommDto>> getProdCommPageByProdId(PageParam page, Long prodId, Integer evaluate) {
        return ServerResponseEntity.success(prodCommService.getProdCommDtoPageByProdId(page, prodId, evaluate));
    }
}

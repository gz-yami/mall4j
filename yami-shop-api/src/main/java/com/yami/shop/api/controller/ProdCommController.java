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
import com.yami.shop.bean.app.param.ProdCommParam;
import com.yami.shop.bean.model.ProdComm;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.ProdCommService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
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

    @GetMapping("/prodCommPageByUser")
    @Operation(summary = "根据用户返回评论分页数据" , description = "传入页码")
    public ServerResponseEntity<IPage<ProdCommDto>> getProdCommPage(PageParam page) {
        return ServerResponseEntity.success(prodCommService.getProdCommDtoPageByUserId(page, SecurityUtils.getUser().getUserId()));
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

    @PostMapping
    @Operation(summary = "添加评论")
    public ServerResponseEntity<Void> saveProdCommPage(ProdCommParam prodCommParam) {
        ProdComm prodComm = new ProdComm();
        prodComm.setProdId(prodCommParam.getProdId());
        prodComm.setOrderItemId(prodCommParam.getOrderItemId());
        prodComm.setUserId(SecurityUtils.getUser().getUserId());
        prodComm.setScore(prodCommParam.getScore());
        prodComm.setContent(prodCommParam.getContent());
        prodComm.setPics(prodCommParam.getPics());
        prodComm.setIsAnonymous(prodCommParam.getIsAnonymous());
        prodComm.setRecTime(new Date());
        prodComm.setStatus(0);
        prodComm.setEvaluate(prodCommParam.getEvaluate());
        prodCommService.save(prodComm);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除评论" , description = "根据id删除")
    public ServerResponseEntity<Void> deleteProdComm(Long prodCommId) {
        prodCommService.removeById(prodCommId);
        return ServerResponseEntity.success();
    }
}

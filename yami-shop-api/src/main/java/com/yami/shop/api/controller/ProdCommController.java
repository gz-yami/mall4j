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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.bean.app.dto.ProdCommDataDto;
import com.yami.shop.bean.app.dto.ProdCommDto;
import com.yami.shop.bean.app.param.ProdCommParam;
import com.yami.shop.bean.model.ProdComm;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.util.SecurityUtils;
import com.yami.shop.service.ProdCommService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/prodComm")
@Api(tags = "评论接口")
@AllArgsConstructor
public class ProdCommController {

    private final ProdCommService prodCommService;


    @GetMapping("/prodCommData")
    @ApiOperation(value = "返回商品评论数据(好评率 好评数量 中评数 差评数)", notes = "根据商品id获取")
    public ResponseEntity<ProdCommDataDto> getProdCommData(Long prodId) {
        return ResponseEntity.ok(prodCommService.getProdCommDataByProdId(prodId, SecurityUtils.getUser().getUserId()));
    }

    @GetMapping("/prodCommPageByUser")
    @ApiOperation(value = "根据用户返回评论分页数据", notes = "传入页码")
    public ResponseEntity<IPage<ProdCommDto>> getProdCommPage(PageParam page) {
        return ResponseEntity.ok(prodCommService.getProdCommDtoPageByUserId(page, SecurityUtils.getUser().getUserId()));
    }

    @GetMapping("/prodCommPageByProd")
    @ApiOperation(value = "根据商品返回评论分页数据", notes = "传入商品id和页码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prodId", value = "商品id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "evaluate", value = "-1或null 全部，0好评 1中评 2差评 3有图", required = true, dataType = "Long"),
    })
    public ResponseEntity<IPage<ProdCommDto>> getProdCommPageByProdId(PageParam page, Long prodId, Integer evaluate) {
        return ResponseEntity.ok(prodCommService.getProdCommDtoPageByProdId(page, prodId, evaluate, SecurityUtils.getUser().getUserId()));
    }

    @PostMapping
    @ApiOperation(value = "添加评论")
    public ResponseEntity<Void> saveProdCommPage(ProdCommParam prodCommParam) {
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
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "删除评论", notes = "根据id删除")
    public ResponseEntity<Void> deleteProdComm(Long prodCommId) {
        prodCommService.removeById(prodCommId);
        return ResponseEntity.ok().build();
    }
}

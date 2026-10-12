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
import com.yami.shop.bean.app.dto.ProdCommDto;
import com.yami.shop.bean.app.param.ProdCommParam;
import com.yami.shop.bean.model.ProdComm;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.ProdCommService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * 登录用户评论操作（需鉴权 /p/*）
 * @author lanhai
 */
@RestController
@RequestMapping("/p/prodComm")
@Tag(name = "评论接口（登录）")
@AllArgsConstructor
public class ProdCommAuthController {

    private final ProdCommService prodCommService;

    @GetMapping("/prodCommPageByUser")
    @Operation(summary = "根据用户返回评论分页数据" , description = "传入页码")
    public ServerResponseEntity<IPage<ProdCommDto>> getProdCommPage(PageParam page) {
        return ServerResponseEntity.success(prodCommService.getProdCommDtoPageByUserId(page, SecurityUtils.getUser().getUserId()));
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
    @Operation(summary = "删除评论" , description = "只能删除自己的评论")
    public ServerResponseEntity<Void> deleteProdComm(Long prodCommId) {
        ProdComm prodComm = prodCommService.getById(prodCommId);
        if (prodComm == null) {
            throw new YamiShopBindException("评论不存在");
        }
        if (!Objects.equals(prodComm.getUserId(), SecurityUtils.getUser().getUserId())) {
            throw new YamiShopBindException("无权删除该评论");
        }
        prodCommService.removeById(prodCommId);
        return ServerResponseEntity.success();
    }
}

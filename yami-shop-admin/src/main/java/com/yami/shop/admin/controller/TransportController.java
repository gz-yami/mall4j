/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Transport;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.TransportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author lgh on 2018/11/16.
 */
@RestController
@RequestMapping("/shop/transport")
public class TransportController {

    @Autowired
    private TransportService transportService;


    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('shop:transport:page')")
    public ServerResponseEntity<IPage<Transport>> page(Transport transport,PageParam<Transport> page) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        IPage<Transport> transports = transportService.page(page,
                new LambdaQueryWrapper<Transport>()
                        .eq(Transport::getShopId, shopId)
                        .like(StringUtils.isNotBlank(transport.getTransName()), Transport::getTransName, transport.getTransName()));
        return ServerResponseEntity.success(transports);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pms.hasPermission('shop:transport:info')")
    public ServerResponseEntity<Transport> info(@PathVariable("id") Long id) {
        Transport transport = transportService.getTransportAndAllItems(id);
        return ServerResponseEntity.success(transport);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('shop:transport:save')")
    public ServerResponseEntity<Void> save(@RequestBody Transport transport) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        transport.setShopId(shopId);
        Date createTime = new Date();
        transport.setCreateTime(createTime);
        transportService.insertTransportAndTransfee(transport);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('shop:transport:update')")
    public ServerResponseEntity<Void> update(@RequestBody Transport transport) {
        transportService.updateTransportAndTransfee(transport);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('shop:transport:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        transportService.deleteTransportAndTransfeeAndTranscity(ids);
        // 删除运费模板的缓存
        for (Long id : ids) {
            transportService.removeTransportAndAllItemsCache(id);
        }
        return ServerResponseEntity.success();
    }


    /**
     * 获取运费模板列表
     */
    @GetMapping("/list")
    public ServerResponseEntity<List<Transport>> list() {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        List<Transport> list = transportService.list(new LambdaQueryWrapper<Transport>().eq(Transport::getShopId, shopId));
        return ServerResponseEntity.success(list);
    }

}

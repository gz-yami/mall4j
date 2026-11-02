/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.app.dto.ProdCommDataDto;
import com.yami.shop.bean.app.dto.ProdCommDto;
import com.yami.shop.bean.app.param.ProdCommParam;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.ProdComm;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Arith;
import com.yami.shop.dao.ProdCommMapper;
import com.yami.shop.service.OrderItemService;
import com.yami.shop.service.OrderService;
import com.yami.shop.service.ProdCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 商品评论
 *
 * @author xwc
 * @date 2019-04-19 10:43:57
 */
@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService {

    @Autowired
    private ProdCommMapper prodCommMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Override
    public ProdCommDataDto getProdCommDataByProdId(Long prodId) {
        ProdCommDataDto prodCommDataDto=prodCommMapper.getProdCommDataByProdId(prodId);
        //计算出好评率
        if(prodCommDataDto.getPraiseNumber() == 0||prodCommDataDto.getNumber() == 0){
            prodCommDataDto.setPositiveRating(0.0);
        }else{
            prodCommDataDto.setPositiveRating(Arith.mul(Arith.div(prodCommDataDto.getPraiseNumber(),prodCommDataDto.getNumber()),100));
        }
        return prodCommDataDto;
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page, String userId) {
        return prodCommMapper.getProdCommDtoPageByUserId(page,userId);
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByProdId(Page page, Long prodId, Integer evaluate) {

        IPage<ProdCommDto> prodCommDtos = prodCommMapper.getProdCommDtoPageByProdId(page, prodId, evaluate);
        prodCommDtos.getRecords().forEach(prodCommDto -> {
            // 匿名评价
            if (prodCommDto.getIsAnonymous() == 1) {
                prodCommDto.setNickName(null);
            }
        });
        return prodCommDtos;
    }

    @Override
    public IPage<ProdComm> getProdCommPage(Page page,ProdComm prodComm) {
        return prodCommMapper.getProdCommPage(page,prodComm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProdComm(String userId, ProdCommParam prodCommParam) {
        if (prodCommParam.getOrderItemId() == null) {
            throw new YamiShopBindException("订单项不存在");
        }
        OrderItem orderItem = orderItemService.getById(prodCommParam.getOrderItemId());
        if (orderItem == null || !Objects.equals(orderItem.getUserId(), userId)) {
            throw new YamiShopBindException("订单项不存在");
        }
        if (!Objects.equals(orderItem.getProdId(), prodCommParam.getProdId())) {
            throw new YamiShopBindException("评价商品与订单不一致");
        }
        Order order = orderService.getOrderByOrderNumber(orderItem.getOrderNumber());
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new YamiShopBindException("订单不存在");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.CONFIRM.value())
                && !Objects.equals(order.getStatus(), OrderStatus.SUCCESS.value())) {
            throw new YamiShopBindException("当前订单状态无法评价");
        }
        if (Objects.equals(orderItem.getCommSts(), 1)
                || count(new LambdaQueryWrapper<ProdComm>().eq(ProdComm::getOrderItemId, orderItem.getOrderItemId())) > 0) {
            throw new YamiShopBindException("该订单项已评价");
        }

        ProdComm prodComm = new ProdComm();
        prodComm.setProdId(orderItem.getProdId());
        prodComm.setOrderItemId(orderItem.getOrderItemId());
        prodComm.setUserId(userId);
        prodComm.setScore(prodCommParam.getScore());
        prodComm.setContent(prodCommParam.getContent());
        prodComm.setPics(prodCommParam.getPics());
        prodComm.setIsAnonymous(prodCommParam.getIsAnonymous());
        prodComm.setRecTime(new Date());
        prodComm.setStatus(0);
        prodComm.setEvaluate(prodCommParam.getEvaluate());
        save(prodComm);

        orderItem.setCommSts(1);
        orderItemService.updateById(orderItem);
    }
}

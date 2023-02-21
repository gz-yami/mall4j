/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @author lanhai
 */
@Data
@TableName("tz_order_refund")
public class OrderRefund implements Serializable {
    /**
     * 记录ID
     */
    @TableId
    private Long refundId;

    /**
     * shopId
     */
    private Long shopId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单流水号
     */

    private String orderNumber;

    /**
     * 订单总金额
     */

    private Double orderAmount;

    /**
     * 订单项ID 全部退款是0
     */

    private Long orderItemId;

    /**
     * 退款编号
     */

    private String refundSn;

    /**
     * 订单支付流水号
     */

    private String flowTradeNo;

    /**
     * 第三方退款单号(微信退款单号)
     */

    private String outRefundNo;

    /**
     * 订单支付方式 1 微信支付 2 支付宝
     */

    private Integer payType;

    /**
     * 订单支付名称
     */

    private String payTypeName;

    /**
     * 买家ID
     */

    private String userId;

    /**
     * 退货数量
     */

    private Integer goodsNum;

    /**
     * 退款金额
     */

    private Double refundAmount;

    /**
     * 申请类型:1,仅退款,2退款退货
     */

    private Integer applyType;

    /**
     * 处理状态:1为待审核,2为同意,3为不同意
     */

    private Integer refundSts;

    /**
     * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
     */

    private Integer returnMoneySts;

    /**
     * 申请时间
     */

    private Date applyTime;

    /**
     * 卖家处理时间
     */

    private Date handelTime;

    /**
     * 退款时间
     */

    private Date refundTime;

    /**
     * 文件凭证json
     */

    private String photoFiles;

    /**
     * 申请原因
     */

    private String buyerMsg;

    /**
     * 卖家备注
     */

    private String sellerMsg;

    /**
     * 物流公司名称
     */
    private String expressName;

    /**
     * 物流单号
     */

    private String expressNo;

    /**
     * 发货时间
     */

    private Date shipTime;

    /**
     * 收货时间
     */

    private Date receiveTime;

    /**
     * 收货备注
     */

    private String receiveMessage;

    /**
     * 拒绝原因
     */
    private String rejectMessage;

    /**
     * 订单列表
     */
    @TableField(exist = false)
    private List<OrderItem> orderItems;
}
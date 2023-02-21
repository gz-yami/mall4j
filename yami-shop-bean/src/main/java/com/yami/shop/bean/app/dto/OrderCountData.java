/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lanhai
 */
@Data
@Schema(description = "我的订单数量")
public class OrderCountData {

    @Schema(description = "所有订单数量" )
    private Integer allCount;

    @Schema(description = "待付款" )
    private Integer unPay;

    @Schema(description = "待发货" )
    private Integer payed;

    @Schema(description = "待收货" )
    private Integer consignment;

    @Schema(description = "待评价" )
    private Integer confirm;

    @Schema(description = "成功" )
    private Integer success;

    @Schema(description = "失败" )
    private Integer close;


}

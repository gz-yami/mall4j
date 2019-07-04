/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CouponOrderDto implements Serializable {

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty(value = "使用条件")
    private Double cashCondition;

    @ApiModelProperty(value = "减免金额")
    private Double reduceAmount;

    @ApiModelProperty(value = "折扣")
    private Double couponDiscount;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "适用商品类型 0全部商品参与 1指定商品参与 2指定商品不参与")
    private Integer suitableProdType;

    @ApiModelProperty(value = "指定的商品id")
    private List<Long> prodIds;

    @ApiModelProperty(value = "选中的商品id")
    private List<Long> chooseProdIds = new ArrayList<>();

    @ApiModelProperty(value = "是否选中")
    private boolean isChoose;

    @ApiModelProperty(value = "是否可用")
    private boolean canUse;

    @ApiModelProperty(value = "用户优惠券id")
    private Long couponUserId;

    @ApiModelProperty(value = "生效时间 1:固定时间 2:领取后生效")
    private Integer validTimeType;

    @ApiModelProperty(value = "领券后X天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty(value = "有效天数")
    private Integer validDays;

    @ApiModelProperty(value = "领券时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

}

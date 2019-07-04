package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderRefundExpressParam {

    @ApiModelProperty(value = "退款编号名称", required = true)
    @NotEmpty(message = "退款编号不能为空")
    private String refundSn;

    @ApiModelProperty(value = "物流公司名称", required = true)
    @NotEmpty(message = "物流公司名称不能为空")
    private String expressName;

    @ApiModelProperty(value = "物流单号", required = true)
    @NotEmpty(message = "物流单号不能为空")
    private String expressNo;


}

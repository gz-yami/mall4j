package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author lanhai
 */
@Data
public class OrderRefundExpressParam {

    @Schema(description = "退款编号名称" , required = true)
    @NotEmpty(message = "退款编号不能为空")
    private String refundSn;

    @Schema(description = "物流公司名称" , required = true)
    @NotEmpty(message = "物流公司名称不能为空")
    private String expressName;

    @Schema(description = "物流单号" , required = true)
    @NotEmpty(message = "物流单号不能为空")
    private String expressNo;


}

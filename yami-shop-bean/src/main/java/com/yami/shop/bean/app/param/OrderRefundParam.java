package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author lanhai
 */
@Data
public class OrderRefundParam {

    @Schema(description = "订单编号" ,requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单编号不能为空")
    private String orderNumber;

    @Schema(description = "申请类型(1:仅退款 2退款退货)" ,requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申请类型不能为空")
    private Integer applyType;


    @Schema(description = "订单项id(全部退款是0)" ,requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单项id不能为空")
    private Long orderItemId;


    @Schema(description = "凭证图片列表" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoFiles;

    @Schema(description = "申请原因" ,requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单编号不能为空")
    private String buyerMsg;

}

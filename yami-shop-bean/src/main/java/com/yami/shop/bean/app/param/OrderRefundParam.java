package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lanhai
 */
@Data
public class OrderRefundParam {

    @Schema(description = "订单编号" , required = true)
    @NotEmpty(message = "订单编号不能为空")
    private String orderNumber;

    @Schema(description = "申请类型(1:仅退款 2退款退货)" , required = true)
    @NotNull(message = "申请类型不能为空")
    private Integer applyType;


    @Schema(description = "订单项id(全部退款是0)" , required = true)
    @NotNull(message = "订单项id不能为空")
    private Long orderItemId;


    @Schema(description = "凭证图片列表" , required = true)
    private String photoFiles;

    @Schema(description = "申请原因" , required = true)
    @NotEmpty(message = "订单编号不能为空")
    private String buyerMsg;

}

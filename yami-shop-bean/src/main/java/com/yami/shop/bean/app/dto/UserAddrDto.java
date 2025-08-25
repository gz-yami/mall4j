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

import java.io.Serializable;
/**
 * @author lanhai
 */
@Data
public class UserAddrDto implements Serializable {
        @Schema(description = "地址id" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long addrId;

    @Schema(description = "收货人" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiver;

    @Schema(description = "省" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String province;

    @Schema(description = "城市" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "区" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String area;

    @Schema(description = "地址" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String addr;

    @Schema(description = "手机" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobile;

    @Schema(description = "是否默认地址（1:是 0：否） " ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer commonAddr;

    /**
     * 省ID
     */
    @Schema(description = "省ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long provinceId;

    /**
     * 城市ID
     */
    @Schema(description = "城市ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long cityId;

    /**
     * 区域ID
     */
    @Schema(description = "区域ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private Long areaId;
}

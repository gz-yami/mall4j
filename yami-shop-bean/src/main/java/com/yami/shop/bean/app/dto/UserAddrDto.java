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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddrDto implements Serializable {
        @ApiModelProperty(value = "地址id", required = true)
    private Long addrId;

    @ApiModelProperty(value = "收货人", required = true)
    private String receiver;

    @ApiModelProperty(value = "省", required = true)
    private String province;

    @ApiModelProperty(value = "城市", required = true)
    private String city;

    @ApiModelProperty(value = "区", required = true)
    private String area;

    @ApiModelProperty(value = "地址", required = true)
    private String addr;

    @ApiModelProperty(value = "手机", required = true)
    private String mobile;

    @ApiModelProperty(value = "是否默认地址（1:是 0：否） ", required = true)
    private Integer commonAddr;

    /**
     * 省ID
     */
    @ApiModelProperty(value = "省ID", required = true)
    private Long provinceId;

    /**
     * 城市ID
     */
    @ApiModelProperty(value = "城市ID", required = true)
    private Long cityId;

    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID", required = true)
    private Long areaId;
}

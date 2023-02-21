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
public class DeliveryInfoDto {
	
	@Schema(description = "详细信息" ,required=true)
	private String context;
	
	private String ftime;
	
	@Schema(description = "快递所在区域" ,required=true)
	private String location;
	
	@Schema(description = "物流更新时间" ,required=true)
	private String time;
	
}

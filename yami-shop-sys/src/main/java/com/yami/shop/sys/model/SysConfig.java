/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 系统配置信息
 * @author lanhai
 */
@Data
@TableName("tz_sys_config")
public class SysConfig {
	@TableId
	private Long id;

	@NotBlank(message="参数名不能为空")
	private String paramKey;

	@NotBlank(message="参数值不能为空")
	private String paramValue;

	private String remark;

}

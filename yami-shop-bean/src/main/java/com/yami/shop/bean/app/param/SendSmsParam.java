/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Pattern;

/**
 * @author lanhai
 */
@Schema(description = "发送验证码参数")
public class SendSmsParam {
	
	@Schema(description = "手机号" )
	@Pattern(regexp="1[0-9]{10}",message = "请输入正确的手机号")
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
}

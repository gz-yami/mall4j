/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value= "登陆参数")
public class LoginParam {

	@ApiModelProperty(value = "小程序登陆时返回的code(使用code登陆必填)",required=true)
	private String code;
	@ApiModelProperty(value = "登陆时的用户名(账号密码登陆必填)",required=true)
	private String mobile;
	@ApiModelProperty(value = "登陆时的密码(账号密码登陆必填)",required=true)
	private String password;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

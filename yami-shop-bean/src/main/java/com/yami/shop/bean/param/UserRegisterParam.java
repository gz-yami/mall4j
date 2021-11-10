/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value= "设置用户信息")
public class UserRegisterParam {

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "邮箱")
	private String userMail;

	@ApiModelProperty(value = "昵称")
	private String nickName;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "应用类型 1小程序 2微信公众号 3 PC 4 h5")
	private Integer appType;

	@ApiModelProperty(value = "手机号")
	private String mobile;

	@ApiModelProperty(value = "验证码")
	private String validCode;

	@ApiModelProperty(value = "微信小程序的encryptedData")
	private String encryptedData;

	@ApiModelProperty(value = "微信小程序的ivStr")
	private String ivStr;

	@ApiModelProperty(value = "头像")
	private String img;

	@ApiModelProperty(value = "校验登陆注册验证码成功的标识")
	private String checkRegisterSmsFlag;

	@ApiModelProperty(value = "验证类型 1验证码验证 2 小程序encryptedData验证 3 密码验证 ")
	private Integer validateType;

	@ApiModelProperty(value = "验证类型 1注册 2绑定 ")
	private Integer registerOrBind;
}

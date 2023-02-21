/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lh
 */
@Data
@Schema(description = "设置用户信息")
public class UserRegisterParam {

	@Schema(description = "密码" )
	private String passWord;

	@Schema(description = "邮箱" )
	private String userMail;

	@Schema(description = "昵称" )
	private String nickName;

	@Schema(description = "用户名" )
	private String userName;

	@Schema(description = "手机号" )
	private String mobile;

	@Schema(description = "头像" )
	private String img;

	@Schema(description = "校验登陆注册验证码成功的标识" )
	private String checkRegisterSmsFlag;

	@Schema(description = "当账户未绑定时，临时的uid" )
	private String tempUid;

	@Schema(description = "用户id" )
	private Long userId;
}

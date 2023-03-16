/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * @author lanhai
 */
@Data
@Schema(description = "更新密码参数")
public class UpdatePasswordDto {

	@NotBlank(message="旧密码不能为空")
	@Size(max = 50)
	@Schema(description = "旧密码" ,required=true)
	private String password;

	@NotBlank(message="新密码不能为空")
	@Size(max = 50)
	@Schema(description = "新密码" ,required=true)
	private String newPassword;
}

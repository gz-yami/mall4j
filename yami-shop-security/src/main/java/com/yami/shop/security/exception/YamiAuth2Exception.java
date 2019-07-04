/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 */
public abstract class YamiAuth2Exception extends AuthenticationException {

	public YamiAuth2Exception(String msg) {
		super(msg);
	}

	public int getHttpErrorCode() {
		// 400 not 401
		return HttpStatus.BAD_REQUEST.value();
	}

	public abstract String getOAuth2ErrorCode();
}

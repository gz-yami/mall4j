/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.bean;

import lombok.Data;

/**
 * 本地存储配置信息
 * @author lgh
 */
@Data
public class ImgUpload {

	/**
	 * 本地文件上传文件夹
	 */
	private String imagePath;

	/**
	 * 文件上传方式 1.本地文件上传 2.七牛云
	 */
	private Integer uploadType;

	/**
	 * 网站url
	 */
	private String resourceUrl;
}

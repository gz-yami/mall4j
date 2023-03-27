/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.AttachFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author lanhai
 * Created by lgh on 2018/07/27.
 */
public interface AttachFileService extends IService<AttachFile> {

	/**
	 * 上传文件到本地
	 * @param file
	 * @throws IOException e
	 * @return
	 */
	String uploadFile(MultipartFile file) throws IOException;

	/**
	 * 删除文件
	 * @param fileName 文件名称
	 */
	void deleteFile(String fileName);
}

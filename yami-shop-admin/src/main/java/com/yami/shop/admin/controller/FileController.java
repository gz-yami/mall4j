/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yami.shop.bean.dto.TinymceEditorDto;
import com.yami.shop.common.bean.Qiniu;
import com.yami.shop.service.AttachFileService;

/**
 * 文件上传 controller
 * @author lgh
 *
 */
@RestController
@RequestMapping("/admin/file")
public class FileController {
	
	@Autowired
	private AttachFileService attachFileService;
	@Autowired
	private Qiniu qiniu;
	
	@PostMapping("/upload/element")
	public ResponseEntity<String> uploadElementFile(@RequestParam("file") MultipartFile file) throws IOException{
		if(file.isEmpty()){
            return ResponseEntity.noContent().build();
        }
		String fileName = attachFileService.uploadFile(file.getBytes(),file.getOriginalFilename());
        return ResponseEntity.ok(fileName);
	}
	
	@PostMapping("/upload/tinymceEditor")
	public ResponseEntity<String> uploadTinymceEditorImages(@RequestParam("editorFile") MultipartFile editorFile) throws IOException{
		String fileName =  attachFileService.uploadFile(editorFile.getBytes(),editorFile.getOriginalFilename());
        return ResponseEntity.ok(qiniu.getResourcesUrl() + fileName);
	}
	
}

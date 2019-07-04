/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yami.shop.sys.service.SysLogService;
import com.yami.shop.sys.dao.SysLogMapper;
import com.yami.shop.sys.model.SysLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author lgh
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	@Autowired
	private SysLogMapper sysLogMapper;
}

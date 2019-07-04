/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yami.shop.bean.model.ScoreLog;
import com.yami.shop.dao.ScoreLogMapper;
import com.yami.shop.service.ScoreLogService;

/**
 *
 * @author lgh on 2018/10/15.
 */
@Service
public class ScoreLogServiceImpl extends ServiceImpl<ScoreLogMapper, ScoreLog> implements ScoreLogService {

    @Autowired
    private ScoreLogMapper scoreLogMapper;

}

/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.dto.NoticeDto;
import com.yami.shop.bean.model.Notice;

import java.util.List;

/**
 * 公告管理
 *
 * @author hzm
 * @date 2019-04-18 21:21:40
 */
public interface NoticeService extends IService<Notice> {

    List<Notice> listNotice();

    void removeNoticeList();

    Page<NoticeDto> pageNotice(Page<NoticeDto> page);

    Notice getNoticeById(Long noticeId);

    void removeNoticeById(Long noticeId);
}

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

    /**
     * 获取公告列表
     * @return
     */
    List<Notice> listNotice();

    /**
     * 删除公告缓存
     */
    void removeNoticeList();

    /**
     * 分页获取公布的公告
     * @param page
     * @return
     */
    Page<NoticeDto> pageNotice(Page<NoticeDto> page);

    /**
     * 根据公告id获取公告
     * @param noticeId
     * @return
     */
    Notice getNoticeById(Long noticeId);

    /**
     * 根据公告id删除公告
     * @param noticeId
     */
    void removeNoticeById(Long noticeId);
}

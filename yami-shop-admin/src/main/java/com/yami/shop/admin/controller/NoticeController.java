/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Notice;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.NoticeService;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

/**
 * 公告管理
 *
 * @author hzm
 * @date
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 分页查询
     *
     * @param page   分页对象
     * @param notice 公告管理
     * @return 分页数据
     */
    @GetMapping("/page")
    public ServerResponseEntity<IPage<Notice>> getNoticePage(PageParam<Notice> page, Notice notice) {
        IPage<Notice> noticePage = noticeService.page(page, new LambdaQueryWrapper<Notice>()
                .eq(notice.getStatus() != null, Notice::getStatus, notice.getStatus())
                .eq(notice.getIsTop()!=null,Notice::getIsTop,notice.getIsTop())
                .like(notice.getTitle() != null, Notice::getTitle, notice.getTitle()).orderByDesc(Notice::getUpdateTime));
        return ServerResponseEntity.success(noticePage);
    }


    /**
     * 通过id查询公告管理
     *
     * @param id id
     * @return 单个数据
     */
    @GetMapping("/info/{id}")
    public ServerResponseEntity<Notice> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(noticeService.getById(id));
    }

    /**
     * 新增公告管理
     *
     * @param notice 公告管理
     * @return 是否新增成功
     */
    @SysLog("新增公告管理")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('shop:notice:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid Notice notice) {
        notice.setShopId(SecurityUtils.getSysUser().getShopId());
        if (notice.getStatus() == 1) {
            notice.setPublishTime(new Date());
        }
        notice.setUpdateTime(new Date());
        noticeService.removeNoticeList();
        return ServerResponseEntity.success(noticeService.save(notice));
    }

    /**
     * 修改公告管理
     *
     * @param notice 公告管理
     * @return 是否修改成功
     */
    @SysLog("修改公告管理")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('shop:notice:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid Notice notice) {
        Notice oldNotice = noticeService.getById(notice.getId());
        if (oldNotice.getStatus() == 0 && notice.getStatus() == 1) {
            notice.setPublishTime(new Date());
        }
        notice.setUpdateTime(new Date());
        noticeService.removeNoticeList();
        noticeService.removeNoticeById(notice.getId());
        return ServerResponseEntity.success(noticeService.updateById(notice));
    }

    /**
     * 通过id删除公告管理
     *
     * @param id id
     * @return 是否删除成功
     */
    @SysLog("删除公告管理")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('shop:notice:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long id) {
        noticeService.removeNoticeList();
        noticeService.removeNoticeById(id);
        return ServerResponseEntity.success(noticeService.removeById(id));
    }

}

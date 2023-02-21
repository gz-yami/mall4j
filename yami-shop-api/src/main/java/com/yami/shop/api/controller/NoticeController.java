/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonView;
import com.yami.shop.bean.app.dto.NoticeDto;
import com.yami.shop.bean.model.Notice;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/shop/notice")
@Tag(name = "公告管理接口")
@AllArgsConstructor
public class NoticeController {

    private NoticeService noticeService;

    private MapperFacade mapperFacade;

    /**
     * 置顶公告列表接口
     */
    @GetMapping("/topNoticeList")
    @Operation(summary = "置顶公告列表信息" , description = "获取所有置顶公告列表信息")
    @JsonView(NoticeDto.NoContent.class)
    public ResponseEntity<List<NoticeDto>> getTopNoticeList() {
        List<Notice> noticeList = noticeService.listNotice();
        List<NoticeDto> noticeDtoList = mapperFacade.mapAsList(noticeList, NoticeDto.class);
        return ResponseEntity.ok(noticeDtoList);
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/info/{id}")
    @Operation(summary = "公告详情" , description = "获取公告id公告详情")
    @JsonView(NoticeDto.WithContent.class)
    public ResponseEntity<NoticeDto> getNoticeById(@PathVariable("id") Long id) {
        Notice notice = noticeService.getNoticeById(id);
        NoticeDto noticeDto = mapperFacade.map(notice, NoticeDto.class);
        return ResponseEntity.ok(noticeDto);
    }

    /**
     * 公告列表
     */
    @GetMapping("/noticeList")
    @Operation(summary = "公告列表信息" , description = "获取所有公告列表信息")
    @Parameters({
    })
    public ResponseEntity<IPage<NoticeDto>> pageNotice(PageParam<NoticeDto> page) {

        return ResponseEntity.ok(noticeService.pageNotice(page));
    }
}

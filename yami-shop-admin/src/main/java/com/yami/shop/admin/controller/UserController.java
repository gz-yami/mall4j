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

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;


/**
 * @author lgh on 2018/10/16.
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:user:page')")
    public ResponseEntity<IPage<User>> page(User user,PageParam<User> page) {
        IPage<User> userPage = userService.page(page, new LambdaQueryWrapper<User>()
                .like(StrUtil.isNotBlank(user.getNickName()), User::getNickName, user.getNickName())
                .eq(user.getStatus() != null, User::getStatus, user.getStatus()));
        for (User userResult : userPage.getRecords()) {
            userResult.setNickName(EmojiUtil.toUnicode(userResult.getNickName() == null ? "" : userResult.getNickName()));
        }
        return ResponseEntity.ok(userPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{userId}")
    @PreAuthorize("@pms.hasPermission('admin:user:info')")
    public ResponseEntity<User> info(@PathVariable("userId") String userId) {
        User user = userService.getById(userId);
        user.setNickName(EmojiUtil.toUnicode(user.getNickName() == null ? "" : user.getNickName()));
        return ResponseEntity.ok(user);
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:user:update')")
    public ResponseEntity<Void> update(@RequestBody User user) {
        user.setModifyTime(new Date());
        user.setNickName(EmojiUtil.toAlias(user.getNickName() == null ? "" : user.getNickName()));
        userService.updateById(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin:user:delete')")
    public ResponseEntity<Void> delete(@RequestBody String[] userIds) {
        userService.removeByIds(Arrays.asList(userIds));
        return ResponseEntity.ok().build();
    }
}

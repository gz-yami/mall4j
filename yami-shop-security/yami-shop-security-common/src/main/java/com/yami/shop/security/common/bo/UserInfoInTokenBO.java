/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.bo;

import lombok.Data;

import java.util.Set;

/**
 * 保存在token信息里面的用户信息
 *
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@Data
public class UserInfoInTokenBO {

    /**
     * 用户在自己系统的用户id
     */
    private String userId;

    /**
     * 租户id (商家id)
     */
    private Long shopId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 系统类型
     * @see com.yami.shop.security.common.enums.SysTypeEnum
     */
    private Integer sysType;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    private String bizUserId;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 状态 1 正常 0 无效
     */
    private Boolean enabled;

    /**
     * 其他Id
     */
    private Long otherId;

}

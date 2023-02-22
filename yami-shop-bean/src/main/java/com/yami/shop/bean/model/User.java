/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
/**
 * @author lanhai
 */
@Data
@TableName("tz_user")
public class User implements Serializable {
    private static final long serialVersionUID = 2090714647038636896L;
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */

    private String realName;

    /**
     * 用户邮箱
     */

    private String userMail;

    /**
     * 登录密码
     */

    private String loginPassword;

    /**
     * 支付密码
     */

    private String payPassword;

    /**
     * 手机号码
     */

    private String userMobile;

    /**
     * 修改时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 注册时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userRegtime;

    /**
     * 注册IP
     */

    private String userRegip;

    /**
     * 最后登录时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userLasttime;

    /**
     * 最后登录IP
     */

    private String userLastip;

    /**
     * 备注
     */

    private String userMemo;

    /**
     * M(男) or F(女)
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 积分
     */
    private Integer score;

}

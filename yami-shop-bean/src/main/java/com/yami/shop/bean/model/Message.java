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
@TableName("tz_message")
public class Message implements Serializable {
    @TableId
    private Long id;

    /**
     * 留言创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 状态：0:未审核 1审核通过
     */
    private Integer status;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言回复
     */
    private String reply;

}

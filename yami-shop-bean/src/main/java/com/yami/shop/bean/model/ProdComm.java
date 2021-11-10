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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.bean.vo.UserVO;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品评论
 *
 * @author xwc
 * @date 2019-04-19 10:43:57
 */
@Data
@TableName("tz_prod_comm")
@EqualsAndHashCode
public class ProdComm implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long prodCommId;
    /**
     * 商品ID
     */
    private Long prodId;
    /**
     * 订单项ID
     */
    private Long orderItemId;
    /**
     * 评论用户ID
     */
    private String userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 掌柜回复
     */
    private String replyContent;
    /**
     * 记录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recTime;
    /**
     * 回复时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;
    /**
     * 是否回复 0:未回复  1:已回复
     */
    private Integer replySts;
    /**
     * IP来源
     */
    private String postip;
    /**
     * 得分，0-5分
     */
    private Integer score;
    /**
     * 有用的计数
     */
    private Integer usefulCounts;
    /**
     * 晒图的字符串 以逗号分隔
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pics;
    /**
     * 是否匿名(1:是  0:否)
     */
    private Integer isAnonymous;
    /**
     * 是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1
     */
    private Integer status;

    /**
     * 评价(0好评 1中评 2差评)
     */
    private Integer evaluate;

    /**
     * 关联用户
     */
    @TableField(exist = false)
    private UserVO user;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String prodName;
}

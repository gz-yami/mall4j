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

import java.io.Serializable;
import java.util.Date;

/**
 * @author lanhai
 */
@Data
@TableName("tz_attach_file")
public class AttachFile implements Serializable {
    @TableId

    private Long fileId;

    /**
     * 文件路径
     */

    private String filePath;

    /**
     * 文件类型
     */

    private String fileType;

    /**
     * 文件大小
     */

    private Integer fileSize;

    /**
     * 上传时间
     */

    private Date uploadTime;

    /**
     * 文件关联的表主键id
     */

    private Long fileJoinId;
    
    /**
     * 文件关联表类型：1 商品表  @see FileJoinType
     */

    private Integer fileJoinType;
}
/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.quartz.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tz_schedule_job_log")
public class ScheduleJobLog {
    /**
     * 任务日志id
     */
    @TableId

    private Long logId;

    /**
     * 任务id
     */

    private Long jobId;

    /**
     * spring bean名称
     */

    private String beanName;

    /**
     * 方法名
     */

    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务状态    1：成功    0：失败
     */
    private Integer status;

    /**
     * 失败信息
     */
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    private Integer times;

    /**
     * 创建时间
     */

    private Date createTime;
}
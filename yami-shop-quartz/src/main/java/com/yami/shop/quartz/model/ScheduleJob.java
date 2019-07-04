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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_schedule_job")
public class ScheduleJob  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9150018026309004044L;

	/**
     * 任务id
     */
    @TableId
    private Long jobId;

    /**
     * spring bean名称
     */

    @NotBlank(message="bean名称不能为空")
    private String beanName;

    /**
     * 方法名
     */

    @NotBlank(message="方法名称不能为空")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */

    @NotBlank(message="cron表达式不能为空")
    private String cronExpression;

    /**
     * 任务状态  0：正常  1：暂停
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */

    private Date createTime;
}
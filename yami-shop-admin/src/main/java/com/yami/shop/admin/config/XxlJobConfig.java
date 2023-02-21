/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.admin.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 * 为啥这里的代码要注释掉，因为很奇怪的，因为很多人都不会去下载xxl-job来跑定时任务。
 * 原本用spring 的 quartz做定时任务的，但是文档写着要忽略数据库大小写，结果很多人根本不看文档，所以干脆就把这个quartz的删掉，然后通过xxl-job执行定时任务
 * 那么又会带来一个问题，大家也不会看文档，也不会去下载xxl-job，所以要把这里面的代码注掉，要是有人需要的话，改下这里的连接配置，连上xxl-job即可。
 * 毕竟你都能找到这个文件了，下载xxl-job，修改并启动xxl-job-admin，把取消订单，确认收货之类定时任务加入到里面即可咯。还要把下面的注释掉的代码打开，这样启动项目就会连接xxl-job执行定时任务了。
 * @author FrozenWatermelon
 * @date 2021/1/18
 */
@Configuration
public class XxlJobConfig {
    private final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl-job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl-job.accessToken}")
    private String accessToken;

    @Value("${xxl-job.logPath}")
    private String logPath;

    @Value("${server.port}")
    private int port;

    @Autowired
    private InetUtils inetUtils;

//    @Bean
//    public XxlJobSpringExecutor xxlJobExecutor() {
//
//        logger.info(">>>>>>>>>>> xxl-job config init.");
//        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//        xxlJobSpringExecutor.setAppname("mall4j");
//        // 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP
//        xxlJobSpringExecutor.setIp(inetUtils.findFirstNonLoopbackAddress().getHostAddress());
//        xxlJobSpringExecutor.setPort(port + 1000);
//        xxlJobSpringExecutor.setAccessToken(accessToken);
//        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(3);
//        return xxlJobSpringExecutor;
//    }

}

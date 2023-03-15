/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.enums.SmsType;
import com.yami.shop.bean.model.SmsLog;
import com.yami.shop.common.bean.AliDaYu;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Json;
import com.yami.shop.dao.SmsLogMapper;
import com.yami.shop.service.SmsLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author lgh on 2018/11/29.
 */
@Service
@Slf4j
@AllArgsConstructor
public class SmsLogServiceImpl extends ServiceImpl<SmsLogMapper, SmsLog> implements SmsLogService {

    private final SmsLogMapper smsLogMapper;

    private final AliDaYu aLiDaYu;

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String PRODUCT = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 当天最大验证码短信发送量
     */
    private static final int TODAY_MAX_SEND_VALID_SMS_NUMBER = 10;

    /**
     * 一段时间内短信验证码的最大验证次数
     */
    private static final int TIMES_CHECK_VALID_CODE_NUM = 10;

    /**
     * 短信验证码的前缀
     */
    private static final String CHECK_VALID_CODE_NUM_PREFIX = "checkValidCodeNum_";

    /**
     * 短信发送成功的标志
     */
    private static final String SEND_SMS_SUCCESS_FLAG = "OK";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void sendSms(SmsType smsType, String userId, String mobile, Map<String, String> params) {

        SmsLog smsLog = new SmsLog();
        if (smsType.equals(SmsType.VALID)) {
            long todaySendSmsNumber = smsLogMapper.selectCount(new LambdaQueryWrapper<SmsLog>()
                    .gt(SmsLog::getRecDate, DateUtil.beginOfDay(new Date()))
                    .lt(SmsLog::getRecDate, DateUtil.endOfDay(new Date()))
                    .eq(SmsLog::getUserId, userId)
                    .eq(SmsLog::getType, smsType.value()));
            if (todaySendSmsNumber >= TODAY_MAX_SEND_VALID_SMS_NUMBER) {
                throw new YamiShopBindException("今日发送短信验证码次数已达到上限");
            }

            // 将上一条验证码失效
            smsLogMapper.invalidSmsByMobileAndType(mobile, smsType.value());

            String code = RandomUtil.randomNumbers(6);
            params.put("code", code);
        }
        smsLog.setType(smsType.value());
        smsLog.setMobileCode(params.get("code"));
        smsLog.setRecDate(new Date());
        smsLog.setStatus(1);
        smsLog.setUserId(userId);
        smsLog.setUserPhone(mobile);
        smsLog.setContent(formatContent(smsType, params));
        smsLogMapper.insert(smsLog);
        try {
            this.sendSms(mobile, smsType.getTemplateCode(), params);
        } catch (ClientException e) {
            throw new YamiShopBindException("发送短信失败，请稍后再试");
        }


    }

    private void sendSms(String mobile, String templateCode, Map<String, String> params) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aLiDaYu.getAccessKeyId(), aLiDaYu.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(aLiDaYu.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        request.setTemplateParam(Json.toJsonString(params));


        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        log.debug(Json.toJsonString(sendSmsResponse));
        if (sendSmsResponse.getCode() == null || !SEND_SMS_SUCCESS_FLAG.equals(sendSmsResponse.getCode())) {
            throw new YamiShopBindException("发送短信失败，请稍后再试:" + sendSmsResponse.getMessage());
        }
    }

    private String formatContent(SmsType smsType, Map<String, String> params) {
        if (CollectionUtil.isEmpty(params)) {
            return smsType.getContent();
        }
        String content = smsType.getContent();
        for (Entry<String, String> element : params.entrySet()) {
            content = content.replace("${" + element.getKey() + "}", element.getValue());
        }
        return content;
    }
}

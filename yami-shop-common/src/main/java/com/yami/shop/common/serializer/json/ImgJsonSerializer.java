/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.serializer.json;

import cn.hutool.core.util.StrUtil;
import tools.jackson.core.JsonGenerator;
import com.yami.shop.common.bean.Qiniu;
import com.yami.shop.common.util.ImgUploadUtil;
import com.yami.shop.common.util.SpringContextUtils;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lanhai
 */
public class ImgJsonSerializer extends ValueSerializer<String> {

    private Qiniu qiniu;

    private ImgUploadUtil imgUploadUtil;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializationContext serializers) {
        // 如果图片字段为空，则直接按空串返回，避免后续拼接逻辑继续处理。
        if (StrUtil.isBlank(value)) {
            gen.writeString(StrUtil.EMPTY);
            return;
        }

        // Jackson 3 升级后，该序列化器可能由 Jackson 直接实例化，因此这里要主动兜底获取 Spring Bean。
        initBeansIfNecessary();

        String[] imgs = value.split(StrUtil.COMMA);
        StringBuilder sb = new StringBuilder();
        String resourceUrl = "";
        String rule = "^((http[s]{0,1})://)";
        Pattern pattern = Pattern.compile(rule);

        // 根据当前图片上传方式，决定返回七牛云地址还是本地资源前缀。
        if (Objects.equals(imgUploadUtil.getUploadType(), 2)) {
            resourceUrl = qiniu.getResourcesUrl();
        } else if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
            resourceUrl = imgUploadUtil.getResourceUrl();
        }

        // 逐个处理逗号分隔的图片路径，已经是完整 http/https 地址的就原样返回。
        for (String img : imgs) {
            Matcher matcher = pattern.matcher(img);
            // 若图片以 http 或 https 开头，说明已经是完整地址，不再重复拼接资源前缀。
            if (matcher.find()) {
                sb.append(img).append(StrUtil.COMMA);
            } else {
                sb.append(resourceUrl).append(img).append(StrUtil.COMMA);
            }
        }

        // 删除最后一个逗号，保证返回给前端的是合法的逗号分隔图片串。
        sb.deleteCharAt(sb.length() - 1);
        gen.writeString(sb.toString());
    }

    /**
     * 初始化序列化器依赖
     */
    private void initBeansIfNecessary() {
        // 只有在依赖未初始化时才从 Spring 容器中获取，避免每次序列化都重复查找 Bean。
        if (imgUploadUtil == null) {
            imgUploadUtil = SpringContextUtils.getBean("imgUploadUtil", ImgUploadUtil.class);
        }
        // 七牛云配置只在上传方式为云存储时会用到，这里统一懒加载，保持逻辑简单稳定。
        if (qiniu == null) {
            qiniu = SpringContextUtils.getBean("qiniu", Qiniu.class);
        }
    }
}

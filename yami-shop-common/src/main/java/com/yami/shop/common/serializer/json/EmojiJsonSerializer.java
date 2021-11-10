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
import cn.hutool.extra.emoji.EmojiUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 带有emoji字符串的json序列化
 * @author LGH
 */
@Component
public class EmojiJsonSerializer extends JsonSerializer<String> {

    @Override
    @SneakyThrows
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
        gen.writeString(EmojiUtil.toUnicode(StrUtil.isBlank(value)? "" : value));
    }
}

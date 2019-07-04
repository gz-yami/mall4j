/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.serializer.redis;

import com.yami.shop.common.serializer.FSTSerializer;
import lombok.SneakyThrows;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

/**
 * 使用fst 进行reids的序列化
 * @author LGH
 */
public class FstRedisSerializer implements RedisSerializer<Object> {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    @Override
    @SneakyThrows
    public byte[] serialize(Object o) {
        if (o == null) {
            return EMPTY_ARRAY;
        }
        return new FSTSerializer().getConfig().asByteArray(o);
    }

    @Override
    @SneakyThrows
    public Object deserialize(byte[] bytes) {
        if (isEmpty(bytes)) {
            return null;
        }
        return new FSTSerializer().getConfig().asObject(bytes);
    }

    private static boolean isEmpty(@Nullable byte[] data) {
        return (data == null || data.length == 0);
    }
}

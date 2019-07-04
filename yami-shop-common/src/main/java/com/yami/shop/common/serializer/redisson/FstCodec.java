/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.serializer.redisson;

import java.io.IOException;

import com.yami.shop.common.serializer.FSTSerializer;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

/**
 * 被redisson使用
 * Efficient and speedy serialization codec fully
 * compatible with JDK Serialization codec.
 *
 * https://github.com/RuedigerMoeller/fast-serialization
 *
 * @author Nikita Koksharov
 *
 */
public class FstCodec extends BaseCodec {


    private final FSTConfiguration config;

    public FstCodec() {
        config = new FSTSerializer().getConfig();
    }


    private final Decoder<Object> decoder = new Decoder<Object>() {
        @Override
        public Object decode(ByteBuf buf, State state) throws IOException {
            ByteBufInputStream in = new ByteBufInputStream(buf);
            FSTObjectInput inputStream = config.getObjectInput(in);
            try {
                return inputStream.readObject();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    };

    private final Encoder encoder = new Encoder() {

        @Override
        public ByteBuf encode(Object in) throws IOException {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            FSTObjectOutput oos = config.getObjectOutput(os);
            try {
                oos.writeObject(in);
                oos.flush();
                return os.buffer();
            } catch (IOException e) {
                out.release();
                throw e;
            } catch (Exception e) {
                out.release();
                throw new IOException(e);
            }
        }
    };

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (config.getClassLoader() != null) {
            return config.getClassLoader();
        }

        return super.getClassLoader();
    }

}

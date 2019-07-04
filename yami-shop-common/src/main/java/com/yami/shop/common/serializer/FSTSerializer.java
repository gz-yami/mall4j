/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.serializer;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTDecoder;
import org.nustaq.serialization.FSTEncoder;
import org.nustaq.serialization.coders.FSTStreamDecoder;
import org.nustaq.serialization.coders.FSTStreamEncoder;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 使用fts进行序列化
 * @author LGH
 */
public class FSTSerializer {

    static class FSTDefaultStreamCoderFactory implements FSTConfiguration.StreamCoderFactory {

        Field chBufField;
        Field ascStringCacheField;

        {
            try {
                chBufField = FSTStreamDecoder.class.getDeclaredField("chBufS");
                ascStringCacheField = FSTStreamDecoder.class.getDeclaredField("ascStringCache");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            ascStringCacheField.setAccessible(true);
            chBufField.setAccessible(true);
        }

        private FSTConfiguration fstConfiguration;

        FSTDefaultStreamCoderFactory(FSTConfiguration fstConfiguration) {
            this.fstConfiguration = fstConfiguration;
        }

        @Override
        public FSTEncoder createStreamEncoder() {
            return new FSTStreamEncoder(fstConfiguration);
        }

        @Override
        public FSTDecoder createStreamDecoder() {
            return new FSTStreamDecoder(fstConfiguration) {
                @Override
                public String readStringUTF() throws IOException {
                    try {
                        String res = super.readStringUTF();
                        chBufField.set(this, null);
                        return res;
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }

                @Override
                public String readStringAsc() throws IOException {
                    try {
                        String res = super.readStringAsc();
                        ascStringCacheField.set(this, null);
                        return res;
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            };
        }

        static ThreadLocal input = new ThreadLocal();
        static ThreadLocal output = new ThreadLocal();

        @Override
        public ThreadLocal getInput() {
            return input;
        }

        @Override
        public ThreadLocal getOutput() {
            return output;
        }

    }

    private static class InstanceHolder {
        private static final FSTConfiguration INSTANCE = FSTConfiguration.createDefaultConfiguration();
        static {
            INSTANCE.setStreamCoderFactory(new FSTDefaultStreamCoderFactory(INSTANCE));
        }
    }

    public FSTConfiguration getConfig() {
        return InstanceHolder.INSTANCE;
    }
}

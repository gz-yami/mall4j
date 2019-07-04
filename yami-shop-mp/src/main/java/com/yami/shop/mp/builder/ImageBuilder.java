/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.mp.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class ImageBuilder extends AbstractBuilder {

    @Override
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage,
                                   WxMpService service) {

        WxMpXmlOutImageMessage m = WxMpXmlOutMessage.IMAGE().mediaId(content)
            .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
            .build();

        return m;
    }

}

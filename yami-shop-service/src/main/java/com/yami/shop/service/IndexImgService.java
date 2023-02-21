/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.IndexImg;

import java.util.List;

/**
 *
 * @author lgh on 2018/11/26.
 */
public interface IndexImgService extends IService<IndexImg> {

    /**
     * 根据id列表删除图片
     * @param ids
     */
	void deleteIndexImgByIds(Long[] ids);

    /**
     * 获取全部图片列表
     * @return
     */
    List<IndexImg> listIndexImg();

    /**
     * 删除图片缓存
     */
    void removeIndexImgCache();
}

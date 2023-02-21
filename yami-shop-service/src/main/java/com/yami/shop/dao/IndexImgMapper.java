/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.bean.model.IndexImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface IndexImgMapper extends BaseMapper<IndexImg> {

	/**
	 * 根据id列表删除图片
	 * @param ids
	 */
	void deleteIndexImgByIds(@Param("ids") Long[] ids);

	/**
	 * 获取图片列表
	 * @return
	 */
	List<IndexImg> listIndexImg();
}

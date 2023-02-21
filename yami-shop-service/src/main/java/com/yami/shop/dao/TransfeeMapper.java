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
import com.yami.shop.bean.model.Transfee;
import com.yami.shop.bean.model.TransfeeFree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 */
public interface TransfeeMapper extends BaseMapper<Transfee> {
	/**
	 * 插入运费金额
	 * @param transfees
	 */
	void insertTransfees(List<Transfee> transfees);

	/**
	 * 插入包邮运费项
	 * @param transfeeFrees
	 */
	void insertTransfeeFrees(List<TransfeeFree> transfeeFrees);

	/**
	 * 根据运费模板id删除
	 * @param transportId
	 */
	void deleteByTransportId(@Param("transportId") Long transportId);

	/**
	 * 根据运费模板id删除包邮运费
	 * @param transportId
	 */
	void deleteTransfeeFreesByTransportId(@Param("transportId") Long transportId);


}
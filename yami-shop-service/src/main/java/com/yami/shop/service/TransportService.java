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
import com.yami.shop.bean.model.Transport;

/**
 *
 * @author lgh on 2018/11/16.
 */
public interface TransportService extends IService<Transport> {
	/**
	 * 插入运费模板和运费项
	 * @param transport
	 */
	void insertTransportAndTransfee(Transport transport);

	/**
	 * 根据运费模板和运费项
	 * @param transport
	 */
	void updateTransportAndTransfee(Transport transport);

	/**
	 * 根据id列表删除运费模板和运费项
	 * @param ids
	 */
	void deleteTransportAndTransfeeAndTranscity(Long[] ids);

	/**
	 * 根据模板id获取运费模板和运费项
	 * @param transportId
	 * @return
	 */
	Transport getTransportAndAllItems(Long transportId);

	/**
	 * 删除缓存
	 * @param transportId
	 */
	void removeTransportAndAllItemsCache(Long transportId);

}

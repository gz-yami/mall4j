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
import com.yami.shop.bean.model.UserAddr;

/**
 * @author lanhai
 */
public interface UserAddrService extends IService<UserAddr> {
	/**
	 * 获取用户默认地址
	 * @param userId
	 * @return
	 */
	UserAddr getDefaultUserAddr(String userId);

	/**
	 * 更新默认地址
	 * @param addrId 默认地址id
	 * @param userId 用户id
	 */
	void updateDefaultUserAddr(Long addrId, String userId);

	/**
	 * 删除缓存
	 * @param addrId
	 * @param userId
	 */
    void removeUserAddrByUserId(Long addrId, String userId);

	/**
	 * 根据用户id和地址id获取用户地址
	 * @param addrId
	 * @param userId
	 * @return
	 */
    UserAddr getUserAddrByUserId(Long addrId, String userId);
}


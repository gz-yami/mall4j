/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.UserAddr;
import org.springframework.cache.annotation.CacheEvict;

public interface UserAddrService extends IService<UserAddr> {

	UserAddr getDefaultUserAddr(String userId);

	/**
	 * 更新默认地址
	 * @param addrId 默认地址id
	 * @param userId 用户id
	 */
	void updateDefaultUserAddr(Long addrId, String userId);


    void removeUserAddrByUserId(Long addrId, String userId);

    UserAddr getUserAddrByUserId(Long addrId, String userId);
}


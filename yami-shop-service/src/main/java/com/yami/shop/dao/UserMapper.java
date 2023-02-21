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
import com.yami.shop.bean.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author lanhai
 */
public interface UserMapper extends BaseMapper<User> {
	/**
	 * 根据用户邮箱获取用户信息
	 * @param userMail
	 * @return
	 */
	User getUserByUserMail(@Param("userMail") String userMail);

	/**
	 * 根据用户名称获取一个用户信息
	 * @param userName
	 * @return
	 */
	User selectOneByUserName(@Param("userName") String userName);
}

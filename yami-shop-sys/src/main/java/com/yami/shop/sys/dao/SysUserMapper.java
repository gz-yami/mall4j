/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.sys.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 根据用户id 批量删除用户
	 * @param userIds
	 */
	void deleteBatch(@Param("userIds") Long[] userIds, @Param("shopId") Long shopId);
	
	/**
	 * 根据用户名获取管理员用户
	 * @param username
	 * @return
	 */
	SysUser selectByUsername(String username);

}

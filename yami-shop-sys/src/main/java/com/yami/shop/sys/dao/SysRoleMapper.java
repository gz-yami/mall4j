/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.sys.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 * 角色管理
 */
public interface SysRoleMapper extends BaseMapper<SysRole>{
	/**
	 * 批量删除
	 * @param roleIds
	 */
	void deleteBatch(@Param("roleIds") Long[] roleIds);

	/**
	 * 根据用户id获取角色id列表
	 * @param userId
	 * @return
	 */
	List<Long> listRoleIdByUserId(Long userId);

}

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
import com.yami.shop.sys.model.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lanhai
 * 角色与菜单对应关系
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
	
	/**
	 * 根据角色ID数组，批量删除
	 * @param roleIds
	 * @return
	 */
	int deleteBatch(Long[] roleIds);

	/**
	 * 根据菜单id 删除菜单关联角色信息
	 * @param menuId
	 */
	void deleteByMenuId(Long menuId);

	/**
	 * 根据角色id 批量添加角色与菜单关系
	 * @param roleId
	 * @param menuIdList
	 */
	void insertRoleAndRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") List<Long> menuIdList);
}

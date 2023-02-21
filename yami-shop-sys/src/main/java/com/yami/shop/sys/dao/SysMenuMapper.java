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
import com.yami.shop.sys.model.SysMenu;

import java.util.List;


/**
 * 菜单管理
 * @author lgh
 */
public interface SysMenuMapper extends BaseMapper<SysMenu>{
	
	/**
	 * 根据角色id获取菜单列表
	 * @param roleId 角色id
	 * @return 菜单id列表
	 */
	List<Long> listMenuIdByRoleId(Long roleId);

	/**
	 * 查询用户的所有菜单ID
	 * @param userId 用户id
	 * @return 该用户所有可用的菜单
	 */
	List<SysMenu> listMenuByUserId(Long userId);
	
	/**
	 * 获取系统的所有菜单
	 * @return 系统的所有菜单
	 */
	List<SysMenu> listMenu();

	/**
	 * 获取简单的menu tree 用于在 ele-ui tree中显示，根据orderNum排序
	 * @return ztreeDto列表
	 */
	List<SysMenu> listSimpleMenuNoButton();

	/**
	 * 获取一级菜单
	 * @return 一级菜单列表
	 */
	List<SysMenu> listRootMenu();
	
	/**
	 * 根据一级菜单id 获取二级菜单
	 * @param parentId 一级菜单id
	 * @return 二级菜单列表
	 */
	List<SysMenu> listChildrenMenuByParentId(Long parentId);

	/**
	 * 获取菜单和按钮列表
	 * @return
	 */
	List<SysMenu> listMenuAndBtn();
}

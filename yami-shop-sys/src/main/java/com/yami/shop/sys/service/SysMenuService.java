/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.sys.model.SysMenu;

import java.util.List;


/**
 * 菜单管理
 * @author lgh
 */
public interface SysMenuService extends IService<SysMenu> {

	/**
	 * 获取用户菜单列表
	 * @param userId 用户id
	 * @return 菜单列表
	 */
	List<SysMenu> listMenuByUserId(Long userId);

	/**
	 * 删除 菜单，与角色菜单之间的关系
	 * @param menuId 菜单id
	 */
	void deleteMenuAndRoleMenu(Long menuId);
	
	/**
	 * 根据角色ID，获取菜单列表
	 * @param roleId 角色id
	 * @return 角色所拥有的菜单id列表
	 */
	List<Long> listMenuIdByRoleId(Long roleId);

	/**
	 * 获取简单的menu tree 用于在ele-ui tree中显示，根据orderNum排序
	 * @return 所有的菜单
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

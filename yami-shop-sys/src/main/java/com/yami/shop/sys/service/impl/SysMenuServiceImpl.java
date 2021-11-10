/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.sys.constant.Constant;
import com.yami.shop.sys.dao.SysMenuMapper;
import com.yami.shop.sys.dao.SysRoleMenuMapper;
import com.yami.shop.sys.model.SysMenu;
import com.yami.shop.sys.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lgh
 */
@Service("sysMenuService")
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuMapper sysRoleMenuMapper;

	private final SysMenuMapper sysMenuMapper;

	@Override
	public List<SysMenu> listMenuByUserId(Long userId) {
		// 用户的所有菜单信息
		List<SysMenu> sysMenus ;
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN_ID){
			sysMenus = sysMenuMapper.listMenu();
		}else {
			sysMenus = sysMenuMapper.listMenuByUserId(userId);
		}
		
		Map<Long, List<SysMenu>> sysMenuLevelMap = sysMenus.stream()
				.sorted(Comparator.comparing(SysMenu::getOrderNum))
				.collect(Collectors.groupingBy(SysMenu::getParentId));
		
		// 一级菜单
		List<SysMenu> rootMenu = sysMenuLevelMap.get(0L);
		if (CollectionUtil.isEmpty(rootMenu)) {
			return Collections.emptyList();
		}
		// 二级菜单
		for (SysMenu sysMenu : rootMenu) {
			sysMenu.setList(sysMenuLevelMap.get(sysMenu.getMenuId()));
		}
		return rootMenu;
	}

	@Override
	public void deleteMenuAndRoleMenu(Long menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuMapper.deleteByMenuId(menuId);
	}

	
	@Override
	public List<Long> listMenuIdByRoleId(Long roleId) {
		return sysMenuMapper.listMenuIdByRoleId(roleId);
	}


	@Override
	public List<SysMenu> listSimpleMenuNoButton() {
		return sysMenuMapper.listSimpleMenuNoButton();
	}

	@Override
	public List<SysMenu> listRootMenu() {
		return sysMenuMapper.listRootMenu();
	}

	@Override
	public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
		return sysMenuMapper.listChildrenMenuByParentId(parentId);
	}

	@Override
	public List<SysMenu> listMenuAndBtn() {
		return sysMenuMapper.listMenuAndBtn();
	}
	
}

/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yami.shop.sys.service.SysRoleService;
import com.yami.shop.sys.dao.SysRoleMapper;
import com.yami.shop.sys.dao.SysRoleMenuMapper;
import com.yami.shop.sys.dao.SysUserRoleMapper;
import com.yami.shop.sys.model.SysRole;


/**
 * 角色
 * @author lgh
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	@Autowired
	private SysRoleMenuMapper sysRoleMenuMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleAndRoleMenu(SysRole role) {
		role.setCreateTime(new Date());
		this.save(role);
		if (CollectionUtils.isEmpty(role.getMenuIdList())) {
			return;
		}
		
		
		//保存角色与菜单关系
		sysRoleMenuMapper.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateRoleAndRoleMenu(SysRole role) {
		// 更新角色
		sysRoleMapper.updateById(role);
		//先删除角色与菜单关系
		sysRoleMenuMapper.deleteBatch(new Long[]{role.getRoleId()});
		if (CollectionUtils.isEmpty(role.getMenuIdList())) {
			return;
		}
		//保存角色与菜单关系
		sysRoleMenuMapper.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		sysRoleMapper.deleteBatch(roleIds);

		//删除角色与菜单关联
		sysRoleMenuMapper.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleMapper.deleteBatch(roleIds);
	}
	@Override
	public List<Long> listRoleIdByUserId(Long userId) {
		return sysRoleMapper.listRoleIdByUserId(userId);
	}

}

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
import com.yami.shop.sys.dao.SysRoleMapper;
import com.yami.shop.sys.dao.SysRoleMenuMapper;
import com.yami.shop.sys.dao.SysUserRoleMapper;
import com.yami.shop.sys.model.SysRole;
import com.yami.shop.sys.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 角色
 * @author lgh
 */
@Service("sysRoleService")
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	private final SysRoleMenuMapper sysRoleMenuMapper;
	private final SysUserRoleMapper sysUserRoleMapper;
	private final SysRoleMapper sysRoleMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleAndRoleMenu(SysRole role) {
		role.setCreateTime(new Date());
		this.save(role);
		if (CollectionUtil.isEmpty(role.getMenuIdList())) {
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
		if (CollectionUtil.isEmpty(role.getMenuIdList())) {
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

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


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.sys.dao.SysUserMapper;
import com.yami.shop.sys.dao.SysUserRoleMapper;
import com.yami.shop.sys.model.SysUser;
import com.yami.shop.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 系统用户
 * @author lgh
 */
@Service("sysUserService")
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private SysUserRoleMapper sysUserRoleMapper;

	private SysUserMapper sysUserMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUserAndUserRole(SysUser user) {
		user.setCreateTime(new Date());
		sysUserMapper.insert(user);
		if(CollUtil.isEmpty(user.getRoleIdList())){
			return ;
		}
		//保存用户与角色关系
		sysUserRoleMapper.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserAndUserRole(SysUser user) {
		// 更新用户
		sysUserMapper.updateById(user);

		//先删除用户与角色关系
		sysUserRoleMapper.deleteByUserId(user.getUserId());

		if(CollUtil.isEmpty(user.getRoleIdList())){
			return ;
		}
		//保存用户与角色关系
		sysUserRoleMapper.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void updatePasswordByUserId(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setPassword(newPassword);
        user.setUserId(userId);
        sysUserMapper.updateById(user);
    }

	@Override
	public void deleteBatch(Long[] userIds,Long shopId) {
		sysUserMapper.deleteBatch(userIds,shopId);
	}

	@Override
	public SysUser getByUserName(String username) {
		return sysUserMapper.selectByUsername(username);
	}

	@Override
	public SysUser getSysUserById(Long userId) {
		return sysUserMapper.selectById(userId);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserMapper.queryAllPerms(userId);
	}
}

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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yami.shop.sys.service.SysUserService;
import com.yami.shop.sys.dao.SysUserMapper;
import com.yami.shop.sys.dao.SysUserRoleMapper;
import com.yami.shop.sys.model.SysUser;

import cn.hutool.core.collection.CollUtil;


/**
 * 系统用户
 * @author lgh
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
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
	@CacheEvict(cacheNames="sys_user",key="#user.userId")
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
	@Cacheable(cacheNames="sys_user",key="#userId")
	public SysUser getSysUserById(Long userId) {
		return sysUserMapper.selectById(userId);
	}

	@Override
	@CacheEvict(cacheNames="sys_user",key="#userId")
	public void evictSysUserById(Long userId) {

	}
}

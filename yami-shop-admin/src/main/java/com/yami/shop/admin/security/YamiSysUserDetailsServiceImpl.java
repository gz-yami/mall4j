/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.security;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.common.util.CacheManagerUtil;
import com.yami.shop.sys.constant.Constant;
import com.yami.shop.security.enums.App;
import com.yami.shop.security.exception.UsernameNotFoundExceptionBase;
import com.yami.shop.security.model.AppConnect;
import com.yami.shop.security.service.YamiSysUser;
import com.yami.shop.security.service.YamiUser;
import com.yami.shop.security.service.YamiUserDetailsService;
import com.yami.shop.sys.dao.SysMenuMapper;
import com.yami.shop.sys.dao.SysUserMapper;
import com.yami.shop.sys.model.SysMenu;
import com.yami.shop.sys.model.SysUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详细信息
 *
 * @author
 */
@Slf4j
@Service
@AllArgsConstructor
public class YamiSysUserDetailsServiceImpl implements YamiUserDetailsService {
	private final SysMenuMapper sysMenuMapper;
	private final SysUserMapper sysUserMapper;
	private final CacheManagerUtil cacheManagerUtil;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundExceptionBase
	 */
	@Override
	@SneakyThrows
	public YamiSysUser loadUserByUsername(String username) {
		YamiSysUser userDetails = cacheManagerUtil.getCache("yami_sys_user", username);
		if (userDetails != null) {
			return userDetails;
		}

		userDetails = getUserDetails(username);
		cacheManagerUtil.putCache("yami_sys_user",username, userDetails);
		return userDetails;
	}


	/**
	 * 构建userdetails
	 *
	 * @param username 用户名称
	 * @return
	 */
	private YamiSysUser getUserDetails(String username) {
		SysUser sysUser = sysUserMapper.selectByUsername(username);

		if (sysUser == null) {
			throw new UsernameNotFoundExceptionBase("用户不存在");
		}

		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(getUserPermissions(sysUser.getUserId()).toArray(new String[0]));
		// 构造security用户
		return new YamiSysUser(sysUser.getUserId(), sysUser.getShopId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getStatus() == 1,
				true, true, true , authorities);
	}

	private Set<String> getUserPermissions(Long userId) {
		List<String> permsList;

		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN_ID){
			List<SysMenu> menuList = sysMenuMapper.selectList(Wrappers.emptyWrapper());


			permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
		}else{
			permsList = sysUserMapper.queryAllPerms(userId);
		}


		Set<String> permsSet = permsList.stream().flatMap((perms)->{
					if (StrUtil.isBlank(perms)) {
						return null;
					}
					return Arrays.stream(perms.trim().split(","));
				}
		).collect(Collectors.toSet());
		return permsSet;
	}

	@Override
	public YamiUser loadUserByAppIdAndBizUserId(App app, String bizUserId) {
		return null;
	}

	@Override
	public void insertUserIfNecessary(AppConnect appConnect) {

	}
}

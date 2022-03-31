/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.sys.controller;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.sys.constant.Constant;
import com.yami.shop.sys.dto.UpdatePasswordDto;
import com.yami.shop.sys.model.SysUser;
import com.yami.shop.sys.service.SysRoleService;
import com.yami.shop.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 系统用户
 * @author lgh
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PasswordManager passwordManager;
	@Autowired
	private TokenStore tokenStore;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('sys:user:page')")
	public ResponseEntity<IPage<SysUser>> page(String username,PageParam<SysUser> page){
		IPage<SysUser> sysUserPage = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
				.eq(SysUser::getShopId, SecurityUtils.getSysUser().getShopId())
				.like(StrUtil.isNotBlank(username), SysUser::getUsername, username));
		return ResponseEntity.ok(sysUserPage);
	}

	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public ResponseEntity<SysUser> info(){
		return ResponseEntity.ok(sysUserService.getSysUserById(SecurityUtils.getSysUser().getUserId()));
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	@ApiOperation(value="修改密码", notes="修改当前登陆用户的密码")
	public ResponseEntity<String> password(@RequestBody @Valid UpdatePasswordDto param){
		Long userId = SecurityUtils.getSysUser().getUserId();

        // 开源版代码，禁止用户修改admin 的账号密码
        // 正式使用时，删除此部分代码即可
        if (Objects.equals(1L,userId) && StrUtil.isNotBlank(param.getNewPassword())) {
            throw new YamiShopBindException("禁止修改admin的账号密码");
        }
		SysUser dbUser = sysUserService.getSysUserById(userId);
		String password = passwordManager.decryptPassword(param.getPassword());
		if (!passwordEncoder.matches(password, dbUser.getPassword())) {
			return ResponseEntity.badRequest().body("原密码不正确");
		}
		//新密码
		String newPassword = passwordEncoder.encode(passwordManager.decryptPassword(param.getNewPassword()));
//		更新密码
		sysUserService.updatePasswordByUserId(userId, newPassword);
		tokenStore.deleteAllToken(String.valueOf(SysTypeEnum.ADMIN.value()),String.valueOf(userId));
		return ResponseEntity.ok().build();
	}

	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@PreAuthorize("@pms.hasPermission('sys:user:info')")
	public ResponseEntity<SysUser> info(@PathVariable("userId") Long userId){
		SysUser user = sysUserService.getSysUserById(userId);
		user.setUserId(null);
		if (!Objects.equals(user.getShopId(), SecurityUtils.getSysUser().getShopId())) {
			throw new YamiShopBindException("没有权限获取该用户信息");
		}
		//获取用户所属的角色列表
		List<Long> roleIdList = sysRoleService.listRoleIdByUserId(userId);
		user.setRoleIdList(roleIdList);
		return ResponseEntity.ok(user);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys:user:save')")
	public ResponseEntity<String> save(@Valid @RequestBody SysUser user){
		String username = user.getUsername();
		SysUser dbUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
				.eq(SysUser::getUsername, username));
		if (dbUser!=null) {
			return ResponseEntity.badRequest().body("该用户已存在");
		}
		user.setShopId(SecurityUtils.getSysUser().getShopId());
		user.setPassword(passwordEncoder.encode(passwordManager.decryptPassword(user.getPassword())));
		sysUserService.saveUserAndUserRole(user);
		return ResponseEntity.ok().build();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys:user:update')")
	public ResponseEntity<String> update(@Valid @RequestBody SysUser user){
		String password = passwordManager.decryptPassword(user.getPassword());
		SysUser dbUser = sysUserService.getSysUserById(user.getUserId());

		if (!Objects.equals(dbUser.getShopId(), SecurityUtils.getSysUser().getShopId())) {
			throw new YamiShopBindException("没有权限修改该用户信息");
		}
		SysUser dbUserNameInfo = sysUserService.getByUserName(user.getUsername());

		if (dbUserNameInfo != null && !Objects.equals(dbUserNameInfo.getUserId(),user.getUserId())) {
			return ResponseEntity.badRequest().body("该用户已存在");
		}
		if (StrUtil.isBlank(password)) {
			user.setPassword(null);
		}else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		// 开源版代码，禁止用户修改admin 的账号密码密码
		// 正式使用时，删除此部分代码即可
		boolean is = Objects.equals(1L,dbUser.getUserId()) && (StrUtil.isNotBlank(password) || !StrUtil.equals("admin",user.getUsername()));
		if (is) {
			throw new YamiShopBindException("禁止修改admin的账号密码");
		}

		if (Objects.equals(1L,user.getUserId()) && user.getStatus()==0) {
			throw new YamiShopBindException("admin用户不可以被禁用");
		}
		sysUserService.updateUserAndUserRole(user);
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('sys:user:delete')")
	public ResponseEntity<String> delete(@RequestBody Long[] userIds){
		if (userIds.length == 0) {
			return ResponseEntity.badRequest().body("请选择需要删除的用户");
		}
		if(ArrayUtil.contains(userIds, Constant.SUPER_ADMIN_ID)){
			return ResponseEntity.badRequest().body("系统管理员不能删除");
		}
		if(ArrayUtil.contains(userIds, SecurityUtils.getSysUser().getUserId())){
			return ResponseEntity.badRequest().body("当前用户不能删除");
		}
		sysUserService.deleteBatch(userIds,SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}
}

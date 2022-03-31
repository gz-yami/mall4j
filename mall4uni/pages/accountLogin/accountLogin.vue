<template>
	<view class="con">
		<image src="../../static/logo.png"></image>
		<!-- 登录 -->
		<view class="login-form">
			<view :class="['item',errorTips==1? 'error':'']">
				<view class="account">
					<text class="input-item">账号</text>
					<input type="text" @input="getInputVal" data-type="account" placeholder-class="inp-palcehoder" placeholder="请输入用户名"></input>
				</view>
				<view class="error-text" v-if="errorTips==1"><text class="warning-icon">!</text>请输入账号！</view>
			</view>
			<view :class="['item',errorTips==2? 'error':'']">
				<view class="account">
					<text class="input-item">密码</text>
					<input type="password" @input="getInputVal" data-type="password" placeholder-class="inp-palcehoder" placeholder="请输入密码"></input>
				</view>
				<view class="error-text" v-if="errorTips==2"><text class="warning-icon">!</text>请输入密码！</view>
			</view>
			<view class="operate">
				<view class="to-register" @tap="toRegitser">还没有账号？<text>去注册></text></view>
			</view>
		</view>

		<view>
			<button class="authorized-btn" @tap="login">登录</button>
			<button class="to-idx-btn" @tap="toIndex">回到首页</button>
		</view>

	</view>
</template>

<script>
	var http = require("../../utils/http");
	var util = require('../../utils/util.js');
	import { encrypt } from '../../utils/crypto.js'
	import {
		AppType
	} from '../../utils/constant.js'
	export default {
		data() {
			return {
				principal: '', // 账号
				credentials: '', // 密码
				// isPersonalCenter: false, //是否从个人中心页面跳转过来
				errorTips: 0, //错误提示

			};
		},

		components: {},
		props: {},

		computed: {},

		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {
			if (options.isPersonalCenter) {
				this.isPersonalCenter = options.isPersonalCenter
			}
		},

		/**
		 * 生命周期函数--监听页面初次渲染完成
		 */
		onReady: function() {},

		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			//头部导航标题
			uni.setNavigationBarTitle({
				title: "用户登录"
			});
		},

		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide: function() {},

		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload: function() {},

		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {},

		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function() {},

		methods: {
			/**
			 * 输入框的值
			 */
			getInputVal: function(e) {
				const type = e.currentTarget.dataset.type;
				if (type == 'account') {
					this.setData({
						principal: e.detail.value
					});
				} else if (type == 'password') {
					this.setData({
						credentials: e.detail.value
					});
				}
			},

			/**
			 * 登录
			 */
			login() {
				// // #ifdef H5
				// var ua = navigator.userAgent.toLowerCase();
				// var data = {
				// 	appType: ua.search(/MicroMessenger/i) > -1 ? AppType.MP : AppType.H5,
				// 	principal: ua.search(/MicroMessenger/i) > -1 ? this.principal + ':' + util.getUrlKey('code') : this.principal,
				// 	credentials: this.credentials,
				// 	loginType: 0, //账号登录
				// }
				// // #endif
				// // #ifdef APP-PLUS
				// var data = {
				// 	appType: uni.getStorageSync('appType'),
				// 	principal: this.principal,
				// 	credentials: this.credentials,
				// 	loginType: 0, //账号登录
				// }
				// // #endif

				if (this.principal.length == 0) {
					this.setData({
						errorTips: 1
					})
					return
				} else if (this.credentials.length == 0) {
					this.setData({
						errorTips: 2
					})
					return
				} else {
					this.setData({
						errorTips: 0
					})
					// #ifdef H5 || APP-PLUS
					var params = {
						url: "/login",
						method: "post",
						data: {
							"userName": this.principal,
							"passWord": encrypt(this.credentials)
						},
						callBack: res => {
							http.loginSuccess(res, () => {
								uni.showToast({
									title: "登录成功",
									icon: 'none',
									complete: () => {
										setTimeout(() => {
											wx.switchTab({
											url: '/pages/index/index'
										});
										},1000)

									}
								})
							})
						},
					}
					http.request(params)
					// #endif
					// #ifdef MP-WEIXIN
					// wx.login({
					// 	success: (res) => {
					// 		var params = {
					// 			url: "/login",
					// 			method: "post",
					// 			data: {
					// 				appType: 1,
					// 				credentials: this.credentials,
					// 				loginType: 0,
					// 				principal: this.principal + ':' + res.code
					// 			},
					// 			callBack: result => {
					// 				http.loginSuccess(result)
					// 				this.$Router.pushTab('/pages/index/index')
					// 			},
					// 		}
					// 		http.request(params)
					// 	},
					// })
					// #endif
				}
			},

			/**
			 * 去注册
			 */
			toRegitser() {
				uni.navigateTo({
					url: "/pages/register/register"
				})
			},

			/**
			 * 回到首页
			 */
			toIndex() {
				wx.switchTab({
					url: '/pages/index/index'
				});
				// this.$Router.pushTab('/pages/index/index')
			}

		},
		watch: {
			principal(nv, ov) {
				this.errorTips = 0
			}
		}
	};
</script>
<style>
	@import "./accountLogin.css";
</style>

<template>
	<!--pages/register/register.wxml-->

	<view class="register">

		<view class="con">
			<image src="../../static/logo.png"></image>
			<!-- 登录 -->
			<view class="login-form">
				<view :class="['item',errorTips==1? 'error':'']">
					<view class="account">
						<text class="input-item">账号</text>
						<input type="text" @input="getInputVal" data-type="account" placeholder-class="inp-palcehoder" placeholder="请输入账号名称"></input>
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
					<view class="to-register" @tap="toLogin">已有账号？<text>去登录></text></view>
				</view>
			</view>

			<view>
				<button class="authorized-btn" @tap="toRegister">注册</button>
				<button class="to-idx-btn" @tap="toIndex">回到首页</button>
			</view>

		</view>
	</view>
</template>

<script>
	// pages/register/register.js
	var http = require("../../utils/http");
	var util = require("../../utils/util.js");
	import { encrypt } from '../../utils/crypto.js'

	export default {
		data() {
			return {
				errorTips: 0, // 输入错误提示:  1账号输入  2密码输入
				principal: '', // 账号
				credentials: '', // 密码

			};
		},

		components: {},
		props: {},

		computed: {},

		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {},

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
				title: "用户注册"
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
			 * 注册
			 */
			toRegister() {

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
					uni.showLoading();
					var params = {
						url: "/user/register",
						method: "post",
						data: {
							// appType: 1,
							// 应用类型 1小程序 2微信公众号 3 PC 4 H5
							userName: this.principal,
							passWord: encrypt(this.credentials),
						},
						callBack: res => {
							uni.hideLoading();
							uni.showToast({
								title: "注册成功，请登录",
								icon: "none",
								duration: 1500
							})
							setTimeout(() => {
								uni.navigateTo({
									url: "/pages/accountLogin/accountLogin"
								})
							}, 1800)
							// wx.setStorageSync('loginResult', res);
							// wx.setStorageSync('token', 'bearer' + res.access_token);
						}
					};
					http.request(params);
				}
			},
			/**
			 * 去登陆
			 */
			toLogin: function() {
				uni.navigateTo({
					url: "/pages/accountLogin/accountLogin"
				});
			},

			/**
			 * 回到首页
			 */
			toIndex: function() {
				uni.switchTab({
					url: '/pages/index/index'
				});
				// this.$Router.pushTab('/pages/index/index')
			}
		}
	}
</script>
<style>
	@import "./register.css";
</style>

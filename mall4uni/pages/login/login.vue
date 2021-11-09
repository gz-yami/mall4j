<template>
<view>
<!--pages/login/login.wxml-->
<image src="http://img-test.gz-yami.com/mini/logo.jpg" class="c-logo"></image>
<view class="title">mall4j</view>
<view class="msg">申请获得你的公开信息（昵称、头像等）</view>
<button color="#eb2444" open-type="getUserInfo" lang="zh_CN" @getuserinfo="onGotUserInfo" class>微信授权</button>
</view>
</template>

<script>
var http = require("../../utils/http.js");

export default {
  data() {
    return {};
  },

  components: {},
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {},
  methods: {
    onGotUserInfo: function (res) {
      http.updateUserInfo();
      uni.navigateBack({
        delta: 1
      });
    }
  }
};
</script>
<style>
@import "./login.css";
</style>
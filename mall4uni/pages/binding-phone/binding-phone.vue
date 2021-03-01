<template>
<!--pages/binding-phone/binding-phone.wxml-->
<view class="container">
  <view class="binding-phone">
    <!-- <block wx:for='{{couponList}}' wx:key=''> -->
      <view class="item">
        <text class="item-tip">手机号码：</text>
        <input placeholder="输入手机号码" type="number" maxlength="11" :value="phonenum" @input="onPhoneInput"></input>
      </view>
      <view class="item ">
        <text class="item-tip">验证码：</text>
        <input placeholder="输入验证码" type="number" :value="code" @input="onCodeInput"></input>
        <text class="get-code gray" @tap="getCodeNumber">获取验证码</text>
      </view>
    <!-- </block> -->
  </view>

  <view class="btn-box">
    <text class="sure-btn gray">确定</text>
    <text class="sure-btn ">确定</text>
  </view>
</view>
</template>

<script>
// pages/binding-phone/binding-phone.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");

export default {
  data() {
    return {
      phonenum: '',
      code: ''
    };
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
    getCodeNumber: function () {
      if (this.phoneNumber == "") {
        uni.showToast({
          title: '请输入手机号',
          icon: "none"
        });
        return;
      }

      var params = {
        url: "/p/sms/send",
        method: "POST",
        data: {// phonenum: this.data.phonenum,
          // code: this.data.code
        },
        callBack: res => {
          this.setData({
            phonenum: this.phonenum,
            code: this.code
          });
        }
      };
      http.request(params);
    },
    onPhoneInput: function (e) {
      this.setData({
        phonenum: e.detail.value
      });
    },
    onCodeInput: function (e) {
      this.setData({
        code: e.detail.value
      });
    }
  }
};
</script>
<style>
@import "./binding-phone.css";
</style>
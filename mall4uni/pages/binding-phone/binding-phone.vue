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
        <text v-if="show" class="get-code gray" @tap="getCodeNumber">获取验证码</text>
        <text v-if="!show" class="get-code gray">{{count}} s</text>
      </view>
    <!-- </block> -->
  </view>

  <view class="btn-box">
    <text v-if="phonenum && code" class="sure-btn" @click="bindMobile">确定</text>
    <text v-else class="sure-btn gray">确定</text>
  </view>
</view>
</template>

<script>
// pages/binding-phone/binding-phone.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
import {
  AppType
} from "../../utils/constant.js";
export default {
  data() {
    return {
      phonenum: '',
      code: '',
      show: true,
      count: '',
      timer: null
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
      if (!this.phonenum) {
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
          mobile: this.phonenum
        },
        callBack: res => {
          const timeCount = 60;
          if (!this.timer) {
            this.count = timeCount
            this.show = false;
            this.timer = setInterval(() => {
              if (this.count > 0 && this.count <= timeCount) {
                this.count--;
              } else {
                clearInterval(this.timer);
                this.timer = null,
                this.show = true
              }
            }, 1000)
          }
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
    },

    /**
     * 绑定
     */
    bindMobile() {
      var params = {
        url: '/user/registerOrBindUser',
        method: 'PUT',
        data: {
          appType: AppType.MP,
          mobile: this.phonenum,
          validCode: this.code,
          validateType: 1, // 验证类型:1验证码验证 ,
          registerOrBind: 2 // 验证类型 1注册 2绑定
        },
        callBack: res => {
          http.loginSuccess(res)
          uni.navigateTo({
            url: '/pages/index/index'
          });
        },
      }
      http.request(params)
    }
  }
};
</script>
<style>
@import "./binding-phone.css";
</style>
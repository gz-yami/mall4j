// pages/binding-phone/binding-phone.js

var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    phonenum:'',
    code:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  getCodeNumber:function(){
    if (!this.data.phonenum) {
      wx.showToast({
        title: '请输入手机号',
        icon: "none"
      })
      return;
    }
    var params = {
      url: "/p/sms/send",
      method: "POST",
      data: {
        // phonenum: this.data.phonenum,
        // code: this.data.code
        mobile: this.data.phonenum
      },
      callBack: (res) => {}
    };
    http.request(params);
  },
  onPhoneInput:function(e){
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
        appType: 1, // 微信小程序
        mobile: this.data.phonenum,
        validCode: this.data.code,
        validateType: 1, // 验证类型:1验证码验证 ,
        registerOrBind: 2 // 验证类型 1注册 2绑定
      },
      callBack: res => {
        uni.navigateTo({
          url: '/pages/index/index'
        });
      },
    }
    http.request(params)
  }
})

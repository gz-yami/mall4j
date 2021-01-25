//app.js
var http = require("utils/http.js");
App({
  onLaunch: function () {
    http.getToken();
    wx.getSetting({
      success(res) {
        if (!res.authSetting['scope.userInfo']) {
          wx.navigateTo({
            url: '/pages/login/login',
          })
        }
      }
    })
  },
  globalData: {
    // 定义全局请求队列
    requestQueue: [],
    // 是否正在进行登陆
    isLanding: true,
    // 购物车商品数量
    totalCartCount: 0
  }
})
// pages/pay-result/pay-result.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sts: 0,
    orderNumbers: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      sts: options.sts,
      orderNumbers: options.orderNumbers
    });
  },
  toOrderList: function () {
    wx.navigateTo({
      url: '/pages/orderList/orderList?sts=0'
    })
  },
  toIndex: function () {
    wx.switchTab({
      url: '/pages/index/index'
    })
  },
  payAgain: function () {
    wx.showLoading({
      mask: true
    });
    var params = {
      url: "/p/order/pay",
      method: "POST",
      data: {
        payType: 1,
        orderNumbers: this.data.orderNumbers
      },
      callBack: function (res) {
        //console.log(res);
        wx.hideLoading();
        wx.requestPayment({
          timeStamp: res.timeStamp,
          nonceStr: res.nonceStr,
          package: res.packageValue,
          signType: res.signType,
          paySign: res.paySign,
          success: e => {
            //console.log("支付成功");
            wx.redirectTo({
              url: '/pages/pay-result/pay-result?sts=1&orderNum=' + orderNumbers + "&orderType=" + this.data.orderType,
            })
          },
          fail: err => {
            
          }
        })

      }
    };
    http.request(params);
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

  }
})
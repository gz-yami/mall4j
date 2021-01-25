// pages/order-detail/order-detail.js

var http = require('../../utils/http.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderItemDtos: [],
    remarks: "",
    actualTotal: 0,
    userAddrDto: null,
    orderNumber: "",
    createTime: "",
    status: 0,
    productTotalAmount: '',
    transfee: '',
    reduceAmount: '',
    actualTotal: '',
    prodid: ''
  },

  //跳转商品详情页
  toProdPage: function(e) {
    var prodid = e.currentTarget.dataset.prodid;
    wx.navigateTo({
      url: '/pages/prod/prod?prodid=' + prodid,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.loadOrderDetail(options.orderNum);
  },

  /**
   * 加载订单数据
   */
  loadOrderDetail: function(orderNum) {
    var ths = this;
    wx.showLoading();
    //加载订单详情
    var params = {
      url: "/p/myOrder/orderDetail",
      method: "GET",
      data: {
        orderNumber: orderNum
      },
      callBack: function(res) {
        ths.setData({
          orderNumber: orderNum,
          actualTotal: res.actualTotal,
          userAddrDto: res.userAddrDto,
          remarks: res.remarks,
          orderItemDtos: res.orderItemDtos,
          createTime: res.createTime,
          status: res.status,
          productTotalAmount: res.orderItemDtos[0].productTotalAmount,
          transfee: res.transfee,
          reduceAmount: res.reduceAmount,
          actualTotal: res.actualTotal,
        });
        wx.hideLoading();
      }
    };
    http.request(params);

  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },


  // 一键复制事件
  copyBtn: function(e) {
    var ths = this;
    wx.setClipboardData({
      //准备复制的数据
      data: ths.data.orderNumber,
      success: function(res) {
        wx.showToast({
          title: '复制成功',
        });
      }
    })
  },
})
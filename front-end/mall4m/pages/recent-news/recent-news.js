// pages/recent-news/recent-news.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    news:[],
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
    var ths = this;
    //加载公告
    var params = {
      url: "/shop/notice/noticeList",
      method: "GET",
      data: {},
      callBack: function (res) {
        // console.log(res);
        ths.setData({
          news: res.records,
        });
      }
    };
    http.request(params);
  },

  // 跳转公告详情页
  toNewsDetail:function(e){
    console.log(e)
    var id = e.currentTarget.dataset.id
    // console.log(id)
    wx.navigateTo({
      url: '/pages/news-detail/news-detail?id='+e.currentTarget.dataset.id,
    })
    // console.log(id)
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
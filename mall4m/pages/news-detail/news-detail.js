// pages/news-detail/news-detail.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    news: {
      title: '',
      content: '',
      id: null
    }
  },



  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // var ths = this;
    //加载公告详情
    
    var params = {
      // `/shop/notice/info/${options.id}`
      url: '/shop/notice/info/' + options.id,
      method: "GET",
      // data: {
      //   id: id,
      // },
      // callBack: function(news){
      callBack: res => {
        res.content = res.content.replace(/width=/gi, 'sss=');
        res.content = res.content.replace(/height=/gi, 'sss=');
        res.content = res.content.replace(/ \/\>/gi, ' style="max-width:100% !important;display:block;" \/\>');
        this.setData({
          news: res
        });
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
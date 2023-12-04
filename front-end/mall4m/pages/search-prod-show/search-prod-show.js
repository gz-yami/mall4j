// pages/search-prod-show/search-prod-show.js

var http = require('../../utils/http.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sts: 0,
    showType:2,
    searchProdList:[],
    prodName:"",
  },

  changeShowType:function(){
    var showType = this.data.showType;
    if (showType==1){
      showType=2;
    }else{
      showType = 1;
    }
    this.setData({
      showType: showType
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      prodName: options.prodName
    });
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  //输入商品获取数据
  getSearchContent: function (e) {
    this.setData({
      prodName: e.detail.value
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.toLoadData();
  },

//请求商品接口
  toLoadData:function(){
    var ths = this;
    //热门搜索
    var params = {
      url: "/search/searchProdPage",
      method: "GET",
      data: {
        current: 1,
        prodName: this.data.prodName,
        size: 10,
        sort: this.data.sts
      },
      callBack: function (res) {
        ths.setData({
          searchProdList: res.records,
        });
      },
    };
    http.request(params);
  },

//当前搜索页二次搜索商品
  toSearchConfirm:function(){
    this.toLoadData();
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

    /**
   * 状态点击事件
   */
  onStsTap: function(e) {
    var sts = e.currentTarget.dataset.sts;
    this.setData({
      sts: sts
    });
    this.toLoadData();
  },

  toProdPage: function (e) {
    var prodid = e.currentTarget.dataset.prodid;
    wx.navigateTo({
      url: '/pages/prod/prod?prodid=' + prodid,
    })
  },
})
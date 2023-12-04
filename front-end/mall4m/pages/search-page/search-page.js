// pages/search-page/search-page.js

var http = require('../../utils/http.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    hotSearchList: [],
    prodName:"",
    recentSearch: [],
    
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
    //热门搜索
    var params = {
      url: "/search/hotSearchByShopId",
      method: "GET",
      data: {
        number:10,
        shopId:1,
        sort:1,
      },
   
      callBack: function (res) {
        ths.setData({
        hotSearchList:res,
        });
      },
    };
    http.request(params);

    // 获取历史搜索
    this.getRecentSearch();
  },



  /**
    * 获取历史搜索
    */
  getRecentSearch: function () {
    let recentSearch = wx.getStorageSync('recentSearch');
    this.setData({ 
      recentSearch
    });
  },

    /**
     * 搜索提交
     */
  toSearchProdPage: function () {

      if (this.data.prodName.trim()) {
      // 记录最近搜索
        let recentSearch = wx.getStorageSync('recentSearch') || [];
        recentSearch = recentSearch.filter(item => item !== this.data.prodName)
        recentSearch.unshift(this.data.prodName);
        if (recentSearch.length>10){
          recentSearch.pop();
        }
        wx.setStorageSync('recentSearch', recentSearch);

      // 跳转到商品列表页
      wx.navigateTo({
        url: '/pages/search-prod-show/search-prod-show?prodName=' + this.data.prodName,
      })
    }
  },

    /**
     * 清空搜索历史
     */
    clearSearch: function () {
    wx.removeStorageSync('recentSearch');
    this.getRecentSearch();
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

 // 返回首页
  goBackIndex:function(){
    wx.navigateBack({
      // url: '/pages/search-page/search-page',
    })
  },



//输入商品名获取数据 || 绑定输入值
  getSearchContent:function(e){
    this.setData({
      prodName: e.detail.value
    })
    // this.data.prodName=e.detail.value
  },

  //点击搜素历史
  onHistSearch:function(e){
    var name = e.currentTarget.dataset.name;
    this.setData({
      prodName: name
    });
    this.toSearchProdPage();
  }
})
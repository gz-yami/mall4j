// pages/delivery-address/delivery-address.js

var http = require("../../utils/http.js");
// var config = require("../../utils/config.js");

Page({
  data: {
    defaultSize: 'mini',
    disabled: false,
    plain: true,
    loading: false,
    addressList: [],
    addAddress: '',
    order: -1
  },

  onLoad: function (option) {
    if (option.order) {
      this.setData({
        order: option.order
      });
    }
  },

  //新增收货地址
  onAddAddr: function (e) {
    wx.navigateTo({
      url: '/pages/editAddress/editAddress',
    })
  },

  //设置为默认地址
  onDefaultAddr: function (e) {
    var addrId = e.currentTarget.dataset.addrid;
    console.log(addrId)
    var ths = this;
    wx.showLoading();
    var params = {
      url: "/p/address/defaultAddr/" + addrId,
      method: "PUT",
      data: {
        addrId:addrId
         },
      callBack: function (res) {
        wx.hideLoading();

      }
    }
    http.request(params);
  },

  //加载地址列表
  onShow: function () {
    var ths = this;
    wx.showLoading();


      var params = {
        url: "/p/address/list",
        method: "GET",
        data: {},
        callBack: function (res) {
          //console.log(res)
          ths.setData({
            addressList: res
          });
          wx.hideLoading();
        }
      }
    
    http.request(params);
  },

// 修改地址 
  toEditAddress: function (e) {
    var addrId = e.currentTarget.dataset.addrid;
    wx.navigateTo({
      url: '/pages/editAddress/editAddress?addrId=' + addrId,
    })
  },

  /**
   * 选择地址 跳转回提交订单页
   */
  selAddrToOrder: function (e) {
    if (this.data.order == 0) {
      var pages = getCurrentPages();//当前页面
      var prevPage = pages[pages.length - 2];//上一页面
      prevPage.setData({//直接给上移页面赋值
        item: e.currentTarget.dataset.item,
        selAddress: 'yes'
      });
      wx.navigateBack({//返回
        delta: 1
      })
    }
  }
})
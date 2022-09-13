//index.js
//获取应用实例
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
const app = getApp()

Page({
  data: {
    indicatorDots: true,
    indicatorColor: '#d1e5fb',
    indicatorActiveColor: '#1b7dec',
    autoplay: true,
    interval: 2000,
    duration: 1000,
    indexImgs: [],
    seq: 0,
    news: [],
    taglist: [],
    sts: 0,
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function() {
    this.getAllData();
  },

  // 页面滚动到指定位置指定元素固定在顶部
  onPageScroll: function(e) { //监听页面滚动
    this.setData({
      scrollTop: e.scrollTop
    })
  },

  toProdPage: function(e) {
    var prodid = e.currentTarget.dataset.prodid;
    if (prodid) {
      wx.navigateTo({
        url: '/pages/prod/prod?prodid=' + prodid,
      })
    }
  },

  toCouponCenter: function() {
    wx.showToast({
      icon:"none",
      title: '该功能未开源'
    })
  },

  // 跳转搜索页
  toSearchPage: function() {
    wx.navigateTo({
      url: '/pages/search-page/search-page',
    })
  },

  //跳转商品活动页面
  toClassifyPage: function(e) {
    var url = '/pages/prod-classify/prod-classify?sts=' + e.currentTarget.dataset.sts;
    var id = e.currentTarget.dataset.id;
    var title = e.currentTarget.dataset.title;
    if (id) {
      url += "&tagid=" + id + "&title=" + title;
    }
    wx.navigateTo({
      url: url
    })
  },

  //跳转限时特惠页面
  toLimitedTimeOffer: function(e) {
    wx.showToast({
      icon:"none",
      title: '该功能未开源'
    })
  },

  //跳转公告列表页面
  onNewsPage: function() {
    wx.navigateTo({
      url: '/pages/recent-news/recent-news',
    })
  },

  onShow: function() {
  },
  getAllData() {
    http.getCartCount(); //重新计算购物车总数量
    this.getIndexImgs();
    this.getNoticeList();
    this.getTag();
  },
  //加载轮播图
  getIndexImgs() {
    //加载轮播图
    var params = {
      url: "/indexImgs",
      method: "GET",
      data: {},
      callBack: (res) => {
        this.setData({
          indexImgs: res,
          seq: res
        });
      }
    };
    http.request(params);
  },
  getNoticeList() {
    // 加载公告
    var params = {
      url: "/shop/notice/topNoticeList",
      method: "GET",
      data: {},
      callBack: (res) => {
        this.setData({
          news: res,
        });
      }
    };
    http.request(params);
  },

  /**
   * 加入购物车
   */
   addToCart(e) {
    const prodId = e.currentTarget.dataset.prodid
    const ths = this
    wx.showLoading();
    var params = {
      url: "/prod/prodInfo",
      method: "GET",
      data: {
        prodId
      },
      callBack: (res) => {
        var params = {
          url: "/p/shopCart/changeItem",
          method: "POST",
          data: {
            basketId: 0,
            count: 1,
            prodId: res.prodId,
            shopId: res.shopId,
            skuId: res.skuList[0].skuId
          },
          callBack: function(res) {
            ths.setData({
              totalCartNum: ths.data.totalCartNum + ths.data.prodNum
            });
            wx.hideLoading();
            http.getCartCount(); //重新计算购物车总数量
            wx.showToast({
              title: "加入购物车成功",
              icon: "none"
            })
          }
        };
        http.request(params);
      }
    };
    http.request(params);
  },


  // 加载商品标题分组列表
  getTag() {
    var params = {
      url: "/prod/tag/prodTagList",
      method: "GET",
      data: {},
      callBack: (res) => {
        this.setData({
          taglist: res,
        });
        for (var i = 0; i < res.length; i++) {
          this.getTagProd(res[i].id, i);
        }
      }
    };
    http.request(params);
  },

  getTagProd(id, index) {
    var param = {
      url: "/prod/prodListByTagId",
      method: "GET",
      data: {
        tagId: id,
        size: 6
      },
      callBack: (res) => {
        var taglist = this.data.taglist;
        taglist[index].prods = res.records

        this.setData({
          taglist: taglist,
        });
      }
    };
    http.request(param);
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  // onPullDownRefresh: function () {
  //     wx.request({
  //       url: '',
  //       data: {},
  //       method: 'GET',
  //       success: function (res) { },
  //       fail: function (res) { },
  //       complete: function (res) {
  //         wx.stopPullDownRefresh();
  //       }
  //     })
  // },

  onPullDownRefresh: function() {

    // wx.showNavigationBarLoading() //在标题栏中显示加载

    //模拟加载
    var ths = this;
    setTimeout(function() {

      ths.getAllData();

      // wx.hideNavigationBarLoading() //完成停止加载

      wx.stopPullDownRefresh() //停止下拉刷新

    }, 100);

  },

  /**
   * 跳转至商品详情
   */
  showProdInfo: function(e) {
    let relation = e.currentTarget.dataset.relation;
    if (relation) {
      wx.navigateTo({
        url: 'pages/prod/prod',
      })
    }
  }
})
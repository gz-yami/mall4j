// pages/prod/prod.js
const app = getApp()
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var util = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    shopId: 1,
    picDomain: config.picDomain,
    indicatorDots: true,
    indicatorColor: '#f2f2f2',
    indicatorActiveColor: '#eb2444',
    autoplay: true,
    interval: 3000,
    duration: 1000,
    prodNum: 1,
    totalCartNum: 0,
    pic: "",
    imgs: '',
    prodName: '',
    price: 0,
    content: '',
    prodId: 0,
    brief: '',
    skuId: 0,
    popupShow: false,
    // 是否获取过用户领取过的优惠券id
    loadCouponIds: false,
    skuShow: false,
    commentShow: false,
    couponList: [],
    skuList: [],
    skuGroup: {},
    findSku: true,
    defaultSku: undefined,
    selectedProp: [],
    selectedPropObj: {},
    propKeys: [],
    allProperties: [],
    prodCommData: {},
    prodCommPage: {
      current: 0,
      pages: 0,
      records: []
    },
    littleCommPage: [],
    evaluate: -1,
    isCollection: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      prodId: options.prodid,
    });

    // 加载商品信息
    this.getProdInfo();
    // 加载评论数据
    this.getProdCommData();
    // 加载评论项
    this.getLittleProdComm();
    // 查看用户是否关注
    this.getCollection();
  },

  /**
   * 获取是否关注信息
   */
  getCollection() {
    wx.showLoading();
    var params = {
      url: "/p/user/collection/isCollection",
      method: "GET",
      data: {
        prodId: this.data.prodId
      },
      callBack: (res) => {
        this.setData({
          isCollection: res
        })
        wx.hideLoading();
      }
    };
    http.request(params);
  },

  /**
   * 添加或者取消收藏商品 
   */
  addOrCannelCollection() {
    wx.showLoading();

    var params = {
      url: "/p/user/collection/addOrCancel",
      method: "POST",
      data: this.data.prodId,
      callBack: (res) => {
        this.setData({
          isCollection: !this.data.isCollection
        })
        wx.hideLoading();
      }
    };
    http.request(params);
  },

  // 获取商品信息
  getProdInfo() {
    wx.showLoading();
    var params = {
      url: "/prod/prodInfo",
      method: "GET",
      data: {
        prodId: this.data.prodId,
        // userType: 0
      },
      callBack: (res) => {
        //console.log(res);
        var imgStrs = res.imgs;
        var imgs = imgStrs.split(",");
        var content = util.formatHtml(res.content);
        this.setData({
          imgs: imgs,
          content: content,
          price: res.price,
          prodName: res.prodName,
          prodId: res.prodId,
          brief: res.brief,
          // skuId: res.skuId
          skuList: res.skuList,
          pic: res.pic
        });
        // 获取优惠券
        //this.getCouponList();
        // 组装sku
        this.groupSkuProp();

        wx.hideLoading();
      }
    };
    http.request(params);
  },
  getProdCommData() {
    http.request({
      url: "/prodComm/prodCommData",
      method: "GET",
      data: {
        prodId: this.data.prodId,
      },
      callBack: (res) => {
        this.setData({
          prodCommData: res
        })
      }
    })
  },
  // 获取部分评论
  getLittleProdComm() {
    if (this.data.prodCommPage.records.length) {
      return;
    }
    this.getProdCommPage();
  },
  getMoreCommPage(e) {
    this.getProdCommPage();
  },
  // 获取分页获取评论
  getProdCommPage(e) {
    if (e) {
      if (e.currentTarget.dataset.evaluate === this.data.evaluate) {
        return;
      }
      this.setData({
        prodCommPage: {
          current: 0,
          pages: 0,
          records: []
        },
        evaluate: e.currentTarget.dataset.evaluate
      })
    }
    http.request({
      url: "/prodComm/prodCommPageByProd",
      method: "GET",
      data: {
        prodId: this.data.prodId,
        size: 10,
        current: this.data.prodCommPage.current + 1,
        evaluate: this.data.evaluate
      },
      callBack: (res) => {
        res.records.forEach(item => {
          if (item.pics) {
            item.pics = item.pics.split(',')
          }
        })
        let records = this.data.prodCommPage.records
        records = records.concat(res.records)
        this.setData({
          prodCommPage: {
            current: res.current,
            pages: res.pages,
            records: records
          }
        })
        // 如果商品详情中没有评论的数据，截取两条到商品详情页商品详情
        if (!this.data.littleCommPage.length) {
          this.setData({
            littleCommPage: records.slice(0, 2)
          })
        }
      }
    })
  },
  getCouponList() {
    http.request({
      url: "/coupon/listByProdId",
      method: "GET",
      data: {
        prodId: this.data.prodId,
        shopId: this.data.shopId,
      },
      callBack: (res) => {
        this.setData({
          couponList: res
        })
      }
    })
  },

  /**
   * 根据skuList进行数据组装
   */
  groupSkuProp: function() {
    var skuList = this.data.skuList;

    //当后台返回只有一个SKU时，且SKU属性值为空时，即该商品没有规格选项，该SKU直接作为默认选中SKU
    if (skuList.length == 1 && skuList[0].properties == "") {
      this.setData({
        defaultSku: skuList[0]
      });
      return;
    }

    var skuGroup = {};//所有的规格名(包含规格名下的规格值集合）对象，如 {"颜色"：["金色","银色"],"内存"：["64G","256G"]}
    var allProperties = [];//所有SKU的属性值集合，如 ["颜色:金色;内存:64GB","颜色:银色;内存:64GB"]
    var propKeys = [];//所有的规格名集合，如 ["颜色","内存"]

    for (var i = 0; i < skuList.length; i++) {

      //找到和商品价格一样的那个SKU，作为默认选中的SKU
      var defaultSku = this.data.defaultSku;
      var isDefault = false;
      if (!defaultSku && skuList[i].price == this.data.price) { 
        defaultSku = skuList[i];
        isDefault = true;
        this.setData({
          defaultSku: defaultSku
        });
      }

      var properties = skuList[i].properties; //如：版本:公开版;颜色:金色;内存:64GB
      allProperties.push(properties);
      var propList = properties.split(";"); // 如：["版本:公开版","颜色:金色","内存:64GB"]

      var selectedPropObj = this.data.selectedPropObj;
      for (var j = 0; j < propList.length; j++) {

        var propval = propList[j].split(":"); //如 ["版本","公开版"]
        var props = skuGroup[propval[0]]; //先取出 规格名 对应的规格值数组

        //如果当前是默认选中的sku，把对应的属性值 组装到selectedProp
        if (isDefault) {
          propKeys.push(propval[0]);
          selectedPropObj[propval[0]] = propval[1];
        }

        if (props == undefined) {
          props = []; //假设还没有版本，新建个新的空数组
          props.push(propval[1]); //把 "公开版" 放进空数组
        } else {
          if (!this.array_contain(props, propval[1])) { //如果数组里面没有"公开版"
            props.push(propval[1]); //把 "公开版" 放进数组
          }
        }
        skuGroup[propval[0]] = props; //最后把数据 放回版本对应的值
      }
      this.setData({
        selectedPropObj: selectedPropObj,
        propKeys: propKeys
      });
    }
    this.parseSelectedObjToVals();
    this.setData({
      skuGroup: skuGroup,
      allProperties: allProperties
    });
  },

  //将已选的 {key:val,key2:val2}转换成 [val,val2]
  parseSelectedObjToVals: function() {
    var selectedPropObj = this.data.selectedPropObj;
    var selectedProperties = "";
    var selectedProp = [];
    for (var key in selectedPropObj) {
      selectedProp.push(selectedPropObj[key]);
      selectedProperties += key + ":" + selectedPropObj[key] + ";";
    }
    selectedProperties = selectedProperties.substring(0, selectedProperties.length - 1);
    this.setData({
      selectedProp: selectedProp
    });

    var findSku = false;
    for (var i = 0; i < this.data.skuList.length; i++) {
      if (this.data.skuList[i].properties == selectedProperties) {
        findSku = true;
        this.setData({
          defaultSku: this.data.skuList[i],
        });
        break;
      }
    }
    this.setData({
      findSku: findSku
    });
  },

  //点击选择规格
  toChooseItem: function(e) {
    var val = e.currentTarget.dataset.val;
    var key = e.currentTarget.dataset.key;
    var selectedPropObj = this.data.selectedPropObj;
    selectedPropObj[key] = val;
    this.setData({
      selectedPropObj: selectedPropObj
    });
    this.parseSelectedObjToVals();
  },

  //判断数组是否包含某对象
  array_contain: function(array, obj) {
    for (var i = 0; i < array.length; i++) {
      if (array[i] == obj) //如果要求数据类型也一致，这里可使用恒等号===
        return true;
    }
    return false;
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
    this.setData({
      totalCartNum: app.globalData.totalCartCount
    });
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

  /**
   * 跳转到首页
   */
  toHomePage: function() {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },

  /**
   * 跳转到购物车
   */
  toCartPage: function() {
    wx.switchTab({
      url: '/pages/basket/basket',
    })
  },

  /**
   * 加入购物车
   */
  addToCart: function(event) {
    if (!this.data.findSku) {
      return;
    }
    var ths = this;
    wx.showLoading({
      mask: true
    });
    var params = {
      url: "/p/shopCart/changeItem",
      method: "POST",
      data: {
        basketId: 0,
        count: this.data.prodNum,
        prodId: this.data.prodId,
        shopId: this.data.shopId,
        skuId: this.data.defaultSku.skuId
      },
      callBack: function(res) {
        //console.log(res);
        ths.setData({
          totalCartNum: ths.data.totalCartNum + ths.data.prodNum
        });
        wx.hideLoading();
        wx.showToast({
          title: "加入购物车成功",
          icon: "none"
        })
      }
    };
    http.request(params);
  },

  /**
   * 立即购买
   */
  buyNow: function() {
    if (!this.data.findSku) {
      return;
    }
    wx.setStorageSync("orderItem", JSON.stringify({
      prodId: this.data.prodId,
      skuId: this.data.defaultSku.skuId,
      prodCount: this.data.prodNum,
      shopId: this.data.shopId
    }));
    wx.navigateTo({
      url: '/pages/submit-order/submit-order?orderEntry=1',
    })

  },

  /**
   * 减数量
   */
  onCountMinus: function() {
    var prodNum = this.data.prodNum;
    if (prodNum > 1) {
      this.setData({
        prodNum: prodNum - 1
      });
    }
  },

  /**
   * 加数量
   */
  onCountPlus: function() {
    var prodNum = this.data.prodNum;
    if (prodNum < 1000) {
      this.setData({
        prodNum: prodNum + 1
      });
    }
  },

  /**
   * 分享设置
   */
  onShareAppMessage: function(res) {
    return {
      title: this.data.prodName,
      path: '/pages/prod/prod?prodid=' + this.data.prodid
    }
  },

  showPopup: function() {
    if (this.data.loadCouponIds) {
      this.setData({
        popupShow: true
      });
      return;
    }
    http.request({
      url: "/p/myCoupon/listCouponIds",
      method: "GET",
      data: {},
      callBack: (couponIds) => {
        var couponList = this.data.couponList;
        console.log(couponList)

        couponList.forEach(coupon => {
          if (couponIds && couponIds.length) {
            // 领取该优惠券数量
            var couponLimit = 0;
            couponIds.forEach(couponId => {
              if (couponId == coupon.couponId) {
                couponLimit++;
              }
            });
            // 小于用户领取优惠券上限，可以领取优惠券
            if (couponLimit < coupon.limitNum) {
              coupon.canReceive = true;
            } else {
              coupon.canReceive = false;
            }
          } else {
            coupon.canReceive = true;
          }
        });
        this.setData({
          couponList: couponList,
          popupShow: true,
          loadCouponIds: true
        })
      }
    })
  },
  showSku: function() {
    this.setData({
      skuShow: true
    });
  },
  showComment: function() {
    this.setData({
      commentShow: true
    });
  },

  closePopup: function() {
    this.setData({
      popupShow: false,
      skuShow: false,
      commentShow: false
    });
  },



})
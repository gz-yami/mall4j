<template>
<!-- 商品详情 -->
<view class="container">
  <!-- 轮播图 -->
  <swiper :indicator-dots="indicatorDots" :autoplay="autoplay" :indicator-color="indicatorColor" :interval="interval" :duration="duration" :indicator-active-color="indicatorActiveColor">
    <block v-for="(item, index) in imgs" :key="index">
      <swiper-item>
        <image :src="item"></image>
      </swiper-item>
    </block>
  </swiper>
  <!-- end  轮播图 -->
  <!-- 商品信息 -->
  <!-- <block wx:for="{{imgs}}" wx:key=''> -->
  <view class="prod-info">
    <view class="tit-wrap">
      <view class="prod-tit">{{prodName}}</view>
      <view class="col" @tap="addOrCannelCollection">
        <image v-if="!isCollection" src="/static/images/icon/prod-col.png"></image>
        <image v-if="isCollection" src="/static/images/icon/prod-col-red.png"></image>
        收藏
      </view>
    </view>
    <view class="sales-p">{{brief}}</view>
    <view class="prod-price">
      <text v-if="defaultSku && defaultSku.price" class="price">￥<text class="price-num">{{wxs.parsePrice(defaultSku.price)[0]}}</text>.{{wxs.parsePrice(defaultSku.price)[1]}}</text>
      <text v-if="defaultSku && defaultSku.oriPrice" class="ori-price">
        ￥{{wxs.parsePrice(defaultSku.oriPrice)[0]}}.{{wxs.parsePrice(defaultSku.oriPrice)[1]}}
      </text>
      <text class="sales"></text>
    </view>
    <!-- <button class="share-icon" open-type="share">
        <image src='../../images/icon/share.png'></image>
        <view class="share-text">分享</view>
      </button> -->
  </view>
  <!-- </block> -->
  <!-- end 商品信息 -->
  <!-- 领券 -->
  <!-- <view class="coupon" bindtap='showPopup' wx:if="{{couponList.length}}">
    <view class="coupon-tit">领券</view>
    <view class="coupon-con">
      <text class="item" wx:for="{{couponList}}" wx:key="item.couponId">满{{item.cashCondition}}<block wx:if="{{item.couponType == 1}}">减{{item.reduceAmount}}</block><block wx:if="{{item.couponType == 2}}">打{{item.couponDiscount}}折</block></text>
    </view>
    <view class="num">共{{couponList.length}}张</view>
    <view class="more">...</view>
  </view> -->
  <!-- 已选规格 -->
  <view class="sku" @tap="showSku">
    <view class="sku-tit">已选</view>
    <view class="sku-con">{{selectedProp.length>0?selectedProp+'，':''}}{{prodNum}}件</view>
    <view class="more">...</view>
  </view>
  <!-- 评价 -->
  <view class="cmt-wrap">
    <view class="cmt-tit" @tap="showComment">
      <view class="cmt-t">
        评价
        <text class="cmt-good">好评{{prodCommData.positiveRating}}%</text>
      </view>
      <view class="cmt-count">
        共{{prodCommData.number}}条
        <text class="cmt-more"></text>
      </view>
    </view>
    <view class="cmt-cont">
      <view class="cmt-tag" @tap="showComment">
        <text>全部({{prodCommData.number}})</text>
        <text>好评({{prodCommData.praiseNumber}})</text>
        <text>中评({{prodCommData.secondaryNumber}})</text>
        <text>差评({{prodCommData.negativeNumber}})</text>
        <text>有图({{prodCommData.picNumber}})</text>
      </view>
      <view class="cmt-items">
        <view v-for="(item, index) in littleCommPage" :key="index" class="cmt-item">
          <view class="cmt-user">
            <text class="date">{{item.recTime}}</text>
            <view class="cmt-user-info">
              <image class="user-img" :src="item.pic"></image>
              <view class="nickname">{{item.nickName}}</view>
              <!-- <van-rate readonly :value="item.score" @change="onChange" color="#f44"></van-rate> -->
            </view>
          </view>
          <view class="cmt-cnt">{{item.content}}</view>
          <scroll-view class="cmt-attr" scroll-x="true" v-if="item.pics.length">
            <image v-for="(commPic, index2) in item.pics" :key="index2" :src="commPic"></image>
          </scroll-view>
        </view>
      </view>
      <view class="cmt-more-v" v-if="prodCommPage.records.length > 2">
        <text @tap="showComment">查看全部评价</text>
      </view>
    </view>
  </view>
  <!-- 商品详情 -->
  <view class="prod-detail">
    <view>
      <rich-text :nodes="content"></rich-text>
      <!-- <image src="//img14.360buyimg.com/cms/jfs/t1/25195/1/9487/388554/5c7f80a5E8b8f8f0c/46818404849d6ec6.jpg!q70.dpg" mode="widthFix"></image> -->
    </view>
  </view>
  <!-- end 商品详情 -->
  <!-- 底部按钮 -->
  <view class="cart-footer">
    <view class="btn icon" @tap="toHomePage">
      <image src="/static/images/tabbar/homepage.png"></image>
      首页
    </view>
    <view class="btn icon" @tap="toCartPage">
      <image src="/static/images/tabbar/basket.png"></image>
      购物车
      <view class="badge badge-1" v-if="totalCartNum>0">{{totalCartNum}}</view>
    </view>
    <view class="btn cart" @tap="showSku">
      <text>加入购物车</text>
    </view>
    <view class="btn buy" @tap="showSku">
      <text>立即购买</text>
    </view>
  </view>

  <!-- end 底部按钮 -->

  <!-- 优惠券 -->
  <!-- <view class="popup-hide" wx:if="{{popupShow}}">
    <view class="popup-box">
      <view class="popup-tit">
        <text>优惠券</text>
        <text class="close" bindtap='closePopup'></text>
      </view>
      <view class='popup-cnt'>
        <block wx:for="{{couponList}}" wx:key='couponId'>
          <coupon showTimeType="{{1}}" canUse="{{true}}" item="{{item}}"></coupon>
        </block>
      </view>
    </view>
  </view> -->
  <!-- 规格弹窗 -->
  <view class="pup-sku" v-if="skuShow">
    <view class="pup-sku-main">
      <view class="pup-sku-header">
        <image class="pup-sku-img" :src="defaultSku.pic?defaultSku.pic:pic"></image>
        <view class="pup-sku-price">
          ￥
          <text v-if="defaultSku && defaultSku.price" class="pup-sku-price-int">{{wxs.parsePrice(defaultSku.price)[0]}}</text> .{{wxs.parsePrice(defaultSku.price)[1]}}
        </view>
        <view class="pup-sku-prop">
          <text>已选</text> {{selectedProp.length>0?selectedProp+'，':''}}{{prodNum}}件
        </view>
        <view class="close" @tap="closePopup"></view>
      </view>
      <view class="pup-sku-body">
        <view class="pup-sku-area">
          <block v-for="(value, key) in skuGroup" :key="key">
            <view class="sku-kind">{{key}}</view>
            <view class="sku-choose">
              <block v-for="(item, index) in value" :key="index">
                <text :class="'sku-choose-item ' + (wxs.array_contain(selectedProp,item)?'active':'') + ' ' + (wxs.props_contain(allProperties,selectedPropObj,key,item,propKeys)?'':'gray')" :data-ok="wxs.props_contain(allProperties,selectedPropObj,key,item,propKeys)" @tap="toChooseItem" :data-key="key" :data-val="item">{{item}}</text>
              </block>
            </view>
          </block>
        </view>
        <view class="pup-sku-count">
          <view class="num-wrap">
            <view class="minus" @tap="onCountMinus">
              <text class="row"></text>
            </view>
            <view class="text-wrap">
              <input type="number" :value="prodNum" disabled></input>
            </view>
            <view class="plus" @tap="onCountPlus">
              <text class="row"></text>
              <text class="col"></text>
            </view>
          </view>
          <view class="count-name">数量</view>
        </view>
      </view>
      <view class="pup-sku-footer">
        <view class="btn cart" @tap="addToCart">加入购物车</view>
        <view class="btn buy" @tap="buyNow">立即购买</view>
      </view>
    </view>
  </view>
  <!-- 评价弹窗 -->
  <view class="cmt-popup" v-if="commentShow">
    <view class="cmt-tit">
      <view class="cmt-t">
        商品评价
        <text class="cmt-good">好评度{{prodCommData.positiveRating}}%</text>
      </view>
      <text class="close" @tap="closePopup"></text>
    </view>
    <view class="cmt-cont">
      <view class="cmt-tag">
        <text @tap="getProdCommPage" data-evaluate="-1" :class="evaluate==-1?'selected':''">全部({{prodCommData.number}})</text>
        <text @tap="getProdCommPage" data-evaluate="0" :class="evaluate==0?'selected':''">好评({{prodCommData.praiseNumber}})</text>
        <text @tap="getProdCommPage" data-evaluate="1" :class="evaluate==1?'selected':''">中评({{prodCommData.secondaryNumber}})</text>
        <text @tap="getProdCommPage" data-evaluate="2" :class="evaluate==2?'selected':''">差评({{prodCommData.negativeNumber}})</text>
        <text @tap="getProdCommPage" data-evaluate="3" :class="evaluate==3?'selected':''">有图({{prodCommData.picNumber}})</text>
      </view>
      <view class="cmt-items">
        <block v-if="prodCommPage.records.length">
          <view v-for="(item, index) in prodCommPage.records" :key="index" class="cmt-item">
            <view class="cmt-user">
              <text class="date">{{item.recTime}}</text>
              <view class="cmt-user-info">
                <image class="user-img" :src="item.pic"></image>
                <view class="nickname">{{item.nickName}}</view>
                <!-- <van-rate readonly :value="item.score" @change="onChange" color="#f44"></van-rate> -->
              </view>
            </view>
            <view class="cmt-cnt">{{item.content}}</view>
            <scroll-view class="cmt-attr" scroll-x="true" v-if="item.pics.length">
              <image v-for="(commPic, index2) in item.pics" :key="index2" :src="commPic"></image>
            </scroll-view>
            <view class="cmt-reply" v-if="item.replyContent">
              <text class="reply-tit">店铺回复：</text> {{item.replyContent}}
            </view>
          </view>
        </block>
        <view v-if="!prodCommPage.records.length" class="empty">暂无评价</view>
      </view>
      <view class="load-more" v-if="prodCommPage.pages > prodCommPage.current">
        <text @tap="getMoreCommPage">点击加载更多</text>
      </view>
    </view>
  </view>
</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/prod/prod.js
const app = getApp();
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");
var util = require("../../utils/util.js");
import coupon from "../../components/coupon/coupon";
// import vanRate from "../../vant/rate/index";

export default {
  data() {
    return {
      shopId: 1,
      // picDomain: config.picDomain,
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
    };
  },

  components: {
    coupon
  },
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      prodId: options.prodid
    }); // 加载商品信息

    this.getProdInfo(); // 加载评论数据

    this.getProdCommData(); // 加载评论项

    this.getLittleProdComm(); // 查看用户是否关注

    this.getCollection();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      totalCartNum: app.globalData.totalCartCount
    });
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {},

  /**
   * 分享设置
   */
  onShareAppMessage: function (res) {
    return {
      title: this.prodName,
      path: '/pages/prod/prod?prodid=' + this.prodid
    };
  },
  methods: {
    /**
     * 获取是否关注信息
     */
    getCollection() {
      uni.showLoading();
      var params = {
        url: "/p/user/collection/isCollection",
        method: "GET",
        data: {
          prodId: this.prodId
        },
        callBack: res => {
          this.setData({
            isCollection: res
          });
          uni.hideLoading();
        }
      };
      http.request(params);
    },

    /**
     * 添加或者取消收藏商品
     */
    addOrCannelCollection() {
      uni.showLoading();
      var params = {
        url: "/p/user/collection/addOrCancel",
        method: "POST",
        data: this.prodId,
        callBack: res => {
          this.setData({
            isCollection: !this.isCollection
          });
          uni.hideLoading();
        }
      };
      http.request(params);
    },

    // 获取商品信息
    getProdInfo() {
      uni.showLoading();
      var params = {
        url: "/prod/prodInfo",
        method: "GET",
        data: {
          prodId: this.prodId // userType: 0

        },
        callBack: res => {
          uni.hideLoading()
          if (!res) {
            setTimeout(() => {
              uni.navigateBack()
            }, 1000);
            return
          }
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
          }); // 获取优惠券
          //this.getCouponList();
          // 组装sku

          this.groupSkuProp();
          uni.hideLoading();
        }
      };
      http.request(params);
    },

    getProdCommData() {
      http.request({
        url: "/prodComm/prodCommData",
        method: "GET",
        data: {
          prodId: this.prodId
        },
        callBack: res => {
          this.setData({
            prodCommData: res
          });
          console.log('评论prodCommData:', this.prodCommData)
        }
      });
    },

    // 获取部分评论
    getLittleProdComm() {
      if (this.prodCommPage.records.length) {
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
        if (e.currentTarget.dataset.evaluate === this.evaluate) {
          return;
        }

        this.setData({
          prodCommPage: {
            current: 0,
            pages: 0,
            records: []
          },
          evaluate: e.currentTarget.dataset.evaluate
        });
      }

      http.request({
        url: "/prodComm/prodCommPageByProd",
        method: "GET",
        data: {
          prodId: this.prodId,
          size: 10,
          current: this.prodCommPage.current + 1,
          evaluate: this.evaluate
        },
        callBack: res => {
          res.records.forEach(item => {
            if (item.pics) {
              item.pics = item.pics.split(',');
            }
          });
          let records = this.prodCommPage.records;
          records = records.concat(res.records);
          this.setData({
            prodCommPage: {
              current: res.current,
              pages: res.pages,
              records: records
            }
          }); // 如果商品详情中没有评论的数据，截取两条到商品详情页商品详情

          if (!this.littleCommPage.length) {
            this.setData({
              littleCommPage: records.slice(0, 2)
            });
          }
        }
      });
    },

    getCouponList() {
      http.request({
        url: "/coupon/listByProdId",
        method: "GET",
        data: {
          prodId: this.prodId,
          shopId: this.shopId
        },
        callBack: res => {
          this.setData({
            couponList: res
          });
        }
      });
    },

    //根据sku的属性 分组
    groupSkuProp: function () {
      var skuList = this.skuList;

      if (skuList.length == 1 && skuList[0].properties == "") {
        this.setData({
          defaultSku: skuList[0]
        });
        return;
      }

      var skuGroup = {};
      var allProperties = [];
      var propKeys = [];

      for (var i = 0; i < skuList.length; i++) {
        var defaultSku = this.defaultSku;
        var isDefault = false;

        if (!defaultSku && skuList[i].price == this.price) {
          //找到和商品价格一样的那个SKU，作为默认选中的SKU
          defaultSku = skuList[i];
          isDefault = true;
          this.setData({
            defaultSku: defaultSku
          });
        }

        var properties = skuList[i].properties; //版本:公开版;颜色:金色;内存:64GB

        allProperties.push(properties);
        var propList = properties.split(";"); // ["版本:公开版","颜色:金色","内存:64GB"]

        var selectedPropObj = this.selectedPropObj;

        for (var j = 0; j < propList.length; j++) {
          var propval = propList[j].split(":"); //["版本","公开版"]

          var props = skuGroup[propval[0]]; //先取出 版本对应的值数组
          //如果当前是默认选中的sku，把对应的属性值 组装到selectedProp

          if (isDefault) {
            propKeys.push(propval[0]);
            selectedPropObj[propval[0]] = propval[1];
          }

          if (props == undefined) {
            props = []; //假设还没有版本，新建个新的空数组

            props.push(propval[1]); //把 "公开版" 放进空数组
          } else {
            if (!this.array_contain(props, propval[1])) {
              //如果数组里面没有"公开版"
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
    parseSelectedObjToVals: function () {
      var selectedPropObj = this.selectedPropObj;
      var selectedProperties = "";
      var selectedProp = [];

      for (var key in selectedPropObj) {
        selectedProp.push(selectedPropObj[key]);
        selectedProperties += key + ":" + selectedPropObj[key] + ";";
      }

      selectedProperties = selectedProperties.substring(0, selectedProperties.length - 1); // console.log(selectedProperties);

      this.setData({
        selectedProp: selectedProp
      });

      for (var i = 0; i < this.skuList.length; i++) {
        if (this.skuList[i].properties == selectedProperties) {
          this.setData({
            defaultSku: this.skuList[i]
          });
          break;
        }
      }
    },
    //点击选择规格
    toChooseItem: function (e) {
      var ok = e.currentTarget.dataset.ok;

      if (!ok) {
        return;
      }

      var val = e.currentTarget.dataset.val;
      var key = e.currentTarget.dataset.key;
      var selectedPropObj = this.selectedPropObj;
      selectedPropObj[key] = val;
      this.setData({
        selectedPropObj: selectedPropObj
      });
      this.parseSelectedObjToVals();
    },
    //判断数组是否包含某对象
    array_contain: function (array, obj) {
      for (var i = 0; i < array.length; i++) {
        if (array[i] == obj) //如果要求数据类型也一致，这里可使用恒等号===
          return true;
      }

      return false;
    },

    /**
     * 跳转到首页
     */
    toHomePage: function () {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },

    /**
     * 跳转到购物车
     */
    toCartPage: function () {
      uni.switchTab({
        url: '/pages/basket/basket'
      });
    },

    /**
     * 加入购物车
     */
    addToCart: function (event) {
      // var ths = this;
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/shopCart/changeItem",
        method: "POST",
        data: {
          basketId: 0,
          count: this.prodNum,
          prodId: this.prodId,
          shopId: this.shopId,
          skuId: this.defaultSku.skuId
        },
        callBack: res => {
          //console.log(res);
          this.setData({
            totalCartNum: this.totalCartNum + this.prodNum
          });
          uni.hideLoading();
          uni.showToast({
            title: "加入购物车成功",
            icon: "none"
          });
        }
      };
      http.request(params);
    },

    /**
     * 立即购买
     */
    buyNow: function () {
      uni.setStorageSync("orderItem", JSON.stringify({
        prodId: this.prodId,
        skuId: this.defaultSku.skuId,
        prodCount: this.prodNum,
        shopId: this.shopId
      }));
      uni.navigateTo({
        url: '/pages/submit-order/submit-order?orderEntry=1'
      });
    },

    /**
     * 减数量
     */
    onCountMinus: function () {
      var prodNum = this.prodNum;

      if (prodNum > 1) {
        this.setData({
          prodNum: prodNum - 1
        });
      }
    },

    /**
     * 加数量
     */
    onCountPlus: function () {
      var prodNum = this.prodNum;

      if (prodNum < 1000) {
        this.setData({
          prodNum: prodNum + 1
        });
      }
    },
    showPopup: function () {
      if (this.loadCouponIds) {
        this.setData({
          popupShow: true
        });
        return;
      }

      http.request({
        url: "/p/myCoupon/listCouponIds",
        method: "GET",
        data: {},
        callBack: couponIds => {
          var couponList = this.couponList;
          console.log(couponList);
          couponList.forEach(coupon => {
            if (couponIds && couponIds.length) {
              // 领取该优惠券数量
              var couponLimit = 0;
              couponIds.forEach(couponId => {
                if (couponId == coupon.couponId) {
                  couponLimit++;
                }
              }); // 小于用户领取优惠券上限，可以领取优惠券

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
          });
        }
      });
    },
    showSku: function () {
      this.setData({
        skuShow: true
      });
    },
    showComment: function () {
      this.setData({
        commentShow: true
      });
    },
    closePopup: function () {
      this.setData({
        popupShow: false,
        skuShow: false,
        commentShow: false
      });
    }
  }
};
</script>
<style>
@import "./prod.css";
</style>

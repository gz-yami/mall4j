<template>
<view>
<!--pages/submit-order/submit-order.wxml-->
<view class="container">
  <view class="submit-order">
    <!-- 收货地址 -->
    <view class="delivery-addr " @tap="toAddrListPage">
      <view class="addr-bg " v-if="!userAddr">
        <view class="add-addr">
          <view class="plus-sign-img">
            <image src="/static/images/icon/plus-sign.png"></image>
          </view>
          <text>新增收货地址</text>
        </view>
        <view class="arrow empty"></view>
      </view>
      <view class="addr-bg whole" v-if="userAddr">
        <view class="addr-icon">
          <image src="/static/images/icon/addr.png"></image>
        </view>
        <view class="user-info">
          <text class="item">{{userAddr.receiver}}</text>
          <text class="item">{{userAddr.mobile}}</text>
        </view>
        <view class="addr">{{userAddr.province}}{{userAddr.city}}{{userAddr.area}}{{userAddr.addr}}</view>
        <view class="arrow"></view>
      </view>
    </view>

    <!-- 商品详情 -->
    <view class="prod-item">
      <block v-for="(item, index) in orderItems" :key="index">
        <view class="item-cont" @tap="toOrderDetailPage" :data-ordernum="item.primaryOrderNo">
          <view class="prod-pic">
            <image :src="item.pic"></image>
          </view>
          <view class="prod-info">
            <view class="prodname">
              {{item.prodName}}
            </view>
            <view class="prod-info-cont">{{item.skuName}}</view>
            <view class="price-nums">
              <text class="prodprice"><text class="symbol">￥</text>
              <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text>
              <text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text></text>
              <text class="prodcount">x{{item.prodCount}}</text>
            </view>
          </view>
        </view>
      </block>
      <!-- <view class='item-cont' bindtap='toOrderDetailPage' data-ordernum="{{item.primaryOrderNo}}">
        <view class='prod-pic'>
          <image src='../../images/prod/pic09.jpg'></image>
        </view>
        <view class='prod-info'>
          <view class='prodname'>
            THE BEAST/野兽派 易烊千玺同款
          </view>
          <view class='prod-info-cont'>经典杯型升级，杯型更细长优雅</view>
          <view class='price-nums'>
            <text class='prodprice'><text class='symbol'>￥</text>
            <text class='big-num'>{{wxs.parsePrice(40.00)[0]}}</text>
            <text class='small-num'>.{{wxs.parsePrice(40.00)[1]}}</text></text>
            <text class="prodcount">x1</text>
          </view>
        </view>
      </view> -->

      <view class="total-num">
        <text class="prodcount">共{{totalCount}}件商品</text>
        <view class="prodprice">合计：
          <text class="symbol">￥</text>
          <text class="big-num">{{wxs.parsePrice(total)[0]}}</text>
          <text class="small-num">.{{wxs.parsePrice(total)[1]}}</text>
        </view>
      </view>
    </view>

    <!-- 订单详情 -->
    <view class="order-msg">
      <view class="msg-item">
        <view class="item coupon" @tap="showCouponPopup">
          <text class="item-tit">优惠券：</text>
          <text class="item-txt" v-if="!coupons.canUseCoupons">暂无可用</text>
          <text class="coupon-btn">{{coupons.totalLength? coupons.totalLength: 0}}张</text>
          <text class="arrow"></text>
        </view>
        <view class="item">
          <text>买家留言：</text>
          <input v-model="remarks" placeholder="给卖家留言" />
        </view>
      </view>

    </view>

    <view class="order-msg">
      <view class="msg-item">
        <view class="item">
          <view class="item-tit">订单总额：</view>
          <view class="item-txt price">
            <text class="symbol">￥</text>
            <text class="big-num">{{wxs.parsePrice(total)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(total)[1]}}</text>
          </view>
        </view>
        <view class="item">
          <view class="item-tit">运费：</view>
          <view class="item-txt price">
            <text class="symbol">￥</text>
            <text class="big-num">{{wxs.parsePrice(transfee)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(transfee)[1]}}</text>
          </view>
        </view>
        <view class="item">
          <view class="item-tit">优惠金额：</view>
          <view class="item-txt price">
            <text class="symbol">-￥</text>
            <text class="big-num">{{wxs.parsePrice(shopReduce)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(shopReduce)[1]}}</text>
          </view>
        </view>
        <view class="item payment">
          <view class="item-txt price">
            小计：
            <text class="symbol">￥</text>
            <text class="big-num">{{wxs.parsePrice(actualTotal)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(actualTotal)[1]}}</text>
          </view>
        </view>
      </view>
    </view>
  </view>


  <!-- 底部栏 -->
  <view class="submit-order-footer">
    <view class="sub-order">
      <view class="item-txt">
        合计：
        <view class="price">
          <text class="symbol">￥</text>
          <text class="big-num">{{wxs.parsePrice(actualTotal)[0]}}</text>
          <text class="small-num">.{{wxs.parsePrice(actualTotal)[1]}}</text>
        </view>
      </view>
    </view>
    <view class="footer-box" @tap="toPay">
      提交订单
    </view>
  </view>
</view>

<!-- 选择优惠券弹窗 -->
<view class="popup-hide" v-if="popupShow">
  <view class="popup-box">
    <view class="popup-tit">
      <text>优惠券</text>
      <text class="close" @tap="closePopup"></text>
    </view>
    <view class="coupon-tabs">
      <view :class="'coupon-tab ' + (couponSts==1?'on':'')" @tap="changeCouponSts" data-sts="1">可用优惠券({{coupons.canUseCoupons.length?coupons.canUseCoupons.length:0}})</view>
      <view :class="'coupon-tab ' + (couponSts==2?'on':'')" @tap="changeCouponSts" data-sts="2">不可用优惠券({{coupons.unCanUseCoupons.length?coupons.unCanUseCoupons.length:0}})</view>
    </view>
    <view class="popup-cnt">
      <block v-for="(item, index) in coupons.canUseCoupons" :key="index" v-if="couponSts == 1">
        <coupon :item="item" order="true" @checkCoupon="checkCoupon" canUse="true"></coupon>
      </block>
      <block v-for="(item, index) in coupons.unCanUseCoupons" :key="index" v-if="couponSts == 2">
        <coupon :item="item" order="true" canUse="false"></coupon>
      </block>
      <view class="botm-empty"></view>
    </view>
    <view class="coupon-ok" v-if="couponSts==1">
      <text @tap="choosedCoupon">确定</text>
    </view>
  </view>
</view>
</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/submit-order/submit-order.js
var http = require("../../utils/http.js");
import coupon from "../../components/coupon/coupon";

export default {
  data() {
    return {
      popupShow: false,
      couponSts: 1,
      couponList: [],
      // 订单入口 0购物车 1立即购买
      orderEntry: "0",
      userAddr: null,
      orderItems: [],
      coupon: {
        totalLength: 0,
        canUseCoupons: [],
        noCanUseCoupons: []
      },
      actualTotal: 0,
      total: 0,
      totalCount: 0,
      transfee: 0,
      reduceAmount: 0,
      remarks: "",
      couponIds: [],
      coupons: {},
      shopReduce: "",
      item: {},
      selAddress: ''
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
      orderEntry: options.orderEntry
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1];
    if (currPage.selAddress == "yes") {
      //将携带的参数赋值
      this.userAddr = currPage.item
    }

    //获取订单数据
    this.loadOrderData();
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
  methods: {
    //加载订单数据
    loadOrderData: function () {
      var addrId = 0;

      if (this.userAddr != null) {
        addrId = this.userAddr.addrId;
      }

      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/confirm",
        method: "POST",
        data: {
          addrId: addrId,
          orderItem: this.orderEntry === "1" ? JSON.parse(uni.getStorageSync("orderItem")) : undefined,
          basketIds: this.orderEntry === "0" ? JSON.parse(uni.getStorageSync("basketIds")) : undefined,
          couponIds: this.couponIds,
          userChangeCoupon: 1
        },
        callBack: res => {
          uni.hideLoading();
          let orderItems = [];
          res.shopCartOrders[0].shopCartItemDiscounts.forEach(itemDiscount => {
            orderItems = orderItems.concat(itemDiscount.shopCartItems);
          });

          if (res.shopCartOrders[0].coupons) {
            let canUseCoupons = [];
            let unCanUseCoupons = [];
            res.shopCartOrders[0].coupons.forEach(coupon => {
              if (coupon.canUse) {
                canUseCoupons.push(coupon);
              } else {
                unCanUseCoupons.push(coupon);
              }
            });
            this.setData({
              coupons: {
                totalLength: res.shopCartOrders[0].coupons.length,
                canUseCoupons: canUseCoupons,
                unCanUseCoupons: unCanUseCoupons
              }
            });
          }

          this.setData({
            orderItems: orderItems,
            actualTotal: res.actualTotal,
            total: res.total,
            totalCount: res.totalCount,
            userAddr: res.userAddr,
            transfee: res.shopCartOrders[0].transfee,
            shopReduce: res.shopCartOrders[0].shopReduce
          });
        },
        errCallBack: res => {
          uni.hideLoading();
          this.chooseCouponErrHandle(res);
        }
      };
      http.request(params);
    },

    /**
     * 优惠券选择出错处理方法
     */
    chooseCouponErrHandle(res) {
      // 优惠券不能共用处理方法
      if (res.statusCode == 601) {
        uni.showToast({
          title: res.data,
          icon: "none",
          duration: 3000,
          success: res => {
            this.setData({
              couponIds: []
            });
          }
        });
        setTimeout(() => {
          this.loadOrderData();
        }, 2500);
      }
    },

    /**
     * 提交订单
     */
    toPay: function () {
      if (!this.userAddr) {
        uni.showToast({
          title: '请选择地址',
          icon: "none"
        });
        return;
      }

      this.submitOrder();
    },
    submitOrder: function () {
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/submit",
        method: "POST",
        data: {
          orderShopParam: [{
            remarks: this.remarks,
            shopId: 1
          }]
        },
        callBack: res => {
					console.log("res",res)
          uni.hideLoading();
          // this.calWeixinPay(res.orderNumbers);
					this.normalPay(res.orderNumbers)
					
        }
      };
      http.request(params);
    },
		
		//模拟支付，直接提交成功
		normalPay: function(orderNumbers){
			uni.showLoading({
			  mask: true
			});
			var params = {
			  url: "/p/order/normalPay",
			  method: "POST",
			  data: {
			    orderNumbers: orderNumbers
			  },
			  callBack: res => {
					console.log("res",res)
					uni.hideLoading();
					if(res){
						uni.showToast({
							title: "模拟支付成功",
							icon:"none"
						})
						setTimeout(() => {
							uni.navigateTo({
							  url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + orderNumbers
							});
						},1200)
					}else{
						uni.showToast({
							title: "支付失败！",
							icon:"none"
						})
					}
			  }
			};
			http.request(params);
		},
		
    /**
     * 唤起微信支付
     */
    calWeixinPay: function (orderNumbers) {
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/pay",
        method: "POST",
        data: {
          payType: 1,
          orderNumbers: orderNumbers
        },
        callBack: function (res) {
          uni.hideLoading();
          uni.requestPayment({
            timeStamp: res.timeStamp,
            nonceStr: res.nonceStr,
            package: res.packageValue,
            signType: res.signType,
            paySign: res.paySign,
            success: e => {
              // console.log("支付成功");
              uni.navigateTo({
                url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + orderNumbers + "&orderType=" + this.orderType
              });
            },
            fail: err => {
              uni.navigateTo({
                url: '/pages/pay-result/pay-result?sts=0&orderNumbers=' + orderNumbers + "&orderType=" + this.orderType
              });
            }
          });
        }
      };
      http.request(params);
    },
    changeCouponSts: function (e) {
      this.setData({
        couponSts: e.currentTarget.dataset.sts
      });
    },
    showCouponPopup: function () {
      this.setData({
        popupShow: true
      });
    },
    closePopup: function () {
      this.setData({
        popupShow: false
      });
    },

    /**
     * 去地址页面
     */
    toAddrListPage: function () {
      uni.navigateTo({
        url: '/pages/delivery-address/delivery-address?order=0'
      });
    },

    /**
     * 确定选择好的优惠券
     */
    choosedCoupon: function () {
      this.loadOrderData();
      this.setData({
        popupShow: false
      });
    },

    /**
     * 优惠券子组件发过来
     */
    checkCoupon: function (e) {
      var ths = this;
      let index = ths.couponIds.indexOf(e.detail.couponId);

      if (index === -1) {
        ths.couponIds.push(e.detail.couponId);
      } else {
        ths.couponIds.splice(index, 1);
      }
    }
  }
};
</script>
<style>
@import "./submit-order.css";
</style>
<template>
<!--pages/pay-result/pay-result.wxml-->
<view class="container">
  <view v-if="sts == 0">
    <view class="pay-sts fail">支付失败</view>
    <view class="tips">请在
      <text class="warn">30分钟</text>内完成付款</view>
    <view class="tips">否则订单会被系统取消</view>
    <view class="btns">
      <text class="button checkorder" @tap="toOrderList">查看订单</text>
      <text class="button payagain" @tap="payAgain">重新支付</text>
    </view>
  </view>

  <view v-if="sts == 1">
    <view class="pay-sts succ">支付成功</view>
    <view class="tips">感谢您的购买</view>
    <view class="btns">
      <text class="button checkorder" @tap="toOrderList">查看订单</text>
      <text class="button shopcontinue" @tap="toIndex">继续购物</text>
    </view>
  </view>

</view>
</template>

<script>

export default {
  data() {
    return {
      sts: 0,
      orderNumbers: ''
    };
  },

  components: {},
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      sts: options.sts,
      orderNumbers: options.orderNumbers
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {},

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
    toOrderList: function () {
      uni.navigateTo({
        url: '/pages/orderList/orderList?sts=0'
      });
    },
    toIndex: function () {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },
    payAgain: function () {
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/pay",
        method: "POST",
        data: {
          payType: 1,
          orderNumbers: this.orderNumbers
        },
        callBack: function (res) {
          //console.log(res);
          uni.hideLoading();
          uni.requestPayment({
            timeStamp: res.timeStamp,
            nonceStr: res.nonceStr,
            package: res.packageValue,
            signType: res.signType,
            paySign: res.paySign,
            success: e => {
              //console.log("支付成功");
              uni.redirectTo({
                url: '/pages/pay-result/pay-result?sts=1&orderNum=' + orderNumbers + "&orderType=" + this.orderType
              });
            },
            fail: err => {}
          });
        }
      };
      http.request(params);
    }
  }
};
</script>
<style>
@import "./pay-result.css";
</style>
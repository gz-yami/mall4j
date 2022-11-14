<template>
<!--pages/order-detail/order-detail.wxml-->

<view class="container">

  <view class="order-detail">
    <view class="delivery-addr" v-if="userAddrDto">
      <view class="user-info">
        <text class="item">{{userAddrDto.receiver}}</text>
        <text class="item">{{userAddrDto.mobile}}</text>
      </view>
      <view class="addr">{{userAddrDto.province}}{{userAddrDto.city}}{{userAddrDto.area}}{{userAddrDto.area}}{{userAddrDto.addr}}</view>
    </view>



    <!-- 商品信息 -->
    <view class="prod-item" v-if="orderItemDtos">
      
      <block v-for="(item, index) in orderItemDtos" :key="index">
        <view class="item-cont" @tap="toProdPage" :data-prodid="item.prodId">
          <view class="prod-pic">
            <image :src="item.pic"></image>
          </view>
          <view class="prod-info">
            <view class="prodname">
              {{item.prodName}}
            </view>
            <view class="prod-info-cont">
              <text class="number">数量：{{item.prodCount}}</text>
              <text class="info-item">{{item.skuName}}</text>
            </view>
            <view class="price-nums clearfix">
              <text class="prodprice"><text class="symbol">￥</text>
              <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text>
              <text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text></text>
              <view class="btn-box">
                <!-- <text class="btn" v-if="item.status!=1">申请售后</text> -->
                <!-- <text class="btn">加购物车</text> -->
              </view>
            </view>
          </view>
        </view>
      </block>
      <!-- <view class='item-cont' bindtap='toOrderDetailPage' data-ordernum="{{item.primaryOrderNo}}">
        <view class='prod-pic'>
          <image src='../../images/prod/pic10.jpg'></image>
        </view>
        <view class='prod-info'>
          <view class='prodname'>
            THE BEAST/野兽派 雪花发财狗
          </view>
          <view class='prod-info-cont'>
            <text class='number'>数量：1</text>
            <text class='info-item'>发财狗</text>
          </view>
          <view class='price-nums clearfix'>
            <text class='prodprice'><text class='symbol'>￥</text>
            <text class='big-num'>{{wxs.parsePrice(40.00)[0]}}</text>
            <text class='small-num'>.{{wxs.parsePrice(40.00)[1]}}</text></text>
            <view class='btn-box'>
              <text class='btn'>申请售后</text>
              <text class='btn'>加购物车</text>
            </view>
          </view>
        </view>
      </view> -->
      <!-- <view class="prod-foot">
        <view class="btn">
          <button v-if="status==1" class="button" @tap="onCancelOrder" :data-ordernum="orderNumber" hover-class="none">取消订单</button>
          <button v-if="status==1" class="button warn" @tap="onConfirmReceive" :data-ordernum="orderNumber" hover-class="none">再次购买</button>
          <button v-if="status==1" class="button warn" @tap="onPayAgain" :data-ordernum="orderNumber" hover-class="none">付款</button>
          <button v-if="status==3 || status==5" class="button" @tap="toDeliveryPage" :data-ordernum="orderNumber" hover-class="none">查看物流</button>
          <button v-if="status==3" class="button warn" @tap="onConfirmReceive" :data-ordernum="orderNumber" hover-class="none">确认收货</button>
        </view>
      </view> -->
    </view>

    <!-- 订单信息 -->
    <view class="order-msg">
      <view class="msg-item">
        <view class="item">
          <text class="item-tit">订单编号：</text>
          <text class="item-txt">{{orderNumber}}</text>
          <!-- <text class="copy-btn" @tap="copyBtn">复制</text> -->
        </view>
        <view class="item">
          <text class="item-tit">下单时间：</text>
          <text class="item-txt">{{createTime}}</text>
        </view>
      </view>
      <view class="msg-item">
        <view class="item">
          <text class="item-tit">支付方式：</text>
          <text class="item-txt">微信支付</text>
        </view>
        <view class="item">
          <text class="item-tit">配送方式：</text>
          <text class="item-txt">普通配送</text>
        </view>
        <view class="item">
          <text class="item-tit">订单备注：</text>
          <text class="item-txt remarks">{{remarks}}</text>
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
          <view class="item-tit">优惠券：</view>
          <view class="item-txt price">
            <text class="symbol">-￥</text>
            <text class="big-num">{{wxs.parsePrice(reduceAmount)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(reduceAmount)[1]}}</text>
          </view>
        </view>
        <view class="item payment">
          <view class="item-txt price">
            实付款：
            <text class="symbol">￥</text>
            <text class="big-num">{{wxs.parsePrice(actualTotal)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(actualTotal)[1]}}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部栏 -->
    <view class="order-detail-footer" v-if="status==5||status==6">
      <text class="dele-order" v-if="status==5||status==6" @tap="delOrderList">删除订单</text>
      <!-- <view class="footer-box"> -->
        <!-- <text class="apply-service">联系客服</text> -->
        <!-- <text class="buy-again">再次购买</text> -->
      <!-- </view> -->
    </view>

  </view>
</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/order-detail/order-detail.js
var http = require("../../utils/http.js");

export default {
  data() {
    return {
      orderItemDtos: [],
      remarks: "",
      actualTotal: 0,
      userAddrDto: null,
      orderNumber: "",
      createTime: "",
      status: 0,
      productTotalAmount: '',
      transfee: '',
      reduceAmount: '',
      prodid: '',
      total: 0, // 商品总额
    };
  },

  components: {},
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.loadOrderDetail(options.orderNum);
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
    //跳转商品详情页
    toProdPage: function (e) {
      var prodid = e.currentTarget.dataset.prodid;
      uni.navigateTo({
        url: '/pages/prod/prod?prodid=' + prodid
      });
    },

    /**
     * 加载订单数据
     */
    loadOrderDetail: function (orderNum) {
      uni.showLoading(); //加载订单详情

      var params = {
        url: "/p/myOrder/orderDetail",
        method: "GET",
        data: {
          orderNumber: orderNum
        },
        callBack: res => {
          this.setData({
            orderNumber: orderNum,
            actualTotal: res.actualTotal,
            userAddrDto: res.userAddrDto,
            remarks: res.remarks,
            orderItemDtos: res.orderItemDtos,
            createTime: res.createTime,
            status: res.status,
            productTotalAmount: res.orderItemDtos[0].productTotalAmount,
            transfee: res.transfee,
            reduceAmount: res.reduceAmount,
            total: res.total
          });
          uni.hideLoading();
					console.log("orderDetail",this.userAddrDto)
        }
      };
      http.request(params);
    },
    // 一键复制事件
    copyBtn: function (e) {
      var ths = this;
      uni.setClipboardData({
        //准备复制的数据
        data: ths.orderNumber,
        success: function (res) {
          uni.showToast({
            title: '复制成功'
          });
        }
      });
    },
    //删除已完成||已取消的订单
    delOrderList: function () {
      var ths = this;
      uni.showModal({
        title: '',
        content: '确定要删除此订单吗？',
        confirmColor: "#eb2444",
        success(res) {
          if (res.confirm) {
            uni.showLoading();
            var params = {
              url: "/p/myOrder/" + ths.orderNumber,
              method: "DELETE",
              data: {},
              callBack: function (res) {
                uni.hideLoading();
                uni.showToast({
                  title: res ? res : '删除成功',
                  icon: 'none'
                });
                setTimeout(() => {
                  uni.redirectTo({
                    url: '/pages/orderList/orderList'
                  });
                },1000)
              }
            };
            http.request(params);
          } else if (res.cancel) {
            console.log('用户点击取消');
          }
        }

      });
    }
  }
};
</script>
<style>
@import "./order-detail.css";
</style>
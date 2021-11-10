<template>
<!--pages/delivery-address/delivery-address.wxml-->
<view class="container">
  <view class="main">
    <view class="empty" v-if="addressList.length==0">
      <view class="img">
        <image src="http://jiales.gz-yami.com/addr.png"></image>
      </view>
      <view class="txt">您还没有收货地址</view>
    </view>
    <radio-group class="radio-group">
      <block v-for="(item, index) in addressList" :key="index">
        <view class="address">
          <view class="personal" @tap="selAddrToOrder(item)">
            <view class="info-tit">
              <text class="name">{{item.receiver}}</text>
              <text class="tel">{{item.mobile}}</text>
              <image src="/static/images/icon/revise.png" @tap.stop="toEditAddress" :data-addrid="item.addrId"></image>
            </view>
            <view class="addr">

              <text class="addr-get"> {{item.province}}{{item.city}}{{item.area}}{{item.addr}}</text>
            </view>
          </view>
          <view class="select-btn">
            <view class="box">
              <label @tap="onDefaultAddr" :data-addrid="item.addrId">
                <radio :value="item.prodId" :checked="item.commonAddr==1" color="#eb2444"></radio>设为默认地址</label>
            </view>
          </view>
        </view>
      </block>
    </radio-group>
  </view>
  <view class="footer" @tap="onAddAddr">
    <text>新增收货地址</text>
  </view>
</view>
</template>

<script>
// pages/delivery-address/delivery-address.js
var http = require("../../utils/http.js"); // var config = require("../../utils/config.js");

export default {
  data() {
    return {
      defaultSize: 'mini',
      disabled: false,
      plain: true,
      loading: false,
      addressList: [],
      addAddress: '',
      order: -1,
      item: "",
      selAddress: ""
    };
  },

  components: {},
  props: {},
  onLoad: function (option) {
    if (option.order) {
      this.setData({
        order: option.order
      });
    }
  },
  //加载地址列表
  onShow: function () {
    var ths = this;
    uni.showLoading();
    var params = {
      url: "/p/address/list",
      method: "GET",
      data: {},
      callBack: function (res) {
        //console.log(res)
        ths.setData({
          addressList: res
        });
        uni.hideLoading();
      }
    };
    http.request(params);
  },
  methods: {
    //新增收货地址
    onAddAddr: function (e) {
      uni.navigateTo({
        url: '/pages/editAddress/editAddress'
      });
    },
    //设置为默认地址
    onDefaultAddr: function (e) {
      var addrId = e.currentTarget.dataset.addrid;
      console.log(addrId);
      var ths = this;
      uni.showLoading();
      var params = {
        url: "/p/address/defaultAddr/" + addrId,
        method: "PUT",
        data: {
          addrId: addrId
        },
        callBack: function (res) {
          uni.hideLoading();
        }
      };
      http.request(params);
    },
    // 修改地址
    toEditAddress: function (e) {
      var addrId = e.currentTarget.dataset.addrid;
      uni.navigateTo({
        url: '/pages/editAddress/editAddress?addrId=' + addrId
      });
    },

    /**
     * 选择地址 跳转回提交订单页
     */
    selAddrToOrder: function (item) {
      if (this.order == 0) {
        var pages = getCurrentPages(); //当前页面

        var prevPage = pages[pages.length - 2]; //上一页面
        //直接给上一页面赋值
        prevPage.item = item
        prevPage.selAddress = 'yes'
        uni.navigateBack({
          //返回
          delta: 1
        });
      }
    }
  }
};
</script>
<style>
@import "./delivery-address.css";
</style>

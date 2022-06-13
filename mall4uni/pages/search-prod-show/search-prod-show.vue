<template>
<!--pages/search-prod-show/search-prod-show.wxml-->
<view class="container">

  <!-- 搜索框 -->
  <view class="fixed-box">
    <view class="search-bar">
      <view class="search-box">
        <input placeholder="输入关键字搜索" class="sear-input" :value="prodName" @input="getSearchContent" confirm-type="search" @confirm="toSearchConfirm"></input>
        <image src="/static/images/icon/search.png" class="search-img"></image>
      </view>
      <view class="search-list-img" @tap="changeShowType">
        <image v-if="showType==1" src="/static/images/icon/search-col.png"></image>
        <image v-if="showType==2" src="/static/images/icon/search-col2.png"></image>
      </view>
    </view>
    <view class="tabs">
      <text :class="'tab-item complete ' + (sts==0?'on':'')" @tap="onStsTap" data-sts="0">综合</text>
      <text :class="'tab-item ' + (sts==1?'on':'')" @tap="onStsTap" data-sts="1">销量</text>
      <text :class="'tab-item ' + (sts==2?'on':'')" @tap="onStsTap" data-sts="2">价格</text>
    </view>
  </view>

  <!-- 商品列表 -->
  <view class="prod-list">
    <!-- 横向列表 -->
    <view class="prod-show" v-if="showType==1">
      <view class="hotsale-item-cont">
        <block v-for="(item, index) in searchProdList" :key="index">
          <prod :item="item" sts="6"></prod>
        </block>
      </view>
    </view>

    <!-- 纵向列表 -->
    <view class="cont-item" v-if="showType==2">
      <block v-for="(item, index) in searchProdList" :key="index">
        <view class="show-item" @tap="toProdPage" :data-prodid="item.prodId">
          <view class="more-prod-pic">
            <image :src="item.pic" class="more-pic"></image>
          </view>
          <view class="prod-text-right">
            <view class="prod-text more">{{item.prodName}}</view>
            <view class="cate-prod-info">{{item.praiseNumber}}评价 {{item.positiveRating}}%好评</view>
            <view class="prod-price more">
              <text class="symbol">￥</text>
              <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text>
              <text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text>
            </view>
          </view>
        </view>
      </block>
    </view>
  </view>

  <view v-if="!searchProdList.length" :class="['empty',showType==1? 'empty-top':'']">暂无结果</view>
</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/search-prod-show/search-prod-show.js
var http = require("../../utils/http.js");
import prod from "../../components/production/production";

export default {
  data() {
    return {
      sts: 0,
      showType: 2,
      searchProdList: [],
      prodName: ""
    };
  },

  components: {
    prod
  },
  props: {},

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
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.toLoadData();
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
    changeShowType: function () {
      var showType = this.showType;

      if (showType == 1) {
        showType = 2;
      } else {
        showType = 1;
      }

      this.setData({
        showType: showType
      });
    },
    //输入商品获取数据
    getSearchContent: function (e) {
      this.setData({
        prodName: e.detail.value
      });
    },
    //请求商品接口
    toLoadData: function () {
      var ths = this; //热门搜索

      var params = {
        url: "/search/searchProdPage",
        method: "GET",
        data: {
          current: 1,
          prodName: this.prodName,
          size: 10,
          sort: this.sts
        },
        callBack: function (res) {
          ths.setData({
            searchProdList: res.records
          });
        }
      };
      http.request(params);
    },
    //当前搜索页二次搜索商品
    toSearchConfirm: function (e) {
      // this.toLoadData();
      if (e.detail.value) {
				let recentSearch = uni.getStorageSync('recentSearch') || [];
				recentSearch = recentSearch.filter(item => item !== this.prodName);
				recentSearch.unshift(this.prodName);
				if (recentSearch.length > 10) {
				  recentSearch.pop();
				}
				uni.setStorageSync('recentSearch', recentSearch);
			}
      uni.redirectTo({
        url: '/pages/search-prod-show/search-prod-show?prodName=' + e.detail.value
      })
    },

    /**
    * 状态点击事件
    */
    onStsTap: function (e) {
      var sts = e.currentTarget.dataset.sts;
      this.setData({
        sts: sts
      });
      this.toLoadData();
    },
    toProdPage: function (e) {
      var prodid = e.currentTarget.dataset.prodid;
      uni.navigateTo({
        url: '/pages/prod/prod?prodid=' + prodid
      });
    }
  }
};
</script>
<style>
@import "./search-prod-show.css";
</style>

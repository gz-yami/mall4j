<template>
<!--pages/search-page/search-page.wxml-->
<view class="container">

  <!-- 搜索框 -->
  <view class="search-bar">
    <view class="search-box">
      <input placeholder="输入关键字搜索" class="sear-input" confirm-type="search" @confirm="toSearchProdPage" @input="getSearchContent" :value="prodName"></input>
      <image src="/static/images/icon/search.png" class="search-img"></image>
    </view>
    <text class="search-hint" @tap="goBackIndex">取消</text>
  </view>

  <view class="search-display">
    <!-- 热门搜索 -->
    <view class="hot-search">
      <view class="title-text">
        热门搜索
      </view>
      <view v-if="hotSearchList && hotSearchList.length" class="hot-search-tags">
       <block v-for="(item, index) in hotSearchList" :key="index">
        <text class="tags" @tap="onHistSearch" :data-name="item.content">{{item.title}}</text>
        </block> 
      </view>
      <view v-else class="search-tit-empty">暂无数据</view>
    
    </view>

    <!-- 搜索历史 -->
    <view v-if="recentSearch && recentSearch.length" class="history-search">
      <view class="title-text history-line">
        搜索历史
        <view class="clear-history">
          <image src="/static/images/icon/clear-his.png" @tap="clearSearch"></image>
        </view>
      </view>
      <block v-for="(item, index) in recentSearch" :key="index">
        <view class="his-search-tags">
          <text class="tags" @tap="onHistSearch" :data-name="item">{{item}}</text>
        </view>
      </block>
    </view>
  </view>
</view>
</template>

<script>
// pages/search-page/search-page.js
var http = require("../../utils/http.js");

export default {
  data() {
    return {
      hotSearchList: [],
      prodName: "",
      recentSearch: []
    };
  },

  components: {},
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var ths = this; //热门搜索

    var params = {
      url: "/search/hotSearchByShopId",
      method: "GET",
      data: {
        number: 10,
        shopId: 1,
        sort: 1
      },
      callBack: function (res) {
        ths.setData({
          hotSearchList: res
        });
      }
    };
    http.request(params); // 获取历史搜索

    this.getRecentSearch();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
	this.prodName = ''
  },

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
    /**
      * 获取历史搜索
      */
    getRecentSearch: function () {
      let recentSearch = uni.getStorageSync('recentSearch');
      this.setData({
        recentSearch
      });
    },

    /**
     * 搜索提交
     */
    toSearchProdPage: function () {
      if (this.prodName.trim()) {
        // 记录最近搜索
        let recentSearch = uni.getStorageSync('recentSearch') || [];
        recentSearch = recentSearch.filter(item => item !== this.prodName);
        recentSearch.unshift(this.prodName);

        if (recentSearch.length > 10) {
          recentSearch.pop();
        }

        uni.setStorageSync('recentSearch', recentSearch); // 跳转到商品列表页

        uni.navigateTo({
          url: '/pages/search-prod-show/search-prod-show?prodName=' + this.prodName
        });
      }
    },

    /**
     * 清空搜索历史
     */
    clearSearch: function () {
      uni.removeStorageSync('recentSearch');
      this.getRecentSearch();
    },
    // 返回首页
    goBackIndex: function () {
      uni.navigateBack({// url: '/pages/search-page/search-page',
      });
    },
    //输入商品名获取数据 || 绑定输入值
    getSearchContent: function (e) {
      this.setData({
        prodName: e.detail.value
      }); // this.data.prodName=e.detail.value
    },
    //点击搜素历史
    onHistSearch: function (e) {
      var name = e.currentTarget.dataset.name;
      this.setData({
        prodName: name
      });
      this.toSearchProdPage();
    }
  }
};
</script>
<style>
@import "./search-page.css";
</style>
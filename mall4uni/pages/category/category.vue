<template>
  <!--pages/category/category.wxml-->
  
  <view class="container">
  <!-- 头部搜索区 -->
    <view class="search-bar">
      <view class="search-box" @tap="toSearchPage">
        <image src="/static/images/icon/search.png" class="search-img"></image>
        <text class="sear-input">搜索您想要的商品</text>
      </view>
    </view>
  <!-- 滚动内容区 -->
    <view class="main">
      <!-- 左侧菜单start -->
      <scroll-view scroll-y="true" class="leftmenu">
        <block v-for="(item, index) in categoryList" :key="index">
          <view :class="'menu-item ' + (selIndex==index?'active':'') + ' '" :data-index="index" :data-id="item.categoryId" @tap="onMenuTab">
            {{item.categoryName}}
          </view>
        </block>
        <view v-if="!categoryList || !categoryList.length" class="ca-empty">{{categoryList && categoryList.length ? '该分类下暂无商品' : '暂无商品'}}</view>
      </scroll-view>
      <!-- 左侧菜单end -->
  
      <!-- 右侧内容start -->
      <scroll-view scroll-y="true" class="rightcontent">
        <view class="adver-map">
          <view class="item-a">
              <image :src="categoryImg" mode="widthFix"></image>
          </view>
        </view>
      <!-- 子分类 -->
      <view class="th-cate-con" v-if="subCategoryList.length">
        <block v-for="(thCateItem, index) in subCategoryList" :key="index">
            <view class="sub-category">
              <view
                class="sub-category-item"
                :data-categoryid="thCateItem.categoryId"
                :data-parentid="thCateItem.parentId"
                @tap="toCatePage"
              >
                <image :src="thCateItem.pic" class="more-pic" mode="widthFix"></image>
                <text>{{ thCateItem.categoryName }}</text>
              </view>
            </view>
        </block>
      </view>
        <view v-else class="cont-item empty">该分类下暂无子分类~</view>
      </scroll-view>
      <!-- 右侧内容end -->
    </view>
  
  </view>
  </template>
  
  <script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>
  
  <script>
  var http = require("../../utils/http.js");
  var config = require("../../utils/config.js");
  
  export default {
    data() {
      return {
        selIndex: 0,
        categoryList: [],
      subCategoryList: [],
        productList: [],
        categoryImg: '',
        parentId: ''
      };
    },
    components: {},
    props: {},
  
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      var ths = this; //加载分类列表
  
      var params = {
        url: "/category/categoryInfo",
        method: "GET",
        data: {
          parentId: ''
        },
        callBack: function (res) {
          ths.setData({
            categoryImg: res[0].pic,
            categoryList: res
          });
          ths.getProdList(res[0].categoryId);
      ths.parentId = ths.categoryList[0].categoryId
        }
      };
      http.request(params);
    },
    methods: {
      /**
       * 分类点击事件
       */
      onMenuTab: function (e) {
        var id = e.currentTarget.dataset.id;
        var index = e.currentTarget.dataset.index; // this.getProdList(id);
  
        this.getProdList(this.categoryList[index].categoryId);
      this.parentId = this.categoryList[index].categoryId
        this.setData({
          categoryImg: this.categoryList[index].pic,
          selIndex: index
        });
      },
      // 跳转搜索页
      toSearchPage: function () {
        uni.navigateTo({
          url: '/pages/search-page/search-page'
        });
      },
  
      getProdList(categoryId) {
        //加载分类列表
        var params = {
          url: "/category/categoryInfo",
          method: "GET",
          data: {
            parentId: categoryId
          },
          callBack: res => {
            this.setData({
              subCategoryList: res
            });
          }
        };
        http.request(params);
      },
  
    // 跳转子分类商品页面
    toCatePage: function(e) {
      const {
        parentid,
        categoryid,
      } = e.currentTarget.dataset;
      uni.navigateTo({
        url: `/pages/sub-category/sub-category?parentId=${this.parentId}&categoryId=${categoryid}`
      });
    }
    }
  };
  </script>
  <style>
  @import "./category.css";
  </style>
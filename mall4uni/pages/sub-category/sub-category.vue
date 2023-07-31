<template>
  <view class="Mall4j container">
    <!-- 顶部子分类tab -->
    <scroll-view scroll-x="true" class="category-tit" :scroll-into-view="intoView" :scroll-with-animation="true">
      <block v-for="(item, index) in subCategoryList" :key="index">
        <view :id="'sw' + item.categoryId" :class="'category-item ' + (item.categoryId==categoryId? 'on':'')" :data-id="item.categoryId" @tap="onSubCategoryTap">{{ item.categoryName }}</view>
      </block>
    </scroll-view>
    <!-- 商品列表 -->
    <view class="prod-item" v-if="prodList.length">
      <block v-for="(prod, key) in prodList" :key="key">
       <view class="prod-items" @tap="toProdPage" :data-prodid="prod.prodId">
         <view class="hot-imagecont">
           <image :src="prod.pic" class="hotsaleimg"></image>
         </view>
         <view class="hot-text">
           <view class="hotprod-text">{{prod.prodName}}</view>
           <view class="prod-info">{{prod.brief}}</view>
           <view class="prod-text-info">
             <view class="price">
               <text class="symbol">￥</text>
               <text class="big-num">{{wxs.parsePrice(prod.price)[0]}}</text>
               <text class="small-num">.{{wxs.parsePrice(prod.price)[1]}}</text>
             </view>
           </view>
         </view>
       </view>
      </block>
    </view>
	<view v-else class="empty-wrap">
		暂无商品数据~
	</view>
  </view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
var http = require('../../utils/http.js')

export default {
  data() {
    return {
      isLoaded: false, // 列表是否加载完毕
      subCategoryList: [],
      categoryId: 0,
      prodList: [],
      parentId: '',
      current: 1,
      pages: 0,
      intoView: ''
    }
  },

  computed: {
    i18n() {
      return this.$t('index')
    }
  },

	/**
	 * 生命周期函数--监听页面加载
	 */
  onLoad: function(options) {
    this.setData({
      parentId: options.parentId,
      categoryId: options.categoryId
    })
    this.getSubCategory()
    this.getProdList()
  },
	/**
	 * 页面上拉触底事件的处理函数
	 */
  onReachBottom: function() {
    if (this.current < this.pages) {
      this.current = this.current + 1
      this.getProdList()
    }
  },
  methods: {
    /**
	 * 获取顶栏子分类数据
	 */
    getSubCategory() {
      var params = {
        url: '/category/categoryInfo',
        method: 'GET',
        data: {
          parentId: this.parentId
        },
        callBack: res => {
          this.setData({
            subCategoryList: res
          })
          this.$nextTick(() => {
            this.intoView = 'sw' + this.categoryId
          })
        }
      }
      http.request(params)
    },

    /**
	 * 根据分类id获取商品列表数据
	 */
    getProdList() {
      this.isLoaded = false
      var params = {
        url: "/prod/pageProd",
        method: 'GET',
        data: {
			categoryId: this.categoryId,
			current: this.current,
			size: 10,
			sort: 0,
			isAllProdType: true
        },
        callBack: res => {
          this.isLoaded = true
          this.setData({
            prodList: res.current == 1 ? res.records : this.prodList.concat(res.records),
            current: res.current,
            pages: res.pages
          })
        }
      }
      http.request(params)
    },

    /**
	 * 切换顶部分类
	 */
    onSubCategoryTap(e) {
      this.setData({
        categoryId: e.currentTarget.dataset.id,
        current: 1,
        pages: 0,
        intoView: 'sw' + e.currentTarget.dataset.id
      })
      this.getProdList()
    },
	/**
	 * 跳转商品下详情
	 */
	toProdPage: function (e) {
	  let prodid = e.currentTarget.dataset.prodid;
	  if (prodid) {
	    uni.navigateTo({
	      url: '/pages/prod/prod?prodid=' + prodid
	    });
	  }
	},
  }
}
</script>
<style>
	@import "./sub-category.css";
</style>

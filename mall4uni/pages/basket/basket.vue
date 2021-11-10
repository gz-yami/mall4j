<template>
<!--pages/basket/basket.wxml-->
<view class="container">
  <view class="prod-list">
    <block v-for="(item, scIndex) in shopCartItemDiscounts" :key="scIndex">
      <view class="prod-block">
        <view class="discount-tips" v-if="item.chooseDiscountItemDto">
          <text class="text-block">{{wxs.parseDiscount(item.chooseDiscountItemDto.discountRule)}}</text>
          <text class="text-list">{{wxs.parseDiscountMsg(item.chooseDiscountItemDto.discountRule,item.chooseDiscountItemDto.needAmount,item.chooseDiscountItemDto.discount)}}</text>
        </view>
        <block v-for="(prod, index) in item.shopCartItems" :key="index">
          <view class="item">
            <view class="btn">
              <label>
                <checkbox @tap="onSelectedItem" :data-scindex="scIndex" :data-index="index" :value="toString(prod.prodId)" :checked="prod.checked" color="#105c3e"></checkbox>
              </label>
            </view>
            <view class="prodinfo">
              <view class="pic">
                <image :src="prod.pic"></image>
              </view>
              <view class="opt">
                <view class="prod-name">{{prod.prodName}}</view>
                <text :class="'prod-info-text ' + (prod.skuName?'':'empty-n')">{{prod.skuName}}</text>
                <view class="price-count">
                  <view class="price">
                    <text class="symbol">￥</text>
                    <text class="big-num">{{wxs.parsePrice(prod.price)[0]}}</text>
                    <text class="small-num">.{{wxs.parsePrice(prod.price)[1]}}</text>
                  </view>
                  <view class="m-numSelector">
                    <view @tap="onCountMinus" class="minus" :data-scindex="scIndex" :data-index="index"></view>
                    <input type="number" :value="prod.prodCount" disabled></input>
                    <view @tap="onCountPlus" class="plus" :data-scindex="scIndex" :data-index="index"></view>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </block>

      </view>
    </block>

    <!-- <view class='lose-efficacy'>
      <view class='discount-tips'>
        
        <text class='text-list'>失效商品</text>
        <text class='empty-prod'>清空失效商品</text>
      </view>
      <view class='item'>
        <view class="staus">
          <text>失效</text>
        </view>
        <view class='prodinfo'>
          <view class="pic">
            <image src='../../images/prod/pic11.jpg' />
          </view>
          <view class="opt">
            <view class='prod-name'>宠物用品洗澡香波沐浴除臭留</view>
            <view class='prod-info-text'>60克</view>
            <view class='price-count'>
              <view class='price'>
                <text class='symbol'>￥</text>
                <text class='big-num'>{{wxs.parsePrice(10.00)[0]}}</text>
                <text class='small-num'>.{{wxs.parsePrice(10.00)[1]}}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view> -->

  </view>

  <view class="empty" v-if="shopCartItemDiscounts.length==0">
    <view class="img">
      <image src="/static/images/tabbar/basket.png"></image>
    </view>
    <view class="txt">
      您还没有添加任何商品哦~
    </view>
  </view>

  <!-- 底部按钮 -->
  <view class="cart-footer" v-if="shopCartItemDiscounts.length>0">
    <view class="btn all">
      <label @tap="onSelAll">
        <checkbox :checked="allChecked" color="#f7d731;"></checkbox>全选</label>
    </view>
    <view class="btn del" @tap="onDelBasket">
      <text>删除</text>
    </view>
    <view class="btn total">
      <view class="finally">
        <text>合计:</text>
        <view class="price">
          <text class="symbol">￥</text>
          <text class="big-num">{{wxs.parsePrice(finalMoney)[0]}}</text>
          <text class="small-num">.{{wxs.parsePrice(finalMoney)[1]}}</text>
        </view>
      </view>
      <view class="total-msg" v-if="subtractMoney>0">
        总额:￥{{wxs.toPrice(totalMoney)}} 立减:￥{{wxs.toPrice(subtractMoney)}}
      </view>
    </view>
    <view class="btn settle" @tap="toFirmOrder">
      <text>结算</text>
    </view>
  </view>
  <!-- end 底部按钮 -->

</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/basket/basket.js
var http = require("../../utils/http.js"); // var config = require("../../utils/config.js");
// var config = require("../../utils/config.js");
const Big = require("../../utils/big.min.js");

export default {
  data() {
    return {
      // picDomain: config.picDomain,
      shopCartItemDiscounts: [],
      finalMoney: 0,
      totalMoney: 0,
      subtractMoney: 0,
      allChecked: false
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
    this.loadBasketData();
    http.getCartCount(); //重新计算购物车总数量
  },
  methods: {
    loadBasketData(){
			uni.showLoading(); //加载购物车
			
			var params = {
			  url: "/p/shopCart/info",
			  method: "POST",
			  data: {},
			  callBack: res => {
			    if (res.length > 0) {
			      // 默认不选中
			      var shopCartItemDiscounts = res[0].shopCartItemDiscounts;
			      shopCartItemDiscounts.forEach(shopCartItemDiscount => {
			        shopCartItemDiscount.shopCartItems.forEach(shopCartItem => {
			          shopCartItem.checked = false;
			        });
			      });
			      this.setData({
			        shopCartItemDiscounts: shopCartItemDiscounts,
			        allChecked: false
			      });
			    } else {
			      this.setData({
			        shopCartItemDiscounts: []
			      });
			    }
			
			    this.calTotalPrice(); //计算总价
			
			    uni.hideLoading();
			  }
			};
			http.request(params);
		},
    /**
     * 去结算
     */
    toFirmOrder: function () {
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      var basketIds = [];
      shopCartItemDiscounts.forEach(shopCartItemDiscount => {
        shopCartItemDiscount.shopCartItems.forEach(shopCartItem => {
          if (shopCartItem.checked) {
            basketIds.push(shopCartItem.basketId);
          }
        });
      });

      if (!basketIds.length) {
        uni.showToast({
          title: '请选择商品',
          icon: "none"
        });
        return;
      }

      uni.setStorageSync("basketIds", JSON.stringify(basketIds));
      uni.navigateTo({
        url: '/pages/submit-order/submit-order?orderEntry=0'
      });
    },

    /**
     * 全选
     */
    onSelAll: function () {
      var allChecked = this.allChecked;
      allChecked = !allChecked; //改变状态

      var shopCartItemDiscounts = this.shopCartItemDiscounts;

      for (var i = 0; i < shopCartItemDiscounts.length; i++) {
        var cItems = shopCartItemDiscounts[i].shopCartItems;

        for (var j = 0; j < cItems.length; j++) {
          cItems[j].checked = allChecked;
        }
      }

      this.setData({
        allChecked: allChecked,
        shopCartItemDiscounts: shopCartItemDiscounts
      });
      this.calTotalPrice(); //计算总价
    },

    /**
     * 每一项的选择事件
     */
    onSelectedItem: function (e) {
      var index = e.currentTarget.dataset.index; // 获取data- 传进来的index

      var scindex = e.currentTarget.dataset.scindex;
      var shopCartItemDiscounts = this.shopCartItemDiscounts; // 获取购物车列表

      var checked = shopCartItemDiscounts[scindex].shopCartItems[index].checked; // 获取当前商品的选中状态

      shopCartItemDiscounts[scindex].shopCartItems[index].checked = !checked; // 改变状态

      this.setData({
        shopCartItemDiscounts: shopCartItemDiscounts
      });
      this.checkAllSelected(); //检查全选状态

      this.calTotalPrice(); //计算总价
    },

    /**
     * 检查全选状态
     */
    checkAllSelected: function () {
      var allChecked = true;
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      var flag = false;

      for (var i = 0; i < shopCartItemDiscounts.length; i++) {
        var cItems = shopCartItemDiscounts[i].shopCartItems;

        for (var j = 0; j < cItems.length; j++) {
          if (!cItems[j].checked) {
            allChecked = !allChecked;
            flag = true;
            break;
          }
        }

        if (flag) {
          break;
        }
      }

      this.setData({
        allChecked: allChecked
      });
    },

    /**
     * 计算购物车总额
     */
    calTotalPrice: function () {
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      var shopCartIds = [];

      for (var i = 0; i < shopCartItemDiscounts.length; i++) {
        var cItems = shopCartItemDiscounts[i].shopCartItems;

        for (var j = 0; j < cItems.length; j++) {
          if (cItems[j].checked) {
            shopCartIds.push(cItems[j].basketId);
          }
        }
      }

      var ths = this;
      uni.showLoading();
      var params = {
        url: "/p/shopCart/totalPay",
        method: "POST",
        data: shopCartIds,
        callBack: function (res) {
          ths.setData({
            finalMoney: res.finalMoney,
            totalMoney: res.totalMoney,
            subtractMoney: res.subtractMoney
          });
          uni.hideLoading();
        }
      };
      http.request(params);
    },

    /**
     * 减少数量
     */
    onCountMinus: function (e) {
      var index = e.currentTarget.dataset.index;
      var scindex = e.currentTarget.dataset.scindex;
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      var prodCount = shopCartItemDiscounts[scindex].shopCartItems[index].prodCount;

      if (prodCount > 1) {
        this.updateCount(shopCartItemDiscounts, scindex, index, -1);
      }
    },

    /**
     * 增加数量
     */
    onCountPlus: function (e) {
      var index = e.currentTarget.dataset.index;
      var scindex = e.currentTarget.dataset.scindex;
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      this.updateCount(shopCartItemDiscounts, scindex, index, 1);
    },

    /**
     * 改变购物车数量接口
     */
    updateCount: function (shopCartItemDiscounts, scindex, index, prodCount) {
      var ths = this;
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/shopCart/changeItem",
        method: "POST",
        data: {
          count: prodCount,
          prodId: shopCartItemDiscounts[scindex].shopCartItems[index].prodId,
          skuId: shopCartItemDiscounts[scindex].shopCartItems[index].skuId,
          shopId: 1
        },
        callBack: function (res) {
          shopCartItemDiscounts[scindex].shopCartItems[index].prodCount += prodCount;
          ths.setData({
            shopCartItemDiscounts: shopCartItemDiscounts
          });
          ths.calTotalPrice(); //计算总价

          uni.hideLoading();
          http.getCartCount(); //重新计算购物车总数量
        }
      };
      http.request(params);
    },

    /**
     * 删除购物车商品
     */
    onDelBasket: function () {
      var ths = this;
      var shopCartItemDiscounts = this.shopCartItemDiscounts;
      var basketIds = [];

      for (var i = 0; i < shopCartItemDiscounts.length; i++) {
        var cItems = shopCartItemDiscounts[i].shopCartItems;

        for (var j = 0; j < cItems.length; j++) {
          if (cItems[j].checked) {
            basketIds.push(cItems[j].basketId);
          }
        }
      }

      if (basketIds.length == 0) {
        uni.showToast({
          title: '请选择商品',
          icon: "none"
        });
      } else {
        uni.showModal({
          title: '',
          content: '确认要删除选中的商品吗？',
          confirmColor: "#eb2444",

          success(res) {
            if (res.confirm) {
              uni.showLoading({
                mask: true
              });
              var params = {
                url: "/p/shopCart/deleteItem",
                method: "DELETE",
                data: basketIds,
                callBack: function (res) {
                  uni.hideLoading();
                  ths.loadBasketData();
                }
              };
              http.request(params);
            }
          }

        });
      }
    }
  }
};
</script>
<style>
@import "./basket.css";
</style>
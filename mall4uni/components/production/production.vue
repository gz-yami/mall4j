<template>
<view class="prod-items" @tap="toProdPage" :data-prodid="item.prodId">
      <view class="hot-imagecont">
        <image :src="item.pic" class="hotsaleimg"></image>
      </view>
      <view class="hot-text">
        <view class="hotprod-text">{{item.prodName}}</view>
        <view class="prod-info" v-if="sts==6">{{item.prodCommNumber}}评价 {{item.positiveRating}}%好评</view>
        <view class="prod-text-info">
          <view class="price">
            <text v-if="sts==2" class="deadline-price">限时价</text>
            <text class="symbol">￥</text>
            <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text>
          </view>
        </view>
      </view>
    </view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>

export default {
  data() {
    return {};
  },

  components: {},
  props: {
    item: Object,
    sts: Number
  },
  methods: {
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
@import "./production.css";
</style>
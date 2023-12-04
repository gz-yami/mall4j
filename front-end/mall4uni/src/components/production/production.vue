<template>
  <view
    class="prod-items"
    :data-prodid="item.prodId"
    @tap="toProdPage"
  >
    <view class="hot-imagecont">
      <image
        :src="item.pic"
        class="hotsaleimg"
      />
    </view>
    <view class="hot-text">
      <view class="hotprod-text">
        {{ item.prodName }}
      </view>
      <view
        v-if="sts===6"
        class="prod-info"
      >
        {{ item.prodCommNumber }}评价 {{ item.positiveRating }}%好评
      </view>
      <view class="prod-text-info">
        <view class="price">
          <text
            v-if="sts===2"
            class="deadline-price"
          >
            限时价
          </text>
          <text class="symbol">
            ￥
          </text>
          <text class="big-num">
            {{ wxs.parsePrice(item.price)[0] }}
          </text>
          <text class="small-num">
            .{{ wxs.parsePrice(item.price)[1] }}
          </text>
        </view>
      </view>
    </view>
  </view>
</template>
<script setup>
const wxs = number()
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  item: {
    type: Object,
    default: () => {
      return null
    }
  },
  sts: {
    type: Number,
    default: () => {
      return 2
    }
  }
})

const toProdPage = (e) => {
  const prodid = e.currentTarget.dataset.prodid
  uni.navigateTo({
    url: '/pages/prod/prod?prodid=' + prodid
  })
}
</script>

<style scoped lang="scss">
@use './production.scss';
</style>

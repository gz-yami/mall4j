<template>
  <!-- 物流信息 -->
  <view class="container">
    <view class="wrapper">
      <view
        class="deliveryInfo"
        style="background:url(http://jiales.gz-yami.com/delivery-bg.png) center center no-repeat #fff;"
      >
        <view
          class="icon-express"
          style="background:url(http://jiales.gz-yami.com/delivery-car.png) no-repeat;background-size:100% 100%;"
        />
        <view class="infoWarp">
          <view class="companyname">
            <text class="key">
              物流公司：
            </text>
            <text class="value">
              {{ companyName }}
            </text>
          </view>
          <view class="expno">
            <text class="key">
              运单编号：
            </text>
            <text class="value">
              {{ dvyFlowId }}
            </text>
          </view>
        </view>
      </view>
      <view
        v-if="dvyData.length"
        class="deliveryDetail"
      >
        <block
          v-for="(item, index) in dvyData"
          :key="index"
        >
          <view :class="'detailItem ' + (index==0?'lastest':'')">
            <view class="dot">
              <image src="@/static/images/icon/delive-dot.png" />
              <image src="@/static/images/icon/dot.png" />
            </view>
            <view class="detail">
              <view class="desc">
                {{ item.context }}
              </view>
              <view class="time">
                {{ item.time }}
              </view>
            </view>
          </view>
        </block>
      </view>
      <view
        v-else
        class="empty-space"
      >
        暂无配送信息
      </view>
    </view>
  </view>
</template>

<script setup>
const companyName = ref('')
const dvyFlowId = ref('')
const dvyData = ref([])
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  uni.showLoading()
  http.request({
    url: '/delivery/check',
    method: 'GET',
    data: {
      orderNumber: options.orderNum
    }
  })
    .then(({ data }) => {
      companyName.value = data.companyName
      dvyFlowId.value = data.dvyFlowId
      dvyData.value = data.data
      uni.hideLoading()
    })
})
</script>

<style scoped lang="scss">
@use './express-delivery.scss';
</style>

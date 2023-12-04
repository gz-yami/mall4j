<template>
  <view class="container">
    <view v-if="sts == 0">
      <view class="pay-sts fail">
        支付失败
      </view>
      <view class="tips">
        请在
        <text class="warn">
          30分钟
        </text>内完成付款
      </view>
      <view class="tips">
        否则订单会被系统取消
      </view>
      <view class="btns">
        <text
          class="button checkorder"
          @tap="toOrderList"
        >
          查看订单
        </text>
        <text
          class="button payagain"
          @tap="payAgain"
        >
          重新支付
        </text>
      </view>
    </view>

    <view v-if="sts == 1">
      <view class="pay-sts succ">
        支付成功
      </view>
      <view class="tips">
        感谢您的购买
      </view>
      <view class="btns">
        <text
          class="button checkorder"
          @tap="toOrderList"
        >
          查看订单
        </text>
        <text
          class="button shopcontinue"
          @tap="toIndex"
        >
          继续购物
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
const sts = ref(0)
const orderNumbers = ref('')
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  sts.value = options.sts
  orderNumbers.value = options.orderNumbers
})

const toOrderList = () => {
  uni.navigateTo({
    url: '/pages/orderList/orderList?sts=0'
  })
}
const toIndex = () => {
  uni.switchTab({
    url: '/pages/index/index'
  })
}
const payAgain = () => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/order/pay',
    method: 'POST',
    data: {
      payType: 1,
      orderNumbers: orderNumbers.value
    }
  })
    .then(({ data }) => {
      uni.hideLoading()
      uni.requestPayment({
        timeStamp: data.timeStamp,
        nonceStr: data.nonceStr,
        package: data.packageValue,
        signType: data.signType,
        paySign: data.paySign,
        success: () => {
          uni.redirectTo({
            url: '/pages/pay-result/pay-result?sts=1&orderNum=' + orderNumbers.value
          })
        }
      })
    })
}
</script>

<style scoped lang="scss">
@use './pay-result.scss';
</style>

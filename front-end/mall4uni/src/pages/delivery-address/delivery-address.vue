<template>
  <view class="container">
    <view class="main">
      <view
        v-if="addressList.length===0"
        class="empty"
      >
        <view class="img">
          <image src="http://jiales.gz-yami.com/addr.png" />
        </view>
        <view class="txt">
          您还没有收货地址
        </view>
      </view>
      <radio-group class="radio-group">
        <block
          v-for="(item, index) in addressList"
          :key="index"
        >
          <view class="address">
            <view
              class="personal"
              @tap="selAddrToOrder(item)"
            >
              <view class="info-tit">
                <text class="name">
                  {{ item.receiver }}
                </text>
                <text class="tel">
                  {{ item.mobile }}
                </text>
                <image
                  src="@/static/images/icon/revise.png"
                  :data-addrid="item.addrId"
                  @tap.stop="toEditAddress"
                />
              </view>
              <view class="addr">
                <text class="addr-get">
                  {{ item.province }}{{ item.city }}{{ item.area }}{{ item.addr }}
                </text>
              </view>
            </view>
            <view
              class="select-btn"
              :data-addrid="item.addrId"
              @tap="onDefaultAddr"
            >
              <view class="box">
                <radio
                  :value="item.prodId"
                  :checked="item.commonAddr==1"
                  color="#eb2444"
                  :data-addrid="item.addrId"
                  @tap="onDefaultAddr"
                />
                设为默认地址
              </view>
            </view>
          </view>
        </block>
      </radio-group>
    </view>
    <view
      class="footer"
      @tap="onAddAddr"
    >
      <text>新增收货地址</text>
    </view>
  </view>
</template>

<script setup>
const order = ref(-1)
onLoad((option) => {
  if (option.order) {
    order.value = option.order
  }
})

const addressList = ref([])
/**
 * 加载地址列表
 */
onShow(() => {
  onGetList()
})

/**
 * 获取收获列表接口
 */
const onGetList = () => {
  uni.showLoading()
  http.request({
    url: '/p/address/list',
    method: 'GET'
  })
    .then(({ data }) => {
      addressList.value = data
      uni.hideLoading()
    })
}

/**
 * 新增收货地址
 */
const onAddAddr = () => {
  uni.navigateTo({
    url: '/pages/editAddress/editAddress'
  })
}

/**
 * 设置为默认地址
 */
const onDefaultAddr = (e) => {
  const addrId = e.currentTarget.dataset.addrid
  uni.showLoading()
  http.request({
    url: '/p/address/defaultAddr/' + addrId,
    method: 'PUT',
    data: {
      addrId
    }
  })
    .then(() => {
      uni.hideLoading()
    })
}

/**
 * 修改地址
 */
const toEditAddress = (e) => {
  const addrId = e.currentTarget.dataset.addrid
  uni.navigateTo({
    url: '/pages/editAddress/editAddress?addrId=' + addrId
  })
}

/**
 * 选择地址 跳转回提交订单页
 */
const selAddrToOrder = (item) => {
  if (order.value == 0) {
    const pages = getCurrentPages() // 当前页面
    const prevPage = pages[pages.length - 2] // 上一页面
    prevPage.item = item // 直接给上一页面赋值
    prevPage.selAddress = 'yes'
    // 返回
    uni.navigateBack({
      delta: 1
    })
  }
}
</script>

<style scoped lang="scss">
@use './delivery-address.scss';
</style>

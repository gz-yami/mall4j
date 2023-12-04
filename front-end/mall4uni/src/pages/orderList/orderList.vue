<template>
  <view class="container">
    <!-- 头部菜单 -->
    <view class="order-tit">
      <text
        data-sts="0"
        :class="sts==0?'on':''"
        @tap="onStsTap"
      >
        全部
      </text>
      <text
        data-sts="1"
        :class="sts==1?'on':''"
        @tap="onStsTap"
      >
        待支付
      </text>
      <text
        data-sts="2"
        :class="sts==2?'on':''"
        @tap="onStsTap"
      >
        待发货
      </text>
      <text
        data-sts="3"
        :class="sts==3?'on':''"
        @tap="onStsTap"
      >
        待收货
      </text>
      <text
        data-sts="5"
        :class="sts==5?'on':''"
        @tap="onStsTap"
      >
        已完成
      </text>
    </view>
    <!-- end 头部菜单 -->
    <view class="main">
      <view
        v-if="list.length==0"
        class="empty"
      >
        还没有任何相关订单
      </view>
      <!-- 订单列表 -->
      <block
        v-for="(item, index) in list"
        :key="index"
      >
        <view class="prod-item">
          <view class="order-num">
            <text>订单编号：{{ item.orderNumber }}</text>
            <view class="order-state">
              <text
                :class="'order-sts  ' + (item.status==1?'red':'') + '  ' + ((item.status==5||item.status==6)?'gray':'')"
              >
                {{
                  item.status == 1 ? '待支付' : (item.status == 2 ? '待发货' : (item.status == 3 ? '待收货' : (item.status == 5 ? '已完成' : '已取消')))
                }}
              </text>

              <view
                v-if="item.status==5 || item.status==6"
                class="clear-btn"
              >
                <image
                  src="@/static/images/icon/clear-his.png"
                  class="clear-list-btn"
                  :data-ordernum="item.orderNumber"
                  @tap="delOrderList"
                />
              </view>
            </view>
          </view>

          <!-- 商品列表 -->
          <!-- 一个订单单个商品的显示 -->
          <block v-if="item.orderItemDtos.length==1">
            <block
              v-for="(prod, index2) in item.orderItemDtos"
              :key="index2"
            >
              <view>
                <view
                  class="item-cont"
                  :data-ordernum="item.orderNumber"
                  @tap="toOrderDetailPage"
                >
                  <view class="prod-pic">
                    <image :src="prod.pic" />
                  </view>
                  <view class="prod-info">
                    <view class="prodname">
                      {{ prod.prodName }}
                    </view>
                    <view class="prod-info-cont">
                      {{ prod.skuName }}
                    </view>
                    <view class="price-nums">
                      <text class="prodprice">
                        <text class="symbol">
                          ￥
                        </text>
                        <text class="big-num">
                          {{ wxs.parsePrice(prod.price)[0] }}
                        </text>
                        <text class="small-num">
                          .{{ wxs.parsePrice(prod.price)[1] }}
                        </text>
                      </text>
                      <text class="prodcount">
                        x{{ prod.prodCount }}
                      </text>
                    </view>
                  </view>
                </view>
              </view>
            </block>
          </block>
          <!-- 一个订单多个商品时的显示 -->
          <block v-else>
            <view
              class="item-cont"
              :data-ordernum="item.orderNumber"
              @tap="toOrderDetailPage"
            >
              <scroll-view
                scroll-x="true"
                scroll-left="0"
                scroll-with-animation="false"
                class="categories"
              >
                <block
                  v-for="(prod, index2) in item.orderItemDtos"
                  :key="index2"
                >
                  <view class="prod-pic">
                    <image :src="prod.pic" />
                  </view>
                </block>
              </scroll-view>
            </view>
          </block>

          <view class="total-num">
            <text class="prodcount">
              共1件商品
            </text>
            <view class="prodprice">
              合计：
              <text class="symbol">
                ￥
              </text>
              <text class="big-num">
                {{ wxs.parsePrice(item.actualTotal)[0] }}
              </text>
              <text class="small-num">
                .{{ wxs.parsePrice(item.actualTotal)[1] }}
              </text>
            </view>
          </view>
          <!-- end 商品列表 -->
          <view class="prod-foot">
            <view class="btn">
              <text
                v-if="item.status==1"
                class="button"
                :data-ordernum="item.orderNumber"
                hover-class="none"
                @tap="onCancelOrder"
              >
                取消订单
              </text>
              <text
                v-if="item.status==1"
                class="button warn"
                :data-ordernum="item.orderNumber"
                hover-class="none"
                @tap="normalPay"
              >
                付款
              </text>
              <text
                v-if="item.status==3 || item.status==5"
                class="button"
                :data-ordernum="item.orderNumber"
                hover-class="none"
                @tap="toDeliveryPage"
              >
                查看物流
              </text>
              <text
                v-if="item.status==3"
                class="button warn"
                :data-ordernum="item.orderNumber"
                hover-class="none"
                @tap="onConfirmReceive"
              >
                确认收货
              </text>
            </view>
          </view>
        </view>
      </block>
    </view>
  </view>
  <!-- end 订单列表 -->
</template>

<script setup>
const wxs = number()

const sts = ref(0)
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  if (options.sts) {
    sts.value = options.sts
    loadOrderData(options.sts, 1)
  } else {
    loadOrderData(0, 1)
  }
})

const current = ref(1)
const pages = ref(0)
/**
 * 页面上拉触底事件的处理函数
 */
onReachBottom(() => {
  if (current.value < pages.value) {
    loadOrderData(sts.value, current.value + 1)
  }
})

const list = ref([])
/**
 * 加载订单数据
 */
const loadOrderData = (sts, currentParam) => {
  uni.showLoading() // 加载订单列表
  http.request({
    url: '/p/myOrder/myOrder',
    method: 'GET',
    data: {
      current: currentParam,
      size: 10,
      status: sts
    }
  })
    .then(({ data }) => {
      let listParam = []
      if (data.current === 1) {
        listParam = data.records
      } else {
        listParam = list.value
        Array.prototype.push.apply(listParam, data.records)
      }
      list.value = listParam
      pages.value = data.pages
      current.value = data.current
      uni.hideLoading()
    })
}

/**
 * 状态点击事件
 */
const onStsTap = (e) => {
  sts.value = e.currentTarget.dataset.sts
  loadOrderData(sts.value, 1)
}

/**
 * 查看物流
 */
const toDeliveryPage = (e) => {
  uni.navigateTo({
    url: '/pages/express-delivery/express-delivery?orderNum=' + e.currentTarget.dataset.ordernum
  })
}

/**
 * 取消订单
 */
const onCancelOrder = (e) => {
  const ordernum = e.currentTarget.dataset.ordernum
  uni.showModal({
    title: '',
    content: '要取消此订单？',
    confirmColor: '#3e62ad',
    cancelColor: '#3e62ad',
    cancelText: '否',
    confirmText: '是',

    success (res) {
      if (res.confirm) {
        uni.showLoading({
          mask: true
        })
        http.request({
          url: '/p/myOrder/cancel/' + ordernum,
          method: 'PUT',
          data: {}
        })
          .then(() => {
            loadOrderData(sts.value, 1)
            uni.hideLoading()
          })
      }
    }
  })
}

/**
 * 模拟支付，直接提交成功
 * @param e
 */
const normalPay = (e) => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/order/normalPay',
    method: 'POST',
    data: {
      orderNumbers: e.currentTarget.dataset.ordernum
    }
  })
    .then(({ data }) => {
      uni.hideLoading()
      if (data) {
        uni.showToast({
          title: '模拟支付成功',
          icon: 'none'
        })
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + e.currentTarget.dataset.ordernum
          })
        }, 1200)
      } else {
        uni.showToast({
          title: '支付失败！',
          icon: 'none'
        })
      }
    })
}

/**
 * 查看订单详情
 */
const toOrderDetailPage = (e) => {
  uni.navigateTo({
    url: '/pages/order-detail/order-detail?orderNum=' + e.currentTarget.dataset.ordernum
  })
}

/**
 * 确认收货
 */
const onConfirmReceive = (e) => {
  uni.showModal({
    title: '',
    content: '我已收到货？',
    confirmColor: '#eb2444',

    success (res) {
      if (res.confirm) {
        uni.showLoading({
          mask: true
        })
        http.request({
          url: '/p/myOrder/receipt/' + e.currentTarget.dataset.ordernum,
          method: 'PUT'
        })
          .then(() => {
            loadOrderData(sts.value, 1)
            uni.hideLoading()
          })
      }
    }
  })
}

/**
 * 删除已完成||已取消的订单
 * @param e
 */
const delOrderList = (e) => {
  uni.showModal({
    title: '',
    content: '确定要删除此订单吗？',
    confirmColor: '#eb2444',

    success (res) {
      if (res.confirm) {
        const ordernum = e.currentTarget.dataset.ordernum
        uni.showLoading()

        http.request({
          url: '/p/myOrder/' + ordernum,
          method: 'DELETE'
        })
          .then(() => {
            loadOrderData(sts.value, 1)
            uni.hideLoading()
          })
      }
    }
  })
}

</script>

<style scoped lang="scss">
@use './orderList.scss';
</style>

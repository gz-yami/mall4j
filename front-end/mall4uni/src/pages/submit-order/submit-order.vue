<template>
  <view>
    <view class="container">
      <view class="submit-order">
        <!-- 收货地址 -->
        <view
          class="delivery-addr "
          @tap="toAddrListPage"
        >
          <view
            v-if="!userAddr"
            class="addr-bg "
          >
            <view class="add-addr">
              <view class="plus-sign-img">
                <image src="@/static/images/icon/plus-sign.png" />
              </view>
              <text>新增收货地址</text>
            </view>
            <view class="arrow empty" />
          </view>
          <view
            v-if="userAddr"
            class="addr-bg whole"
          >
            <view class="addr-icon">
              <image src="@/static/images/icon/addr.png" />
            </view>
            <view class="user-info">
              <text class="item">
                {{ userAddr.receiver }}
              </text>
              <text class="item">
                {{ userAddr.mobile }}
              </text>
            </view>
            <view class="addr">
              {{ userAddr.province }}{{ userAddr.city }}{{ userAddr.area }}{{ userAddr.addr }}
            </view>
            <view class="arrow" />
          </view>
        </view>

        <!-- 商品详情 -->
        <view class="prod-item">
          <block
            v-for="(item, index) in orderItems"
            :key="index"
          >
            <view
              class="item-cont"
              :data-ordernum="item.primaryOrderNo"
              @tap="toOrderDetailPage"
            >
              <view class="prod-pic">
                <image :src="item.pic" />
              </view>
              <view class="prod-info">
                <view class="prodname">
                  {{ item.prodName }}
                </view>
                <view class="prod-info-cont">
                  {{ item.skuName }}
                </view>
                <view class="price-nums">
                  <text class="prodprice">
                    <text class="symbol">
                      ￥
                    </text>
                    <text class="big-num">
                      {{ wxs.parsePrice(item.price)[0] }}
                    </text>
                    <text class="small-num">
                      .{{ wxs.parsePrice(item.price)[1] }}
                    </text>
                  </text>
                  <text class="prodcount">
                    x{{ item.prodCount }}
                  </text>
                </view>
              </view>
            </view>
          </block>

          <view class="total-num">
            <text class="prodcount">
              共{{ totalCount }}件商品
            </text>
            <view class="prodprice">
              合计：
              <text class="symbol">
                ￥
              </text>
              <text class="big-num">
                {{ wxs.parsePrice(total)[0] }}
              </text>
              <text class="small-num">
                .{{ wxs.parsePrice(total)[1] }}
              </text>
            </view>
          </view>
        </view>

        <!-- 订单详情 -->
        <view class="order-msg">
          <view class="msg-item">
            <view
              class="item coupon"
              @tap="showCouponPopup"
            >
              <text class="item-tit">
                优惠券：
              </text>
              <text
                v-if="!coupons.canUseCoupons"
                class="item-txt"
              >
                暂无可用
              </text>
              <text class="coupon-btn">
                {{ coupons.totalLength ? coupons.totalLength : 0 }}张
              </text>
              <text class="arrow" />
            </view>
            <view class="item">
              <text>买家留言：</text>
              <input
                v-model="remarks"
                placeholder="给卖家留言"
              >
            </view>
          </view>
        </view>

        <view class="order-msg">
          <view class="msg-item">
            <view class="item">
              <view class="item-tit">
                订单总额：
              </view>
              <view class="item-txt price">
                <text class="symbol">
                  ￥
                </text>
                <text class="big-num">
                  {{ wxs.parsePrice(total)[0] }}
                </text>
                <text class="small-num">
                  .{{ wxs.parsePrice(total)[1] }}
                </text>
              </view>
            </view>
            <view class="item">
              <view class="item-tit">
                运费：
              </view>
              <view class="item-txt price">
                <text class="symbol">
                  ￥
                </text>
                <text class="big-num">
                  {{ wxs.parsePrice(transfee)[0] }}
                </text>
                <text class="small-num">
                  .{{ wxs.parsePrice(transfee)[1] }}
                </text>
              </view>
            </view>
            <view class="item">
              <view class="item-tit">
                优惠金额：
              </view>
              <view class="item-txt price">
                <text class="symbol">
                  -￥
                </text>
                <text class="big-num">
                  {{ wxs.parsePrice(shopReduce)[0] }}
                </text>
                <text class="small-num">
                  .{{ wxs.parsePrice(shopReduce)[1] }}
                </text>
              </view>
            </view>
            <view class="item payment">
              <view class="item-txt price">
                小计：
                <text class="symbol">
                  ￥
                </text>
                <text class="big-num">
                  {{ wxs.parsePrice(actualTotal)[0] }}
                </text>
                <text class="small-num">
                  .{{ wxs.parsePrice(actualTotal)[1] }}
                </text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 底部栏 -->
      <view class="submit-order-footer">
        <view class="sub-order">
          <view class="item-txt">
            合计：
            <view class="price">
              <text class="symbol">
                ￥
              </text>
              <text class="big-num">
                {{ wxs.parsePrice(actualTotal)[0] }}
              </text>
              <text class="small-num">
                .{{ wxs.parsePrice(actualTotal)[1] }}
              </text>
            </view>
          </view>
        </view>
        <view
          class="footer-box"
          @tap="toPay"
        >
          提交订单
        </view>
      </view>
    </view>

    <!-- 选择优惠券弹窗 -->
    <view
      v-if="popupShow"
      class="popup-hide"
    >
      <view class="popup-box">
        <view class="popup-tit">
          <text>优惠券</text>
          <text
            class="close"
            @tap="closePopup"
          />
        </view>
        <view class="coupon-tabs">
          <view
            :class="'coupon-tab ' + (couponSts==1?'on':'')"
            data-sts="1"
            @tap="changeCouponSts"
          >
            可用优惠券({{ coupons.canUseCoupons.length ? coupons.canUseCoupons.length : 0 }})
          </view>
          <view
            :class="'coupon-tab ' + (couponSts==2?'on':'')"
            data-sts="2"
            @tap="changeCouponSts"
          >
            不可用优惠券({{ coupons.unCanUseCoupons.length ? coupons.unCanUseCoupons.length : 0 }})
          </view>
        </view>
        <view class="popup-cnt">
          <block v-if="couponSts == 1">
            <view
              v-for="(item, index) in coupons.canUseCoupons"
              :key="index"
            >
              <coupon
                :item="item"
                order="true"
                can-use="true"
                @check-coupon="checkCoupon"
              />
            </view>
          </block>
          <block v-if="couponSts == 2">
            <view
              v-for="(item, index) in coupons.unCanUseCoupons"
              :key="index"
            >
              <coupon
                :item="item"
                order="true"
                can-use="false"
              />
            </view>
          </block>
          <view class="botm-empty" />
        </view>
        <view
          v-if="couponSts==1"
          class="coupon-ok"
        >
          <text @tap="choosedCoupon">
            确定
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
const wxs = number()
let orderEntry = '0' // 订单入口 0购物车 1立即购买
/**
 * 生命周期函数--监听页面加载
 */
onLoad((options) => {
  orderEntry = options.orderEntry
})

const userAddr = ref(null)
/**
 * 生命周期函数--监听页面显示
 */
onShow(() => {
  const pages = getCurrentPages()
  const currPage = pages[pages.length - 1]
  if (currPage.selAddress === 'yes') {
    // 将携带的参数赋值
    userAddr.value = currPage.item
  }
  // 获取订单数据
  loadOrderData()
})

let couponIds = []
const coupons = ref({})
const total = ref(0)
const actualTotal = ref(0)
const orderItems = ref([])
const totalCount = ref(0)
const transfee = ref(0)
const shopReduce = ref('')
/**
 * 加载订单数据
 */
const loadOrderData = () => {
  let addrId = 0
  if (userAddr.value != null) {
    addrId = userAddr.value.addrId
  }
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/order/confirm',
    method: 'POST',
    data: {
      addrId,
      orderItem: orderEntry === '1' ? JSON.parse(uni.getStorageSync('orderItem')) : undefined,
      basketIds: orderEntry === '0' ? JSON.parse(uni.getStorageSync('basketIds')) : undefined,
      couponIds,
      userChangeCoupon: 1
    }
  })
    .then(({ data }) => {
      uni.hideLoading()
      let orderItemsData = []
      data.shopCartOrders[0].shopCartItemDiscounts?.forEach(itemDiscount => {
        orderItemsData = orderItems.value?.concat(itemDiscount.shopCartItems)
      })
      if (data.shopCartOrders[0].coupons) {
        const canUseCoupons = []
        const unCanUseCoupons = []
        data.shopCartOrders[0].coupons?.forEach(coupon => {
          if (coupon.canUse) {
            canUseCoupons.push(coupon)
          } else {
            unCanUseCoupons.push(coupon)
          }
        })
        coupons.value = {
          totalLength: data.shopCartOrders[0].coupons.length,
          canUseCoupons,
          unCanUseCoupons
        }
      }
      orderItems.value = orderItemsData
      actualTotal.value = data.actualTotal
      total.value = data.total
      totalCount.value = data.totalCount
      userAddr.value = data.userAddr
      transfee.value = data.shopCartOrders[0].transfee
      shopReduce.value = data.shopCartOrders[0].shopReduce
    })
    .catch(err => {
      uni.hideLoading()
      chooseCouponErrHandle(err)
    })
}

/**
 * 优惠券选择出错处理方法
 */
const chooseCouponErrHandle = (res) => {
  // 优惠券不能共用处理方法
  if (res.statusCode == 601) {
    uni.showToast({
      title: res.data,
      icon: 'none',
      duration: 3000,
      success: () => {
        couponIds = []
      }
    })
    setTimeout(() => {
      loadOrderData()
    }, 2500)
  }
}

/**
 * 提交订单
 */
const toPay = () => {
  if (!userAddr.value) {
    uni.showToast({
      title: '请选择地址',
      icon: 'none'
    })
    return
  }
  submitOrder()
}

const remarks = ref('')
const submitOrder = () => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/order/submit',
    method: 'POST',
    data: {
      orderShopParam: [{
        remarks: remarks.value,
        shopId: 1
      }]
    }
  })
    .then(({ data }) => {
      uni.hideLoading()
      normalPay(data.orderNumbers)
    })
}

/**
 * 模拟支付，直接提交成功
 * @param orderNumbers
 */
const normalPay = (orderNumbers) => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/order/normalPay',
    method: 'POST',
    data: {
      orderNumbers
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
            url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + orderNumbers
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

const couponSts = ref(1)
const changeCouponSts = (e) => {
  couponSts.value = e.currentTarget.dataset.sts
}

const popupShow = ref(false)
const showCouponPopup = () => {
  popupShow.value = true
}
const closePopup = () => {
  popupShow.value = false
}

/**
 * 去地址页面
 */
const toAddrListPage = () => {
  uni.navigateTo({
    url: '/pages/delivery-address/delivery-address?order=0'
  })
}

/**
 * 确定选择好的优惠券
 */
const choosedCoupon = () => {
  loadOrderData()
  popupShow.value = false
}

/**
 * 优惠券子组件发过来
 */
const checkCoupon = (e) => {
  const index = couponIds.indexOf(e.detail.couponId)
  if (index === -1) {
    couponIds.push(e.detail.couponId)
  } else {
    couponIds.splice(index, 1)
  }
}
</script>

<style scoped lang="scss">
@use './submit-order.scss';
</style>

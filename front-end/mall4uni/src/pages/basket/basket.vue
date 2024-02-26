<template>
  <view class="container">
    <view class="prod-list">
      <block
        v-for="(item, scIndex) in shopCartItemDiscounts"
        :key="scIndex"
      >
        <view class="prod-block">
          <view
            v-if="item.chooseDiscountItemDto"
            class="discount-tips"
          >
            <text class="text-block">
              {{ wxs.parseDiscount(item.chooseDiscountItemDto.discountRule) }}
            </text>
            <text class="text-list">
              {{
                wxs.parseDiscountMsg(item.chooseDiscountItemDto.discountRule, item.chooseDiscountItemDto.needAmount, item.chooseDiscountItemDto.discount)
              }}
            </text>
          </view>
          <block
            v-for="(prod, index) in item.shopCartItems"
            :key="index"
          >
            <view class="item">
              <view class="btn">
                <label>
                  <checkbox
                    :data-scindex="scIndex"
                    :data-index="index"
                    :value="prod.prodId"
                    :checked="prod.checked"
                    color="#105c3e"
                    @tap="onSelectedItem"
                  />
                </label>
              </view>
              <view class="prodinfo">
                <view class="pic">
                  <image :src="prod.pic" />
                </view>
                <view class="opt">
                  <view class="prod-name">
                    {{ prod.prodName }}
                  </view>
                  <text :class="'prod-info-text ' + (prod.skuName?'':'empty-n')">
                    {{ prod.skuName }}
                  </text>
                  <view class="price-count">
                    <view class="price">
                      <text class="symbol">
                        ￥
                      </text>
                      <text class="big-num">
                        {{ wxs.parsePrice(prod.price)[0] }}
                      </text>
                      <text class="small-num">
                        .{{ wxs.parsePrice(prod.price)[1] }}
                      </text>
                    </view>
                    <view class="m-numSelector">
                      <view
                        class="minus"
                        :data-scindex="scIndex"
                        :data-index="index"
                        @tap="onCountMinus"
                      />
                      <input
                        type="number"
                        :value="prod.prodCount"
                        disabled
                      >
                      <view
                        class="plus"
                        :data-scindex="scIndex"
                        :data-index="index"
                        @tap="onCountPlus"
                      />
                    </view>
                  </view>
                </view>
              </view>
            </view>
          </block>
        </view>
      </block>
    </view>

    <view
      v-if="!shopCartItemDiscounts.length"
      class="empty"
    >
      <view class="img">
        <image src="@/static/images/tabbar/basket.png" />
      </view>
      <view class="txt">
        您还没有添加任何商品哦~
      </view>
    </view>

    <!-- 底部按钮 -->
    <view
      v-if="shopCartItemDiscounts.length>0"
      class="cart-footer"
    >
      <view class="btn all">
        <checkbox
          :checked="allChecked"
          color="#f7d731;"
          @tap="onSelAll"
        />
        全选
      </view>
      <view
        class="btn del"
        @tap="onDelBasket"
      >
        <text>删除</text>
      </view>
      <view class="btn total">
        <view class="finally">
          <text>合计:</text>
          <view class="price">
            <text class="symbol">
              ￥
            </text>
            <text class="big-num">
              {{ wxs.parsePrice(finalMoney)[0] }}
            </text>
            <text class="small-num">
              .{{ wxs.parsePrice(finalMoney)[1] }}
            </text>
          </view>
        </view>
        <view
          v-if="subtractMoney>0"
          class="total-msg"
        >
          总额:￥{{ wxs.toPrice(totalMoney) }} 立减:￥{{ wxs.toPrice(subtractMoney) }}
        </view>
      </view>
      <view
        class="btn settle"
        @tap="toFirmOrder"
      >
        <text>结算</text>
      </view>
    </view>
    <!-- end 底部按钮 -->
  </view>
</template>

<script setup>
const wxs = number()
/**
 * 生命周期函数--监听页面显示
 */
onShow(() => {
  loadBasketData()
  http.getCartCount() // 重新计算购物车总数量
})

const allChecked = ref(false)
const shopCartItemDiscounts = ref([])
const loadBasketData = () => {
  uni.showLoading() // 加载购物车
  http.request({
    url: '/p/shopCart/info',
    method: 'POST',
    data: {}
  })
    .then(({ data }) => {
      if (data && data.length > 0) {
      // 默认不选中
        const shopCartItemDiscountsParam = data[0].shopCartItemDiscounts
        shopCartItemDiscountsParam.forEach(shopCartItemDiscount => {
          shopCartItemDiscount.shopCartItems.forEach(shopCartItem => {
            shopCartItem.checked = false
          })
        })
        shopCartItemDiscounts.value = shopCartItemDiscountsParam
        allChecked.value = false
      } else {
        shopCartItemDiscounts.value = []
      }
      calTotalPrice() // 计算总价
      uni.hideLoading()
    })
}

/**
 * 去结算
 */
const toFirmOrder = () => {
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  const basketIds = []
  shopCartItemDiscountsParam.forEach(shopCartItemDiscount => {
    shopCartItemDiscount.shopCartItems.forEach(shopCartItem => {
      if (shopCartItem.checked) {
        basketIds.push(shopCartItem.basketId)
      }
    })
  })

  if (!basketIds.length) {
    uni.showToast({
      title: '请选择商品',
      icon: 'none'
    })
    return
  }

  uni.setStorageSync('basketIds', JSON.stringify(basketIds))
  uni.navigateTo({
    url: '/pages/submit-order/submit-order?orderEntry=0'
  })
}

/**
 * 全选
 */
const onSelAll = () => {
  const allCheckedParam = !allChecked.value // 改变状态
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  for (let i = 0; i < shopCartItemDiscountsParam.length; i++) {
    const cItems = shopCartItemDiscountsParam[i].shopCartItems
    for (let j = 0; j < cItems.length; j++) {
      cItems[j].checked = allCheckedParam
    }
  }
  allChecked.value = allCheckedParam
  shopCartItemDiscounts.value = shopCartItemDiscountsParam
  calTotalPrice() // 计算总价
}

/**
 * 每一项的选择事件
 * +
 */
const onSelectedItem = (e) => {
  const index = e.currentTarget.dataset.index // 获取data- 传进来的index
  const scindex = e.currentTarget.dataset.scindex
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value // 获取购物车列表
  const checked = shopCartItemDiscountsParam[scindex].shopCartItems[index].checked // 获取当前商品的选中状态
  shopCartItemDiscountsParam[scindex].shopCartItems[index].checked = !checked // 改变状态
  shopCartItemDiscounts.value = shopCartItemDiscountsParam
  checkAllSelected() // 检查全选状态
  calTotalPrice() // 计算总价
}

/**
 * 检查全选状态
 */
const checkAllSelected = () => {
  let allCheckedParam = true
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  let flag = false
  for (let i = 0; i < shopCartItemDiscountsParam.length; i++) {
    const cItems = shopCartItemDiscountsParam[i].shopCartItems
    for (let j = 0; j < cItems.length; j++) {
      if (!cItems[j].checked) {
        allCheckedParam = !allCheckedParam
        flag = true
        break
      }
    }
    if (flag) break
  }
  allChecked.value = allCheckedParam
}

const totalMoney = ref(0)
const subtractMoney = ref(0)
const finalMoney = ref(0)
/**
 * 计算购物车总额
 */
const calTotalPrice = () => {
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  const shopCartIds = []
  for (let i = 0; i < shopCartItemDiscountsParam.length; i++) {
    const cItems = shopCartItemDiscountsParam[i].shopCartItems
    for (let j = 0; j < cItems.length; j++) {
      if (cItems[j].checked) {
        shopCartIds.push(cItems[j].basketId)
      }
    }
  }
  uni.showLoading()
  http.request({
    url: '/p/shopCart/totalPay',
    method: 'POST',
    data: shopCartIds
  })
    .then(({ data }) => {
      if (!data) return
      finalMoney.value = data.finalMoney
      totalMoney.value = data.totalMoney
      subtractMoney.value = data.subtractMoney
      uni.hideLoading()
    })
}

/**
 * 减少数量
 */
const onCountMinus = (e) => {
  const index = e.currentTarget.dataset.index
  const scindex = e.currentTarget.dataset.scindex
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  const prodCount = shopCartItemDiscountsParam[scindex].shopCartItems[index].prodCount
  if (prodCount > 1) {
    updateCount(shopCartItemDiscountsParam, scindex, index, -1)
  }
}

/**
 * 增加数量
 */
const onCountPlus = (e) => {
  const index = e.currentTarget.dataset.index
  const scindex = e.currentTarget.dataset.scindex
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  updateCount(shopCartItemDiscountsParam, scindex, index, 1)
}

/**
 * 改变购物车数量接口
 */
const updateCount = (shopCartItemDiscountsParam, scindex, index, prodCount) => {
  uni.showLoading({
    mask: true
  })
  http.request({
    url: '/p/shopCart/changeItem',
    method: 'POST',
    data: {
      count: prodCount,
      prodId: shopCartItemDiscountsParam[scindex].shopCartItems[index].prodId,
      skuId: shopCartItemDiscountsParam[scindex].shopCartItems[index].skuId,
      shopId: 1
    }
  })
    .then(() => {
      shopCartItemDiscountsParam[scindex].shopCartItems[index].prodCount += prodCount
      shopCartItemDiscounts.value = shopCartItemDiscountsParam
      calTotalPrice() // 计算总价
      uni.hideLoading()
      http.getCartCount() // 重新计算购物车总数量
    })
}

/**
 * 删除购物车商品
 */
const onDelBasket = () => {
  const shopCartItemDiscountsParam = shopCartItemDiscounts.value
  const basketIds = []
  for (let i = 0; i < shopCartItemDiscountsParam.length; i++) {
    const cItems = shopCartItemDiscountsParam[i].shopCartItems
    for (let j = 0; j < cItems.length; j++) {
      if (cItems[j].checked) {
        basketIds.push(cItems[j].basketId)
      }
    }
  }
  if (!basketIds.length) {
    uni.showToast({
      title: '请选择商品',
      icon: 'none'
    })
  } else {
    uni.showModal({
      title: '',
      content: '确认要删除选中的商品吗？',
      confirmColor: '#eb2444',
      success (res) {
        if (res.confirm) {
          uni.showLoading({
            mask: true
          })
          http.request({
            url: '/p/shopCart/deleteItem',
            method: 'DELETE',
            data: basketIds
          })
            .then(() => {
              uni.hideLoading()
              loadBasketData()
            })
        }
      }

    })
  }
}
</script>

<style scoped lang="scss">
@import "./basket.scss";
</style>

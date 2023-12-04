<template>
  <view class="container">
    <!--input列表 -->
    <view class="input-box">
      <view class="section">
        <text>收 货 人</text>
        <input
          placeholder="姓名"
          type="text"
          maxlength="15"
          :value="receiver"
          @input="onReceiverInput"
        >
      </view>
      <view class="section">
        <text>手机号码</text>
        <input
          placeholder="11位手机号码"
          type="number"
          maxlength="11"
          :value="mobile"
          @input="onMobileInput"
        >
      </view>
      <view
        class="section"
        @tap="translate"
      >
        <text>所在地区</text>
        <view class="pca">
          {{ province }} {{ city }} {{ area }}
        </view>
        <view
          class="animation-element-wrapper"
          :animation="animation"
          :style="'visibility:' + (show ? 'visible':'hidden')"
          @tap.stop="hiddenFloatView"
        >
          <view
            class="animation-element"
            @tap.stop="nono"
          >
            <text
              class="right-bt"
              @tap.stop="hiddenFloatView"
            >
              确定
            </text>
            <view class="line" />
            <picker-view
              indicator-style="height: 50rpx;"
              :value="valArr"
              @change="bindChange"
              @tap.stop="nono"
            >
              <!--省-->
              <picker-view-column>
                <view
                  v-for="(item, indexs) in provArray"
                  :key="indexs"
                >
                  {{ item.areaName }}
                </view>
              </picker-view-column>
              <!--地级市-->
              <picker-view-column>
                <view
                  v-for="(item, indexss) in cityArray"
                  :key="indexss"
                >
                  {{ item.areaName }}
                </view>
              </picker-view-column>
              <!--区县-->
              <picker-view-column>
                <view
                  v-for="(item, indexsss) in areaArray"
                  :key="indexsss"
                >
                  {{ item.areaName }}
                </view>
              </picker-view-column>
            </picker-view>
          </view>
        </view>

        <view class="arrow">
          <image src="@/static/images/icon/more.png" />
        </view>
      </view>
      <view class="section">
        <text>详细地址</text>
        <input
          placeholder="如楼号/单元/门牌号"
          type="text"
          :value="addr"
          @input="onAddrInput"
        >
      </view>
    </view>
    <!-- end input列表 -->
    <!-- 功能按钮 -->
    <view class="btn-box">
      <view
        class="keep btn"
        @tap="onSaveAddr"
      >
        <text>保存收货地址</text>
      </view>

      <view
        v-if="addrId!=0"
        class="clear btn"
        @tap="onDeleteAddr"
      >
        <text>删除收货地址</text>
      </view>
    </view>
    <!-- end 功能按钮 -->
  </view>
</template>

<script setup>
const addrId = ref(0)
const city = ref('')
const area = ref('')
const provinceId = ref(0)
const cityId = ref(0)
const areaId = ref(0)
const receiver = ref('')
const mobile = ref('')
const addr = ref('')
const province = ref('')
onLoad((options) => {
  if (options.addrId) {
    uni.showLoading()

    http.request({
      url: '/p/address/addrInfo/' + options.addrId,
      method: 'GET'
    })
      .then(({ data }) => {
        province.value = data.province
        city.value = data.city
        area.value = data.area
        provinceId.value = data.provinceId
        cityId.value = data.cityId
        areaId.value = data.areaId
        receiver.value = data.receiver
        mobile.value = data.mobile
        addr.value = data.addr
        addrId.value = options.addrId
        initCityData(data.provinceId, data.cityId, data.areaId)
        uni.hideLoading()
      })
  } else {
    initCityData(provinceId.value, cityId.value, areaId.value)
  }
})

const provArray = ref([])
const valArr = ref([0, 0, 0])
const initCityData = (provinceId, cityId, areaId) => {
  uni.showLoading()
  http.request({
    url: '/p/area/listByPid',
    method: 'GET',
    data: {
      pid: 0
    }
  })
    .then(({ data }) => {
      provArray.value = data
      if (provinceId) {
        for (const index in data) {
          if (data[index].areaId === provinceId) {
            valArr.value = [parseInt(index), valArr.value[1], valArr.value[2]]
          }
        }
      }
      getCityArray(provinceId || data[0].areaId, cityId, areaId)
      uni.hideLoading()
    })
}

let indexArr = [18, 0, 0]
const areaArray = ref([])
const cityArray = ref([])
/**
 * 滑动事件
 */
const bindChange = (e) => {
  // 判断滑动的是第几个column
  const val = e.detail.value
  // 若省份column做了滑动则定位到地级市和区县第一位
  if (indexArr[0] != val[0]) {
    val[1] = 0
    val[2] = 0 // 更新数据
    // 获取地级市数据
    getCityArray(provArray.value[val[0]].areaId)
  } else {
    // 若省份column未做滑动，地级市做了滑动则定位区县第一位
    if (indexArr[1] != val[1]) {
      val[2] = 0 // 更新数据
      getAreaArray(cityArray.value[val[1]].areaId) // 获取区县数据
    }
  }
  indexArr = val
  valArr.value = [val[0], val[1], val[2]]
  province.value = provArray.value[valArr.value[0]].areaName
  city.value = cityArray.value[valArr.value[1]].areaName
  area.value = areaArray.value[valArr.value[2]].areaName
  provinceId.value = provArray.value[valArr.value[0]].areaId
  cityId.value = cityArray.value[valArr.value[1]].areaId
  areaId.value = areaArray.value[valArr.value[2]].areaId
}

let t = 0
let moveY = 200
const show = ref('')
/**
 * 移动按钮点击事件
 */
const translate = () => {
  if (t == 0) {
    moveY = 0
    show.value = true
    t = 1
  } else {
    moveY = 200
    show.value = false
    t = 0
  }
  animationEvents(moveY, show.value)
}

/**
 * 隐藏弹窗浮层
 */
const hiddenFloatView = () => {
  moveY = 200
  show.value = false
  t = 0
  animationEvents(moveY, show.value)
}

const animation = ref('')
/**
 * 动画事件
 */
const animationEvents = (moveY, showParam) => {
  animation.value = uni.createAnimation({
    transformOrigin: '50% 50%',
    duration: 400,
    timingFunction: 'ease',
    delay: 0
  })
  animation.value.translateY(moveY + 'vh').step()
  animation.value = animation.value.export()
  show.value = showParam
}

/**
 * 根据省份ID获取 城市数据
 */
const getCityArray = (provinceId, cityId, areaId) => {
  http.request({
    url: '/p/area/listByPid',
    method: 'GET',
    data: {
      pid: provinceId
    }
  })
    .then(({ data }) => {
      cityArray.value = data
      if (cityId) {
        for (const index in data) {
          if (data[index].areaId == cityId) {
            valArr.value = [valArr.value[0], parseInt(index), valArr.value[2]]
          }
        }
      }
      getAreaArray(cityId || data[0].areaId, areaId)
      uni.hideLoading()
    })
}

/**
 * 根据城市ID获取 区数据
 */
const getAreaArray = (cityId, areaId) => {
  http.request({
    url: '/p/area/listByPid',
    method: 'GET',
    data: {
      pid: cityId
    }
  }).then(({ data }) => {
    areaArray.value = data
    if (areaId) {
      for (const _index in data) {
        if (data[_index].areaId == areaId) {
          valArr.value = [valArr.value[0], valArr.value[1], parseInt(_index)]
        }
      }
      indexArr = valArr.value
    } else {
      province.value = provArray.value[valArr.value[0]].areaName
      city.value = cityArray.value[valArr.value[1]].areaName
      area.value = areaArray.value[valArr.value[2]].areaName
      provinceId.value = provArray.value[valArr.value[0]].areaId
      cityId.value = cityArray.value[valArr.value[1]].areaId
      areaId.value = areaArray.value[valArr.value[2]].areaId
    }
    uni.hideLoading()
  })
}

/**
 * 保存地址
 */
const onSaveAddr = () => {
  const receiverParam = receiver.value
  const mobileParam = mobile.value
  const addrParam = addr.value

  if (!receiverParam.trim()) {
    receiver.value = ''
    uni.showToast({
      title: '请输入收货人姓名',
      icon: 'none'
    })
    return
  }

  if (!mobileParam) {
    uni.showToast({
      title: '请输入手机号码',
      icon: 'none'
    })
    return
  }

  if (mobileParam.length != 11) {
    uni.showToast({
      title: '请输入正确的手机号码',
      icon: 'none'
    })
    return
  }

  if (!addrParam.trim()) {
    receiver.value = ''
    uni.showToast({
      title: '请输入详细地址',
      icon: 'none'
    })
    return
  }

  uni.showLoading()
  let url = '/p/address/addAddr'
  let method = 'POST'

  if (addrId.value != 0) {
    url = '/p/address/updateAddr'
    method = 'PUT'
  } // 添加或修改地址

  http.request({
    url,
    method,
    data: {
      receiver: receiver.value,
      mobile: mobile.value,
      addr: addr.value,
      province: province.value,
      provinceId: provinceId.value,
      city: city.value,
      cityId: cityId.value,
      areaId: areaId.value,
      area: area.value,
      userType: 0,
      addrId: addrId.value
    }
  })
    .then(() => {
      uni.hideLoading()
      uni.navigateBack({
        delta: 1
      })
    })
}
const onReceiverInput = (e) => {
  receiver.value = e.detail.value
}
const onMobileInput = (e) => {
  mobile.value = e.detail.value
}
const onAddrInput = (e) => {
  addr.value = e.detail.value
}

/**
 * 删除配送地址
 */
const onDeleteAddr = () => {
  uni.showModal({
    title: '',
    content: '确定要删除此收货地址吗？',
    confirmColor: '#eb2444',

    success (res) {
      if (res.confirm) {
        const addrIdParam = addrId.value
        uni.showLoading()
        http.request({
          url: '/p/address/deleteAddr/' + addrIdParam,
          method: 'DELETE'
        })
          .then(() => {
            uni.hideLoading()
            uni.navigateBack({
              delta: 1
            })
          })
      }
    }
  })
}
</script>

<style scoped lang="scss">
@use './editAddress.scss';
</style>

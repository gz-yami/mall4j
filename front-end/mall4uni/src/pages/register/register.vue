<template>
  <view class="register">
    <view class="con">
      <image src="@/static/logo.png" />
      <!-- 登录 -->
      <view class="login-form">
        <view :class="['item',errorTips==1? 'error':'']">
          <view class="account">
            <text class="input-item">
              账号
            </text>
            <input
              type="text"
              data-type="account"
              placeholder-class="inp-palcehoder"
              placeholder="请输入账号名称"
              @input="getInputVal"
            >
          </view>
          <view
            v-if="errorTips==1"
            class="error-text"
          >
            <text class="warning-icon">
              !
            </text>
            请输入账号！
          </view>
        </view>
        <view :class="['item',errorTips==2? 'error':'']">
          <view class="account">
            <text class="input-item">
              密码
            </text>
            <input
              type="password"
              data-type="password"
              placeholder-class="inp-palcehoder"
              placeholder="请输入密码"
              @input="getInputVal"
            >
          </view>
          <view
            v-if="errorTips==2"
            class="error-text"
          >
            <text class="warning-icon">
              !
            </text>
            请输入密码！
          </view>
        </view>
        <view class="operate">
          <view
            class="to-register"
            @tap="toLogin"
          >
            已有账号？
            <text>去登录></text>
          </view>
        </view>
      </view>

      <view>
        <button
          class="authorized-btn"
          @tap="toRegister"
        >
          注册
        </button>
        <button
          class="to-idx-btn"
          @tap="toIndex"
        >
          回到首页
        </button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { encrypt } from '@/utils/crypto.js'

/**
 * 生命周期函数--监听页面显示
 */
onShow(() => {
  // 头部导航标题
  uni.setNavigationBarTitle({
    title: '用户注册'
  })
})

const principal = ref('') // 账号
const credentials = ref('') // 密码
/**
 * 输入框的值
 */
const getInputVal = (e) => {
  const type = e.currentTarget.dataset.type
  if (type == 'account') {
    principal.value = e.detail.value
  } else if (type == 'password') {
    credentials.value = e.detail.value
  }
}

const errorTips = ref(0) // 输入错误提示:  1账号输入  2密码输入
/**
 * 注册
 */
const toRegister = () => {
  if (principal.value.length == 0) {
    errorTips.value = 1
  } else if (credentials.value.length == 0) {
    errorTips.value = 2
  } else {
    errorTips.value = 0

    uni.showLoading()
    http.request({
      url: '/user/register',
      method: 'post',
      data: {
        userName: principal.value,
        passWord: encrypt(credentials.value)
      }
    })
      .then(() => {
        uni.hideLoading()
        uni.showToast({
          title: '注册成功，请登录',
          icon: 'none',
          duration: 1500
        })
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/accountLogin/accountLogin'
          })
        }, 1800)
      })
  }
}
/**
 * 去登陆
 */
const toLogin = () => {
  uni.navigateTo({
    url: '/pages/accountLogin/accountLogin'
  })
}

/**
 * 回到首页
 */
const toIndex = () => {
  uni.switchTab({
    url: '/pages/index/index'
  })
}
</script>

<style lang="scss" scoped>
@import "./register.scss";
</style>

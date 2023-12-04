/* eslint-disable no-console */
// 全局请求封装
import loginMethods from './login'

const http = {
  request: async function (params) {
    // 请求参数处理
    if (Object.prototype.toString.call(params.data) === '[object Array]') {
      params.data = JSON.stringify(params.data)
    } else if (Object.prototype.toString.call(params.data) === '[object Number]') {
      params.data = params.data + ''
    }

    // 刷新token
    if (!params.login && !getApp()?.globalData.isLanding && !params.isRefreshing) {
      await loginMethods.refreshToken()
    }
    // 发起请求
    return new Promise((resolve, reject) => {
      uni.request({
        dataType: 'json',
        responseType: params.responseType === undefined ? 'text' : params.responseType,
        header: {
          Authorization: uni.getStorageSync('Token')
        },
        url: (params.domain ? params.domain : import.meta.env.VITE_APP_BASE_API) + params.url,
        data: params.data,
        method: params.method === undefined ? 'POST' : params.method,
        success: (res) => {
          const responseData = res.data

          // 请求小程序码
          if (params.responseType === 'arraybuffer' && res.statusCode === 200) {
            return resolve(responseData)
          }

          // 00000 请求成功
          // A00002 用于直接显示提示系统的成功,内容由输入决定
          if (responseData.code === '00000' || responseData.code === 'A00002') {
            resolve(responseData)
          }
          // A00004 未授权
          if (responseData.code === 'A00004') {
            // 重设登录后跳转地址
            loginMethods.setRouteUrlAfterLogin()
            uni.removeStorageSync('expiresTimeStamp')
            uni.removeStorageSync('loginResult')
            uni.removeStorageSync('Token')
            if (!params.dontTrunLogin) {
              uni.showModal({
                title: '提示',
                content: uni.getStorageSync('hadLogin') ? '登录已过期，请重新登陆！' : '请先进行登录！',
                cancelText: '取消',
                confirmText: '确定',
                success: res => {
                  if (res.confirm) {
                    uni.navigateTo({
                      url: '/pages/accountLogin/accountLogin'
                    })
                  } else {
                    const router = getCurrentPages()
                    if (router[0].route === 'pages/basket/basket') {
                      uni.switchTab({
                        url: '/pages/index/index'
                      })
                    }
                  }
                }
              })
            }
            resolve(responseData)
          }

          // A00005 服务器出了点小差
          if (responseData.code === 'A00005') {
            this.onRequestFail(params, responseData)
            uni.showToast({
              title: '服务器出了点小差~',
              icon: 'none'
            })
          }

          // 其他异常码
          // A00001 用于直接显示提示用户的错误，内容由输入内容决定
          // A04001 社交账号未绑定
          // A00012 tempUid错误
          // A00006 验证码错误
          if (responseData.code === 'A04001' || responseData.code === 'A00001' || responseData.code === 'A00012' || responseData.code === 'A00006') {
            if (!params.hasCatch) {
              uni.showToast({
                title: responseData.msg || responseData.data || 'Error',
                icon: 'none'
              })
            }
          }

          if (responseData.code !== '00000') {
            reject(responseData)
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '请求失败'
          })
          reject(err)
        }
      })
    })
  },
  getCartCount: () => {
    if (!uni.getStorageSync('Token')) {
      util.removeTabBadge()
      return
    }
    http.request({
      url: '/p/shopCart/prodCount',
      method: 'GET',
      dontTrunLogin: true,
      data: {}
    })
      .then(({ data }) => {
        if (data > 0) {
          uni.setTabBarBadge({
            index: 2,
            text: data + ''
          })
          getApp().globalData.totalCartCount = data
        } else {
          uni.removeTabBarBadge({
            index: 2
          })
          getApp().globalData.totalCartCount = 0
        }
      })
  },
  onRequestFail: (params, responseData) => {
    console.error('============== 请求异常 ==============')
    console.log('接口地址: ', params.url)
    console.log('异常信息: ', responseData)
    console.error('============== 请求异常 end ==========')
  },
  /**
   * 登录成功后执行
   * @param {Object} result  登录成功返回的数据
   * @param {Object} fn 登录成功后的回调
   */
  loginSuccess: (result, fn) => {
  // 保存登陆信息
    wx.setStorageSync('loginResult', result)
    // 保存成功登录标识,token过期判断
    wx.setStorageSync('hadLogin', true)
    const expiresTimeStamp = result.expiresIn * 1000 / 2 + new Date().getTime()
    // 缓存token的过期时间
    uni.setStorageSync('expiresTimeStamp', expiresTimeStamp)

    wx.setStorageSync('Token', result.accessToken) // 把token存入缓存，请求接口数据时要用
    if (fn) {
      fn()
    }
  }
}
export default http

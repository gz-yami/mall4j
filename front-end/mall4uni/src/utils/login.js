const loginMethods = {
  /**
   * 刷新token
   */
  refreshToken: () => {
    const expiresTimeStamp = uni.getStorageSync('expiresTimeStamp')
    if (!uni.getStorageSync('Token') || !expiresTimeStamp) return
    const isExpires = expiresTimeStamp < new Date().getTime()
    const isRefreshing = uni.getStorageSync('isRefreshingToken')
    if (!isExpires || isRefreshing) {
      return
    }
    uni.setStorageSync('isRefreshingToken', true)
    http.request({
      url: '/token/refresh',
      method: 'POST',
      login: true,
      isRefreshing: true,
      dontTrunLogin: true
    }).then(res => {
      uni.setStorageSync('isRefreshingToken', false)
      uni.setStorageSync('expiresTimeStamp', res.data * 1000 / 2 + new Date().getTime())
    }).catch(() => {
      uni.setStorageSync('isRefreshingToken', false)
    })
  },

  /**
   * 设置登录后的跳转地址
   */
  setRouteUrlAfterLogin: () => {
    const pages = getCurrentPages()
    // 登录后的回跳地址
    if (pages[pages.length - 1].route.indexOf('user-login') === -1) {
      uni.setStorageSync('routeUrlAfterLogin', pages[pages.length - 1].$page.fullPath)
    }
  }
}

export default loginMethods

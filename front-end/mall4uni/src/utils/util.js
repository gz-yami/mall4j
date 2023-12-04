const util = {
  formatTime: date => {
    const year = date.getFullYear()
    const month = date.getMonth() + 1
    const day = date.getDate()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const second = date.getSeconds()
    return [year, month, day].map(util.formatNumber).join('/') + ' ' + [hour, minute, second].map(util.formatNumber).join(':')
  },

  formatNumber: n => {
    n = n.toString()
    return n[1] ? n : '0' + n
  },

  formatHtml: content => {
    if (!content) {
      return
    }
    content = content.replace(/<p/gi, '<p style="max-width:100% !important;word-wrap:break-word;word-break:break-word;" ')
    content = content.replace(/<img/gi, '<img style="max-width:100% !important;height:auto !important;margin:0;display:flex;" ')
    content = content.replace(/style="/gi, 'style="max-width:100% !important;table-layout:fixed;word-wrap:break-word;word-break:break-word;')
    content = content.replace(/<table/gi, '<table style="table-layout:fixed;word-wrap:break-word;word-break:break-word;" ')
    content = content.replace(/<td/gi, '<td cellspacing="0" cellpadding="0" style="border-width:1px; border-style:solid; border-color:#666; margin: 0px; padding: 0px;"')
    content = content.replace(/width=/gi, 'sss=')
    content = content.replace(/height=/gi, 'sss=')
    content = content.replace(/\/>/gi, ' style="max-width:100% !important;height:auto !important;margin:0;display:block;" />')
    return content
  },

  /**
   * 移除购物车Tabbar的数字
   */
  removeTabBadge: () => {
    uni.removeTabBarBadge({
      index: 3
    })
  },
  /**
   * 获取链接上的参数
   */
  getUrlKey: (name) => {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || ['', ''])[1]
      .replace(/\+/g, '%20')) || null
  },
  /**
   * 文件地址校验
   * @param fileUrl 获取到的文件路径
   */
  checkFileUrl: (fileUrl) => {
    // 防止 fileUrl 为null时 indexOf() 方法失效报错
    const url = fileUrl || ''
    const baseUrl = import.meta.env.VITE_APP_RESOURCES_URL
    // 判断 fileUrl 中是否已存在基础路径
    const check = url.indexOf(baseUrl) !== -1
    if (check || !fileUrl) {
      return url
    } else {
      return baseUrl + url
    }
  }
}

export default util

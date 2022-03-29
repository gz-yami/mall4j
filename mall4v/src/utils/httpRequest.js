import Vue from 'vue'
import axios from 'axios'
import router from '@/router'
import qs from 'qs'
import merge from 'lodash/merge'
import { clearLoginInfo } from '@/utils'
import { Message } from 'element-ui'

const http = axios.create({
  timeout: 1000 * 30,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  config.headers['Authorization'] = Vue.cookie.get('Authorization') // 请求头带上token
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
  return response
}, error => {
  switch (error.response.status) {
    case 400:
      Message({
        message: error.response.data,
        type: 'error',
        duration: 1500,
        customClass: 'element-error-message-zindex'
      })
      break
    case 401:
      clearLoginInfo()
      router.push({ name: 'login' })
      break
    case 405:
      Message({
        message: 'http请求方式有误',
        type: 'error',
        duration: 1500,
        customClass: 'element-error-message-zindex'
      })
      break
    case 500:
      Message({
        message: '服务器出了点小差，请稍后再试',
        type: 'error',
        duration: 1500,
        customClass: 'element-error-message-zindex'
      })
      break
    case 501:
      Message({
        message: '服务器不支持当前请求所需要的某个功能',
        type: 'error',
        duration: 1500,
        customClass: 'element-error-message-zindex'
      })
      break
  }
  return Promise.reject(error)
})

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi' : process.env.VUE_APP_BASE_API) + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = true) => {
  var defaults = {
    't': new Date().getTime()
  }
  return openDefultParams ? merge(defaults, params) : params
}

/**
 * post请求数据处理
 * @param {*} data 数据对象
 * @param {*} openDefultdata 是否开启默认数据?
 * @param {*} contentType 数据格式
 *  json: 'application/json; charset=utf-8'
 *  form: 'application/x-www-form-urlencoded; charset=utf-8'
 */
http.adornData = (data = {}, openDefultdata = true, contentType = 'json') => {
  var defaults = {
    't': new Date().getTime()
  }
  data = openDefultdata ? merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

export default http

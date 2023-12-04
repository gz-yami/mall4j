import axios from 'axios'
import qs from 'qs'
import cookie from 'vue-cookies'
import router from '@/router'
import merge from 'lodash/merge'
import { clearLoginInfo } from '@/utils'
import { ElMessage } from 'element-plus'

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
http.interceptors.request.use(
  config => {
    config.headers.Authorization = cookie.get('Authorization') // 请求头带上token
    // 只针对get方式进行序列化
    if (config.method === 'get' || config.method === 'GET') {
      config.paramsSerializer = function (params) {
        return qs.stringify(params, { arrayFormat: 'repeat' })
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * 响应拦截
 */
http.interceptors.response.use(
  response => {
    // blob 格式处理
    if (response.request.responseType === 'blob') {
      return response
    }
    const res = response.data
    // 00000 请求成功
    if (res.code === '00000' || res.code === 'A00002') {
      return res
    }
    // A00001 用于直接显示提示用户的错误,内容由输入决定
    if (res.code === 'A00001') {
      ElMessage({
        message: res.msg || res.data || 'Error',
        type: 'error',
        duration: 1.5 * 1000
      })
      return Promise.reject(res)
    }
    // A00004 未授权
    if (res.code === 'A00004') {
      clearLoginInfo()
      router.push({ name: 'login' })
    }

    // A00005 服务器异常
    if (res.code === 'A00005') {
      // eslint-disable-next-line no-console
      console.error('============== 请求异常 ==============', '\n', `接口地址: ${response.config.url.replace(import.meta.env.VITE_APP_BASE_API, '')}`, '\n', `异常信息: ${res}`, '\n', '============== 请求异常 end ==========')
      ElMessage({
        message: '服务器出了点小差，请稍后再试',
        type: 'error',
        duration: 1.5 * 1000,
        customClass: 'element-error-message-zindex'
      })
      return Promise.reject(res)
    }
  },
  error => {
    // eslint-disable-next-line no-console
    console.log('========请求失败========', '\n', error.response, '\n', '========请求失败 end========')
    switch (error.response.status) {
      case 400:
        ElMessage({
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
        ElMessage({
          message: 'http请求方式有误',
          type: 'error',
          duration: 1500,
          customClass: 'element-error-message-zindex'
        })
        break
      case 500:
        ElMessage({
          message: '服务器出了点小差，请稍后再试',
          type: 'error',
          duration: 1500,
          customClass: 'element-error-message-zindex'
        })
        break
      case 501:
        ElMessage({
          message: '服务器不支持当前请求所需要的某个功能',
          type: 'error',
          duration: 1500,
          customClass: 'element-error-message-zindex'
        })
        break
    }
    return Promise.reject(error)
  }
)

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = actionName => {
  return import.meta.env.VITE_APP_BASE_API + actionName
}

/**
 * im请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornImUrl = actionName => {
  return import.meta.env.VITE_APP_IM_API + actionName
}

/**
 * im ws 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornWsImUrl = actionName => {
  return import.meta.env.VITE_APP_WS_IM_API + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = true) => {
  const defaults = {
    t: Date.now()
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
  const defaults = {
    t: Date.now()
  }
  data = openDefultdata ? merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

const uploadFile = function (url, file) {
  const config = {
    // 添加请求头
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: cookie.get('Authorization') // 请求头带上token
    }
  }
  const param = new FormData()
  // 通过append向form对象添加数据
  param.append('file', file)
  return axios.post(url, param, config)
}

export default http
export { uploadFile }

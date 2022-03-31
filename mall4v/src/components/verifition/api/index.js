/**
 * 此处可直接引用自己项目封装好的 axios 配合后端联调
 */

import httpRequest from '@/utils/httpRequest'

// 获取验证图片  以及token
export function reqGet (data) {
  return httpRequest({
    url: httpRequest.adornUrl('/captcha/get'),
    method: 'post',
    data
  })
}

// 滑动或者点选验证
export function reqCheck (data) {
  return httpRequest({
    url: httpRequest.adornUrl('/captcha/check'),
    method: 'post',
    data
  })
}

